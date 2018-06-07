package com.prista.netbanking.dao.api;

import com.prista.netbanking.dao.api.filter.UserProfileFilter;
import com.prista.netbanking.dao.api.model.IUserProfile;

import java.util.List;
import java.util.Set;

public interface IUserProfileDao extends BaseDao<IUserProfile, Integer> {

    long getCount(UserProfileFilter filter);

    List<IUserProfile> find(UserProfileFilter filter);

    Set<IUserProfile> getByBranch(Integer id);

}
