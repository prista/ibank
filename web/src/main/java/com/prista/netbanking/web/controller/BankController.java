package com.prista.netbanking.web.controller;

import com.prista.netbanking.dao.api.filter.BankFilter;
import com.prista.netbanking.dao.api.model.IBank;
import com.prista.netbanking.service.IBankService;
import com.prista.netbanking.web.converter.BankFromDTOConverter;
import com.prista.netbanking.web.converter.BankToDTOConverter;
import com.prista.netbanking.web.dto.BankDTO;
import com.prista.netbanking.web.dto.list.ListDTO;
import com.prista.netbanking.web.dto.search.BankSearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/bank")
public class BankController extends AbstractController<BankDTO> {

    private static final String SEARCH_FORM_MODEL = "searchFormModel";

    @Autowired
    private IBankService bankService;

    @Autowired
    private BankFromDTOConverter fromDTOConverter;

    @Autowired
    private BankToDTOConverter toDTOConverter;
    
    @Autowired
    private ApplicationContext appContext;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView index(final HttpServletRequest req, @ModelAttribute(SEARCH_FORM_MODEL) final BankSearchDTO formData,
                              @RequestParam(name = "page", required = false) final Integer pageNumber,
                              @RequestParam(name = "sort", required = false) final String sortColumn) {

        final ListDTO<BankDTO> listDTO = getListDTO(req);
        listDTO.setPage(pageNumber);
        listDTO.setSort(sortColumn);

        final BankSearchDTO  searchDTO = (BankSearchDTO) appContext.getBean("bankSearchDTO");
        
        if (req.getMethod().equalsIgnoreCase("post")) {
            // do not use empty payload which comes in case of GET
            searchDTO.setName(formData.getName());
        } 

        final BankFilter filter = new BankFilter();
        if (searchDTO.getName() != null) {
            filter.setName(searchDTO.getName());
        }

        listDTO.setTotalCount(bankService.getCount(filter));
        applySortAndOrder2Filter(listDTO, filter);

        // use service (then use dao)
        final List<IBank> entities = bankService.find(filter);
        listDTO.setList(entities.stream().map(toDTOConverter).collect(Collectors.toList()));

        final HashMap<String, Object> models = new HashMap<>();
        models.put(ListDTO.SESSION_ATTR_NAME, listDTO);
        models.put(SEARCH_FORM_MODEL, searchDTO);
        return new ModelAndView("bank.list", models);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView showForm() {

        final HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("formModel", new BankDTO());
        hashMap.put("action", "Add");

        return new ModelAndView("bank.edit", hashMap);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String save(@Valid @ModelAttribute("formModel") final BankDTO formModel, final BindingResult result)
            throws Exception {
        if (result.hasErrors()) {
            return "bank.edit";
        } else {
            final IBank entity = fromDTOConverter.apply(formModel);
            bankService.save(entity);
            return "redirect:/bank";
        }
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable(name = "id", required = true) final Integer id) {
        bankService.delete(id);
        return "redirect:/bank";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView viewDetails(@PathVariable(name = "id", required = true) final Integer id) {
        final IBank dbModel = bankService.get(id);
        final BankDTO dto = toDTOConverter.apply(dbModel);
        final HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("formModel", dto);
        hashMap.put("readonly", true);
        hashMap.put("action", "Info");

        return new ModelAndView("bank.edit", hashMap);
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable(name = "id", required = true) final Integer id) {
        final BankDTO dto = toDTOConverter.apply(bankService.get(id));

        final HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("formModel", dto);
        hashMap.put("action", "Edit");

        return new ModelAndView("bank.edit", hashMap);
    }

}
