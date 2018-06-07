package com.prista.netbanking.web.converter;

import com.prista.netbanking.dao.api.model.IUserProfile;
import com.prista.netbanking.service.IUserProfileService;
import com.prista.netbanking.web.dto.UserProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserProfileFromDTOConverter implements Function<UserProfileDTO, IUserProfile> {

    @Autowired
    private IUserProfileService userProfileService;

    @Override
    public IUserProfile apply(UserProfileDTO dto) {
        final IUserProfile entity = userProfileService.createEntity();
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        return entity;
    }

}
