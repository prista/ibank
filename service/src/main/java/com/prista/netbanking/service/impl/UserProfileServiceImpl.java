package com.prista.netbanking.service.impl;

import com.prista.netbanking.dao.api.IUserProfileDao;
import com.prista.netbanking.dao.api.filter.UserProfileFilter;
import com.prista.netbanking.dao.api.model.IUserProfile;
import com.prista.netbanking.service.IUserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserProfileServiceImpl implements IUserProfileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileServiceImpl.class);
    
    @Autowired
    private IUserProfileDao dao;

    @Override
    public IUserProfile createEntity() {
        return dao.createEntity();
    }

    @Override
    public void save(final IUserProfile entity) {
        final Date modifedOn = new Date();
        entity.setUpdated(modifedOn);
        if (entity.getId() == null) {
            entity.setCreated(modifedOn);
            dao.insert(entity);
            LOGGER.info("new saved userProfile: {}", entity);
        } else {
            dao.update(entity);
            LOGGER.info("updated userProfile: {}", entity);
        }
    }

    @Override
    public IUserProfile get(final Integer id) {
        final IUserProfile entity = dao.get(id);
        LOGGER.debug("entityById: {}", entity);
        return entity;
    }

    @Override
    public void delete(final Integer id) {
        LOGGER.info("delete entity: {}", id);
        dao.delete(id);
    }

    @Override
    public void deleteAll() {
        LOGGER.info("delete all userProfile entities");
        dao.deleteAll();
    }

    @Override
    public List<IUserProfile> getAll() {
        final List<IUserProfile> all = dao.selectAll();
        LOGGER.debug("total count in DB: {}", all.size());
        return all;
    }

    @Override
    public List<IUserProfile> find(UserProfileFilter filter) {
        return dao.find(filter);
    }

    @Override
    public long getCount(UserProfileFilter filter) {
        return dao.getCount(filter);
    }

}
