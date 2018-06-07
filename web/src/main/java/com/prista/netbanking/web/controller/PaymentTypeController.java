package com.prista.netbanking.web.controller;

import com.prista.netbanking.dao.api.filter.PaymentTypeFilter;
import com.prista.netbanking.dao.api.model.IPaymentType;
import com.prista.netbanking.service.IPaymentTypeService;
import com.prista.netbanking.web.converter.PaymentTypeFromDTOConverter;
import com.prista.netbanking.web.converter.PaymentTypeToDTOConverter;
import com.prista.netbanking.web.dto.PaymentTypeDTO;
import com.prista.netbanking.web.dto.list.ListDTO;
import com.prista.netbanking.web.dto.search.PaymentTypeSearchDTO;
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
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/payment-type")
public class PaymentTypeController extends AbstractController<PaymentTypeDTO> {

    private static final String SEARCH_FORM_MODEL = "searchFormModel";

    @Autowired
    private IPaymentTypeService paymentTypeService;

    @Autowired
    private PaymentTypeToDTOConverter toDTOConverter;

    @Autowired
    private PaymentTypeFromDTOConverter fromDTOConverter;

    @Autowired
    private ApplicationContext appContext;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView index(final HttpServletRequest req, @ModelAttribute(SEARCH_FORM_MODEL) final PaymentTypeSearchDTO formData,
                              @RequestParam(name = "page", required = false) final Integer pageNumber,
                              @RequestParam(name = "sort", required = false) final String sortColumn) {

        final ListDTO<PaymentTypeDTO> listDTO = getListDTO(req);
        listDTO.setPage(pageNumber);
        listDTO.setSort(sortColumn);

        final PaymentTypeSearchDTO searchDTO = (PaymentTypeSearchDTO) appContext.getBean("paymentTypeSearchDTO");

        if (req.getMethod().equalsIgnoreCase("post")) {
            searchDTO.setName(formData.getName());
        }

        final PaymentTypeFilter filter = new PaymentTypeFilter();
        if (searchDTO.getName() != null) {
            filter.setName(searchDTO.getName());
        }

        listDTO.setTotalCount(paymentTypeService.getCount(filter));
        // build filter method
        applySortAndOrder2Filter(listDTO, filter);

        // use service (then use dao)
        List<IPaymentType> entities = paymentTypeService.find(filter);
        listDTO.setList(entities.stream().map(toDTOConverter).collect(Collectors.toList()));

        Map<String, Object> model = new HashMap<>();

        model.put(ListDTO.SESSION_ATTR_NAME, listDTO);
        model.put(SEARCH_FORM_MODEL, searchDTO);
        return new ModelAndView("payment-type.list", model);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView showForm() {

        final Map<String, Object> hashMap = new HashMap<>();
        final PaymentTypeDTO dto = new PaymentTypeDTO();
        hashMap.put("formModel", dto);
        loadParentDropdown(hashMap);

        return new ModelAndView("payment-type.edit", hashMap);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object save(@Valid @ModelAttribute("formModel") final PaymentTypeDTO formModel, final BindingResult result)
            throws Exception {
        if (result.hasErrors()) {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("formModel", formModel);
            loadParentDropdown(hashMap);

            return new ModelAndView("payment-type.edit", hashMap);
        } else {
            final IPaymentType entity = fromDTOConverter.apply(formModel);
            paymentTypeService.save(entity);
            return "redirect:/payment-type";
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView viewDetails(
            @PathVariable(name = "id", required = true) final Integer id) {
        final IPaymentType dbPaymentType = paymentTypeService.getFullInfo(id);
        final PaymentTypeDTO dto = toDTOConverter.apply(dbPaymentType);
        final Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("formModel", dto);
        hashMap.put("readonly", true);
        loadParentDropdown(hashMap);
        return new ModelAndView("payment-type.edit", hashMap);
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public ModelAndView edit(
            @PathVariable(name = "id", required = true) final Integer id) {
        final PaymentTypeDTO dto = toDTOConverter.apply(paymentTypeService.getFullInfo(id));

        final Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("formModel", dto);
        loadParentDropdown(hashMap);
        return new ModelAndView("payment-type.edit", hashMap);
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable(name = "id", required = true) final Integer id) {
        paymentTypeService.delete(id);
        return "redirect:/payment-type";
    }

    private void loadParentDropdown(Map<String, Object> hashMap) {
        final List<IPaymentType> paymentTypes = paymentTypeService.getAll();
        final Map<Integer, String> parentsMap = paymentTypes.stream()
                .collect(Collectors.toMap(IPaymentType::getId, IPaymentType::getName));

        hashMap.put("parentChoices", parentsMap);
    }

}
