package com.prista.netbanking.web.controller;

import com.prista.netbanking.dao.api.filter.AccountFilter;
import com.prista.netbanking.dao.api.model.IAccount;
import com.prista.netbanking.dao.api.model.IBank;
import com.prista.netbanking.dao.api.model.ICard;
import com.prista.netbanking.dao.api.model.IUserProfile;
import com.prista.netbanking.dao.api.model.enums.AccountType;
import com.prista.netbanking.dao.api.model.enums.CurrencyType;
import com.prista.netbanking.service.IAccountService;
import com.prista.netbanking.service.IBankService;
import com.prista.netbanking.service.IUserProfileService;
import com.prista.netbanking.web.converter.AccountFromDTOConverter;
import com.prista.netbanking.web.converter.AccountToDTOCoverter;
import com.prista.netbanking.web.dto.AccountDTO;
import com.prista.netbanking.web.dto.list.ListDTO;
import com.prista.netbanking.web.dto.search.AccountSearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/account")
public class AccountController extends AbstractController<AccountDTO> {

    private static final String SEARCH_FORM_MODEL = "searchFormModel";
    private static final String SEARCH_DTO = AccountController.class.getSimpleName() + "_SEACH_DTO";

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IUserProfileService userProfileService;

    @Autowired
    private IBankService bankService;

    @Autowired
    private AccountToDTOCoverter toDTOConverter;

    @Autowired
    private AccountFromDTOConverter fromDTOConverter;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView index(final HttpServletRequest req, @ModelAttribute(SEARCH_FORM_MODEL) AccountSearchDTO searchDTO,
                              @RequestParam(name = "page", required = false) final Integer pageNumber,
                              @RequestParam(name = "sort", required = false) final String sortColumn) {

        final ListDTO<AccountDTO> listDTO = getListDTO(req);
        listDTO.setPage(pageNumber);
        listDTO.setSort(sortColumn);

        if (req.getMethod().equalsIgnoreCase("get")) {
            searchDTO = getSearchDTO(req);
        } else {
            req.getSession().setAttribute(SEARCH_DTO, searchDTO);
        }

        final AccountFilter filter = new AccountFilter();
        if (searchDTO.getName() != null) {
            filter.setName(searchDTO.getName());
        }
        if ((searchDTO.getUnlockedOnly() != null) && searchDTO.getUnlockedOnly()) {
            filter.setLocked(Boolean.FALSE);
        } else {
            filter.setLocked(null);
        }

        filter.setFetchUserProfile(true);
        filter.setFetchBank(true);

        listDTO.setTotalCount(accountService.getCount(filter));
        applySortAndOrder2Filter(listDTO, filter);

        // use service (then use dao)
        final List<IAccount> entities = accountService.find(filter);
        final List<AccountDTO> dtos = entities.stream().map(toDTOConverter)
                .collect(Collectors.toList());

        listDTO.setList(dtos);
        Map<String, Object> model = new HashMap<>();
        model.put(ListDTO.SESSION_ATTR_NAME, listDTO);
        model.put(SEARCH_FORM_MODEL, searchDTO);

        return new ModelAndView("account.list", model);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView showForm() {

        final Map<String, Object> hashMap = new HashMap<>();
        final AccountDTO dto = new AccountDTO();
        hashMap.put("formModel", dto);

        loadAccountTypes(hashMap);
        loadCurrencies(hashMap);
        loadUserProfileDropdown(hashMap);
        loadBankDropdown(hashMap);

        return new ModelAndView("account.edit", hashMap);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object save(@Valid @ModelAttribute("formModel") final AccountDTO formModel, final BindingResult result)
            throws Exception {
        if (result.hasErrors()) {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("formModel", formModel);
            loadAccountTypes(hashMap);
            loadCurrencies(hashMap);
            loadUserProfileDropdown(hashMap);
            loadBankDropdown(hashMap);

            return new ModelAndView("account.edit", hashMap);
        } else {
            final IAccount entity = fromDTOConverter.apply(formModel);
            final ICard cardEntity = entity.getCard();
            accountService.save(entity, cardEntity);
            return "redirect:/account";
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView viewDetails(
            @PathVariable(name = "id", required = true) final Integer id) {
        final IAccount dbAccount = accountService.getFullInfo(id);
        final AccountDTO dto = toDTOConverter.apply(dbAccount);
        final Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("formModel", dto);
        hashMap.put("readonly", true);

        loadAccountTypes(hashMap);
        loadCurrencies(hashMap);
        loadUserProfileDropdown(hashMap);
        loadBankDropdown(hashMap);
        return new ModelAndView("account.edit", hashMap);
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public ModelAndView edit(
            @PathVariable(name = "id", required = true) final Integer id) {
        final AccountDTO dto = toDTOConverter.apply(accountService.getFullInfo(id));

        final Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("formModel", dto);

        loadAccountTypes(hashMap);
        loadCurrencies(hashMap);
        loadUserProfileDropdown(hashMap);
        loadBankDropdown(hashMap);
        return new ModelAndView("account.edit", hashMap);
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable(name = "id", required = true) final Integer id) {
        accountService.delete(id);
        return "redirect:/account";
    }

    private void loadAccountTypes(final Map<String, Object> hashMap) {
        final List<AccountType> accountTypes = Arrays.asList(AccountType.values());
        final Map<String, String> accountTypesMap = accountTypes.stream().collect(Collectors.toMap(AccountType::name, AccountType::name));

        hashMap.put("typeChoices", accountTypesMap);
    }

    private void loadCurrencies(final Map<String, Object> hashMap) {
        final List<CurrencyType> currencyTypes = Arrays.asList(CurrencyType.values());
        final Map<String, String> currenciesMap = currencyTypes.stream().collect(Collectors.toMap(CurrencyType::name, CurrencyType::name));

        hashMap.put("currenciesChoices", currenciesMap);
    }

    private void loadUserProfileDropdown(Map<String, Object> hashMap) {
        final List<IUserProfile> userProfiles = userProfileService.getAll();
        final Map<Integer, String> userProfilesMap = userProfiles.stream()
                .collect(Collectors.toMap(IUserProfile::getId, IUserProfile::getUsername));

        hashMap.put("userProfileChoices", userProfilesMap);
    }

    private void loadBankDropdown(Map<String, Object> hashMap) {
        final List<IBank> banks = bankService.getAll();
        final Map<Integer, String> banksMap = banks.stream()
                .collect(Collectors.toMap(IBank::getId, IBank::getName));

        hashMap.put("bankChoices", banksMap);
    }

    private AccountSearchDTO getSearchDTO(HttpServletRequest req) {
        AccountSearchDTO searchDTO = (AccountSearchDTO) req.getSession().getAttribute(SEARCH_DTO);
        if (searchDTO == null) {
            searchDTO = new AccountSearchDTO();
            req.getSession().setAttribute(SEARCH_DTO, searchDTO);
        }
        return searchDTO;
    }

}
