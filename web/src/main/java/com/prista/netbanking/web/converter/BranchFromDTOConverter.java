package com.prista.netbanking.web.converter;

import com.prista.netbanking.dao.api.model.IBank;
import com.prista.netbanking.dao.api.model.IBranch;
import com.prista.netbanking.dao.api.model.IUserProfile;
import com.prista.netbanking.service.IBankService;
import com.prista.netbanking.service.IBranchService;
import com.prista.netbanking.service.IUserProfileService;
import com.prista.netbanking.web.dto.BranchDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BranchFromDTOConverter implements Function<BranchDTO, IBranch> {

    @Autowired
    private IBranchService branchService;
    @Autowired
    private IBankService bankService;

    @Autowired
    private IUserProfileService userProfileService;

    @Override
    public IBranch apply(final BranchDTO dto) {
        final IBranch entity = branchService.createEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setStreetAddress(dto.getStreetAddress());
        entity.setCity(dto.getCity());
        entity.setPostCode(dto.getPostCode());

        final IBank bank = bankService.createEntity();
        bank.setId(dto.getBankId());
        entity.setBank(bank);

        final Set<Integer> userProfilesIds = dto.getUserProfilesIds();
        if (CollectionUtils.isNotEmpty(userProfilesIds)) {
            entity.setUserProfiles(userProfilesIds.stream().map((id) -> {
                final IUserProfile userProfile = userProfileService.createEntity();
                userProfile.setId(id);
                return userProfile;
            }).collect(Collectors.toSet()));
        }

        return entity;
    }
}
