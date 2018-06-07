package com.prista.netbanking.web.controller;

import com.prista.netbanking.dao.api.filter.BranchFilter;
import com.prista.netbanking.dao.api.model.IBank;
import com.prista.netbanking.dao.api.model.IBranch;
import com.prista.netbanking.dao.api.model.IUserProfile;
import com.prista.netbanking.service.IBankService;
import com.prista.netbanking.service.IBranchService;
import com.prista.netbanking.service.IUserProfileService;
import com.prista.netbanking.web.converter.BranchFromDTOConverter;
import com.prista.netbanking.web.converter.BranchToDTOConverter;
import com.prista.netbanking.web.dto.BranchDTO;
import com.prista.netbanking.web.dto.list.ListDTO;
import com.prista.netbanking.web.dto.search.BranchSearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "branch")
public class BranchController extends AbstractController<BranchDTO> {

    private static final String SEARCH_FORM_MODEL = "searchFormModel";
    private static final String SEARCH_DTO = BranchController.class.getSimpleName() + "_SEACH_DTO";

    @Autowired
    private IBranchService branchService;

    @Autowired
    private IBankService bankService;

    @Autowired
    private IUserProfileService userProfileService;

    @Autowired
    private BranchFromDTOConverter fromDTOConverter;

    @Autowired
    private BranchToDTOConverter toDTOConverter;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView index(final HttpServletRequest req, @ModelAttribute(SEARCH_FORM_MODEL) BranchSearchDTO searchDTO,
                              @RequestParam(name = "page", required = false) final Integer pageNumber,
                              @RequestParam(name = "sort", required = false) final String sortColumn) {

        final ListDTO<BranchDTO> listDTO = getListDTO(req);
        listDTO.setPage(pageNumber);
        listDTO.setSort(sortColumn);

        if (req.getMethod().equalsIgnoreCase("get")) {
            searchDTO = getSearchDTO(req);
        } else {
            req.getSession().setAttribute(SEARCH_DTO, searchDTO);
        }

        final BranchFilter filter = new BranchFilter();
        if (searchDTO.getName() != null) {
            filter.setName(searchDTO.getName());
        }
        if (searchDTO.getBankName() != null) {
            filter.setBankName(searchDTO.getBankName());
        }
        filter.setFetchBank(true);

        listDTO.setTotalCount(branchService.getCount(filter));
        applySortAndOrder2Filter(listDTO, filter);

        // use service (then use dao)
        final List<IBranch> entities = branchService.find(filter);
        final List<BranchDTO> dtos = entities.stream().map(toDTOConverter)
                .collect(Collectors.toList());

        listDTO.setList(dtos);
        listDTO.setTotalCount(branchService.getCount(filter));


        final HashMap<String, Object> models = new HashMap<>();
        models.put(ListDTO.SESSION_ATTR_NAME, listDTO);
        models.put(SEARCH_FORM_MODEL, searchDTO);
        return new ModelAndView("branch.list", models);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView showForm() {
        final Map<String, Object> hashMap = new HashMap<>();
        final BranchDTO dto = new BranchDTO();
        hashMap.put("formModel", dto);
        loadComboboxesBranches(hashMap);
        return new ModelAndView("branch.edit", hashMap);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object save(@Valid @ModelAttribute("formModel") final BranchDTO formModel, final BindingResult result) {
        if (result.hasErrors()) {
            final Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("formModel", formModel);
            loadComboboxesBranches(hashMap);
            return new ModelAndView("branch.edit", hashMap);
        } else {
            final IBranch entity = fromDTOConverter.apply(formModel);
            branchService.save(entity);
            return "redirect:/branch";
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView viewDetails(
            @PathVariable(name = "id", required = true) final Integer id) {
        final IBranch dbBranch = branchService.getFullInfo(id);
        final BranchDTO dto = toDTOConverter.apply(dbBranch);
        final HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("formModel", dto);
        hashMap.put("readonly", true);
        loadComboboxesBranches(hashMap);
        return new ModelAndView("branch.edit", hashMap);
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public ModelAndView edit(
            @PathVariable(name = "id", required = true) final Integer id) {
        final BranchDTO dto = toDTOConverter.apply(branchService.getFullInfo(id));

        final HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("formModel", dto);
        loadComboboxesBranches(hashMap);
        return new ModelAndView("branch.edit", hashMap);
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable(name = "id", required = true) final Integer id) {
        branchService.delete(id);
        return "redirect:/branch";
    }

    private void loadComboboxesBranches(final Map<String, Object> hashMap) {
        final List<IBank> banks = bankService.getAll();

        /*
         * final Map<Integer, String> banksMap = new HashMap<>(); 
         * for (final IBank iBank : banks)
         * { banksMap.put(iBank.getId(), iBank.getName());
         * }
         */

        final Map<Integer, String> banksMap = banks.stream()
                .collect(Collectors.toMap(IBank::getId, IBank::getName));

        final Map<Integer, String> userProfilesMap = userProfileService.getAll().stream()
                .collect(Collectors.toMap(IUserProfile::getId, IUserProfile::getUsername));
        hashMap.put("userProfilesChoices", userProfilesMap);

        hashMap.put("banksChoices", banksMap);
    }

    private BranchSearchDTO getSearchDTO(final HttpServletRequest req) {
        BranchSearchDTO searchDTO = (BranchSearchDTO) req.getSession().getAttribute(SEARCH_DTO);
        if (searchDTO == null) {
            searchDTO = new BranchSearchDTO();
            req.getSession().setAttribute(SEARCH_DTO, searchDTO);
        }
        return searchDTO;
    }
}
