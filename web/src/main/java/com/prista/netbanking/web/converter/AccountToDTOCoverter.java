package com.prista.netbanking.web.converter;

import com.prista.netbanking.dao.api.model.IAccount;
import com.prista.netbanking.dao.api.model.IBank;
import com.prista.netbanking.dao.api.model.ICard;
import com.prista.netbanking.dao.api.model.IUserProfile;
import com.prista.netbanking.web.dto.AccountDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AccountToDTOCoverter implements Function<IAccount, AccountDTO> {

    @Override
    public AccountDTO apply(IAccount entity) {
        AccountDTO dto = new AccountDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        IUserProfile userProfile = entity.getUserProfile();
        if (userProfile != null) {
            dto.setUserProfileId(userProfile.getId());
            dto.setUserProfileUserName(userProfile.getUsername());
        }

        dto.setAccountType(entity.getAccountType().name());
        dto.setBalance(entity.getBalance());
        dto.setCurrency(entity.getCurrency().name());
        dto.setLocked(entity.getLocked());

        IBank bank = entity.getBank();
        if (bank != null) {
            dto.setBankId(bank.getId());
            dto.setBankName(bank.getName());
        }

        dto.setDeleted(entity.getDeleted());
        dto.setCreated(entity.getCreated());
        dto.setUpdated(entity.getUpdated());

        final ICard card = entity.getCard();
        if (card != null) {
            dto.getCard().setCardType(card.getCardType());
            dto.getCard().setExpirationDate(card.getExpirationDate());
        }

        return dto;
    }
}
