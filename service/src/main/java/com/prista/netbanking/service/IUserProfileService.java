package com.prista.netbanking.service;

import com.prista.netbanking.dao.api.filter.UserProfileFilter;
import com.prista.netbanking.dao.api.model.IUserProfile;

import javax.transaction.Transactional;
import java.util.List;

public interface IUserProfileService {

    IUserProfile get(Integer id);

    List<IUserProfile> getAll();

    @Transactional
    void save(IUserProfile entity);

    @Transactional
    void delete(Integer id);

    @Transactional
    void deleteAll();

    IUserProfile createEntity();

    List<IUserProfile> find(UserProfileFilter filter);

    long getCount(UserProfileFilter filter);
}
