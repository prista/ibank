package com.prista.netbanking.web.converter;

import com.prista.netbanking.dao.api.model.IUserProfile;
import com.prista.netbanking.web.dto.UserProfileDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserProfileToDTOConverter implements Function<IUserProfile, UserProfileDTO> {

    @Override
    public UserProfileDTO apply(IUserProfile entity) {
        final UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setId(entity.getId());
        userProfileDTO.setUsername(entity.getUsername());
        userProfileDTO.setPassword(entity.getPassword());
        userProfileDTO.setRole(entity.getRole());
        userProfileDTO.setCreated(entity.getCreated());
        userProfileDTO.setUpdated(entity.getUpdated());
        return userProfileDTO;
    }

}
