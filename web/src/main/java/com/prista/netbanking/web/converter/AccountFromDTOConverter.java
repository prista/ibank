package com.prista.netbanking.web.converter;

import com.prista.netbanking.dao.api.model.IAccount;
import com.prista.netbanking.dao.api.model.IBank;
import com.prista.netbanking.dao.api.model.ICard;
import com.prista.netbanking.dao.api.model.IUserProfile;
import com.prista.netbanking.dao.api.model.enums.AccountType;
import com.prista.netbanking.dao.api.model.enums.CurrencyType;
import com.prista.netbanking.service.IAccountService;
import com.prista.netbanking.service.IBankService;
import com.prista.netbanking.service.IUserProfileService;
import com.prista.netbanking.web.dto.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AccountFromDTOConverter implements Function<AccountDTO, IAccount> {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IUserProfileService userProfileService;

    @Autowired
    private IBankService bankService;

    @Override
    public IAccount apply(AccountDTO dto) {

        IAccount entity = accountService.createEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());

        IUserProfile userProfile = userProfileService.createEntity();
        userProfile.setId(dto.getUserProfileId());
        entity.setUserProfile(userProfile);

        entity.setAccountType(AccountType.valueOf(dto.getAccountType()));

        entity.setBalance(dto.getBalance());
        entity.setCurrency(CurrencyType.valueOf(dto.getCurrency()));
        entity.setLocked(dto.getLocked());
        entity.setDeleted(dto.getDeleted());

        IBank bank = bankService.createEntity();
        bank.setId(dto.getBankId());
        entity.setBank(bank);

        final ICard cardEntity = accountService.createCardEntity();
        cardEntity.setId(dto.getId());
        cardEntity.setCardType(dto.getCard().getCardType());
        cardEntity.setExpirationDate(dto.getCard().getExpirationDate());
        entity.setCard(cardEntity);

        return entity;
    }
}
