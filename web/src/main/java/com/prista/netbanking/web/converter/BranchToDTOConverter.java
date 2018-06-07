package com.prista.netbanking.web.converter;

import com.prista.netbanking.dao.api.model.IBank;
import com.prista.netbanking.dao.api.model.IBranch;
import com.prista.netbanking.dao.api.model.IUserProfile;
import com.prista.netbanking.web.dto.BranchDTO;
import org.hibernate.LazyInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BranchToDTOConverter implements Function<IBranch, BranchDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BranchToDTOConverter.class);

    @Override
    public BranchDTO apply(final IBranch entity) {
        final BranchDTO dto = new BranchDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        final IBank bank = entity.getBank();
        if (bank != null) {
            dto.setBankId(bank.getId());
            dto.setBankName(bank.getName());
        }

        dto.setCity(entity.getCity());
        dto.setStreetAddress(entity.getStreetAddress());
        dto.setPostCode(entity.getPostCode());
        dto.setCreated(entity.getCreated());
        dto.setUpdated(entity.getUpdated());


        /*
        1) Branch has many userProfiles. they are lazy.
        For the edit / info pages, I get the object using the getFullInfo(id) method
        where I explicitly fetch this Set of userProfiles from the DataBase.
        The converter does not falls.


        2) But, when I render a list of branches (using find(filter) method),
        I don't need information about usersProfiles. and I don't fetch them in the find () method.

        In this case, toDTOConverter falls immediately with LazyInitializationException (No session)
        as soon as it tries to get the list of userProfiles of the first branch.

        And here it's necessary to catching lazy in the converter.
        The solution is imperfect, but not meaningless =)
        * */

        try {
            final Set<IUserProfile> userProfiles = entity.getUserProfiles();
            if (userProfiles != null) {
                dto.setUserProfilesIds(userProfiles.stream().map(IUserProfile::getId).collect(Collectors.toSet()));
            }
        } catch (final LazyInitializationException e) {
            LOGGER.warn("ignore conversion of 'userProfiles' collection because of:" + e.getMessage());
        }

        return dto;
    }

}
