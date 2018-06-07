package com.prista.netbanking.web.controller;

import com.prista.netbanking.dao.api.filter.UserProfileFilter;
import com.prista.netbanking.dao.api.model.IUserProfile;
import com.prista.netbanking.service.IUserProfileService;
import com.prista.netbanking.web.converter.UserProfileFromDTOConverter;
import com.prista.netbanking.web.converter.UserProfileToDTOConverter;
import com.prista.netbanking.web.dto.UserProfileDTO;
import com.prista.netbanking.web.dto.list.ListDTO;
import com.prista.netbanking.web.dto.search.UserProfileSearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/userprofile")
public class UserProfileController extends AbstractController<UserProfileDTO> {

    private static final String SEARCH_FORM_MODEL = "searchFormModel";
    private static final String SEARCH_DTO = UserProfileController.class.getSimpleName() + "_SEACH_DTO";

    @Autowired
    private IUserProfileService userProfileService;

    @Autowired
    private UserProfileFromDTOConverter fromDTOConverter;

    @Autowired
    private UserProfileToDTOConverter toDTOConverter;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView index(final HttpServletRequest req, @ModelAttribute(SEARCH_FORM_MODEL) UserProfileSearchDTO searchDTO,
                              @RequestParam(name = "page", required = false) final Integer pageNumber,
                              @RequestParam(name = "sort", required = false) final String sortColumn) {

/*        ExtendedUsernamePasswordAuthenticationToken token =
                (ExtendedUsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        Integer currentUserId = token.getId();
        // IT'S EQUAL:
        Integer curentUserId = AuthHelper.getLoggedUserId();*/


        final ListDTO<UserProfileDTO> listDTO = getListDTO(req);
        listDTO.setPage(pageNumber);
        listDTO.setSort(sortColumn);

        if (req.getMethod().equalsIgnoreCase("get")) {
            // do not use empty payload which comes in case of GET
            searchDTO = getSearchDTO(req);
        } else {
            req.getSession().setAttribute(SEARCH_DTO, searchDTO);
        }

        final UserProfileFilter filter = new UserProfileFilter();
        if (searchDTO.getUsername() != null) {
            filter.setUserName(searchDTO.getUsername());
        }
        if (searchDTO.getRole() != null) {
            filter.setRole(searchDTO.getRole());
        }

        listDTO.setTotalCount(userProfileService.getCount(filter));
        applySortAndOrder2Filter(listDTO, filter);

        // use service (then use dao)
        final List<IUserProfile> entities = userProfileService.find(filter);
        listDTO.setList(entities.stream().map(toDTOConverter).collect(Collectors.toList()));

        final HashMap<String, Object> models = new HashMap<>();
        models.put(ListDTO.SESSION_ATTR_NAME, listDTO);
        return new ModelAndView("userprofile.list", models);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView showForm() {

        final HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("formModel", new UserProfileDTO());
        hashMap.put("action", "Add");

        return new ModelAndView("userprofile.edit", hashMap);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String save(@Valid @ModelAttribute("formModel") final UserProfileDTO formModel, final BindingResult result)
            throws Exception {
        if (result.hasErrors()) {
            return "userprofile.edit";
        } else {
            final IUserProfile entity = fromDTOConverter.apply(formModel);
            userProfileService.save(entity);
            return "redirect:/userprofile";
        }
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable(name = "id", required = true) final Integer id) {
        userProfileService.delete(id);
        return "redirect:/userprofile";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView viewDetails(@PathVariable(name = "id", required = true) final Integer id) {
        final IUserProfile dbModel = userProfileService.get(id);
        final UserProfileDTO dto = toDTOConverter.apply(dbModel);
        final HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("formModel", dto);
        hashMap.put("readonly", true);
        hashMap.put("action", "Info");

        return new ModelAndView("userprofile.edit", hashMap);
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable(name = "id", required = true) final Integer id) {
        final UserProfileDTO dto = toDTOConverter.apply(userProfileService.get(id));

        final HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("formModel", dto);
        hashMap.put("action", "Edit");

        return new ModelAndView("userprofile.edit", hashMap);
    }

    private UserProfileSearchDTO getSearchDTO(final HttpServletRequest req) {
        UserProfileSearchDTO searchDTO = (UserProfileSearchDTO) req.getSession().getAttribute(SEARCH_DTO);
        if (searchDTO == null) {
            searchDTO = new UserProfileSearchDTO();
            req.getSession().setAttribute(SEARCH_DTO, searchDTO);
        }
        return searchDTO;
    }

}
