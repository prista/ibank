package com.prista.netbanking.web.controller;

import com.prista.netbanking.dao.api.filter.TransactionFilter;
import com.prista.netbanking.dao.api.model.IAccount;
import com.prista.netbanking.dao.api.model.IPaymentType;
import com.prista.netbanking.dao.api.model.ITransaction;
import com.prista.netbanking.dao.api.model.enums.TransactionType;
import com.prista.netbanking.service.IAccountService;
import com.prista.netbanking.service.IPaymentTypeService;
import com.prista.netbanking.service.ITransactionService;
import com.prista.netbanking.web.converter.TransactionFromDTOConverter;
import com.prista.netbanking.web.converter.TransactionToDTOConverter;
import com.prista.netbanking.web.dto.TransactionDTO;
import com.prista.netbanking.web.dto.list.ListDTO;
import com.prista.netbanking.web.dto.search.TransactionSearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/transaction")
public class TransactionController extends AbstractController<TransactionDTO> {

    private static final String SEARCH_FORM_MODEL = "searchFormModel";

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IPaymentTypeService paymentTypeService;

    @Autowired
    private TransactionToDTOConverter toDTOConverter;

    @Autowired
    private TransactionFromDTOConverter fromDTOConverter;

    @Autowired
    private ApplicationContext appContext;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView index(final HttpServletRequest req, @ModelAttribute(SEARCH_FORM_MODEL) TransactionSearchDTO formData,
                              @RequestParam(name = "page", required = false) final Integer pageNumber,
                              @RequestParam(name = "sort", required = false) final String sortColumn) {

        final ListDTO<TransactionDTO> listDTO = getListDTO(req);
        listDTO.setPage(pageNumber);
        listDTO.setSort(sortColumn);

        final TransactionSearchDTO searchDTO = (TransactionSearchDTO) appContext.getBean("transactionSearchDTO");

        if (req.getMethod().equalsIgnoreCase("post")) {
            // do not use empty payload which comes in case of GET
            searchDTO.setNote(formData.getNote());
        }

        final TransactionFilter filter = new TransactionFilter();
        if (searchDTO.getNote() != null) {
            filter.setNote(searchDTO.getNote());
        }
        filter.setFetchAccount(true);
        filter.setFetchPaymentType(true);

        listDTO.setTotalCount(transactionService.getCount(filter));
        applySortAndOrder2Filter(listDTO, filter);

        // use service (then use dao)
        final List<ITransaction> enities = transactionService.find(filter);
        final List<TransactionDTO> dtos = enities.stream().map(toDTOConverter).collect(Collectors.toList());

        listDTO.setList(dtos);
        final Map<String, Object> model = new HashMap<>();
        model.put(ListDTO.SESSION_ATTR_NAME, listDTO);
        model.put(SEARCH_FORM_MODEL, searchDTO);
        ITransaction newestTransaction = null;

        if (listDTO.getTotalCount() != 0) {
            newestTransaction = transactionService.getNewestTransaction();
        }

        model.put("newestTransactionId", newestTransaction == null ? null : newestTransaction.getId());
        return new ModelAndView("transaction.list", model);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView showForm() {

        final Map<String, Object> hashMap = new HashMap<>();
        final TransactionDTO dto = new TransactionDTO();
        hashMap.put("formModel", dto);
        loadAccountDropdown(hashMap);
        loadPaymentTypeDropdown(hashMap);
        loadTransactionTypes(hashMap);

        return new ModelAndView("transaction.edit", hashMap);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object save(@Valid @ModelAttribute("formModel") final TransactionDTO formModel, final BindingResult result)
            throws Exception {
        if (result.hasErrors()) {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("formModel", formModel);
            loadAccountDropdown(hashMap);
            loadPaymentTypeDropdown(hashMap);
            loadTransactionTypes(hashMap);

            return new ModelAndView("transaction.edit", hashMap);
        } else {
            final ITransaction entity = fromDTOConverter.apply(formModel);
            transactionService.save(entity);
            return "redirect:/transaction";
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView viewDetails(
            @PathVariable(name = "id", required = true) final Integer id) {
        final ITransaction dbTransaction = transactionService.getFullInfo(id);
        final TransactionDTO dto = toDTOConverter.apply(dbTransaction);
        final Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("formModel", dto);
        hashMap.put("readonly", true);
        loadAccountDropdown(hashMap);
        loadPaymentTypeDropdown(hashMap);
        loadTransactionTypes(hashMap);
        return new ModelAndView("transaction.edit", hashMap);
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public ModelAndView edit(
            @PathVariable(name = "id", required = true) final Integer id) {
        final TransactionDTO dto = toDTOConverter.apply(transactionService.getFullInfo(id));

        final Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("formModel", dto);
        loadAccountDropdown(hashMap);
        loadPaymentTypeDropdown(hashMap);
        loadTransactionTypes(hashMap);
        return new ModelAndView("transaction.edit", hashMap);
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable(name = "id", required = true) final Integer id) {
        transactionService.delete(id);
        return "redirect:/transaction";
    }

    @RequestMapping(value = "/lastId", method = RequestMethod.GET)
    public ResponseEntity<Integer> checkNewest() {
        final ITransaction newestTransaction = transactionService.getNewestTransaction();
        return new ResponseEntity<>(newestTransaction == null ? null : newestTransaction.getId(), HttpStatus.OK);
    }

    private void loadAccountDropdown(Map<String, Object> hashMap) {
        final List<IAccount> accounts = accountService.getAll();
        final Map<Integer, String> accountsMap = accounts.stream()
                .collect(Collectors.toMap(IAccount::getId, IAccount::getName));

        hashMap.put("accountChoices", accountsMap);
    }

    private void loadPaymentTypeDropdown(Map<String, Object> hashMap) {
        final List<IPaymentType> paymentTypes = paymentTypeService.getAll();
        final Map<Integer, String> paymentTypesMap = paymentTypes.stream()
                .collect(Collectors.toMap(IPaymentType::getId, IPaymentType::getName));

        hashMap.put("paymentTypeChoices", paymentTypesMap);
    }

    private void loadTransactionTypes(final Map<String, Object> hashMap) {
        final List<TransactionType> transactionTypes = Arrays.asList(TransactionType.values());
        final Map<String, String> transactionTypesMap = transactionTypes.stream().collect(Collectors.toMap(TransactionType::name, TransactionType::name));

        hashMap.put("typeChoices", transactionTypesMap);
    }
}
