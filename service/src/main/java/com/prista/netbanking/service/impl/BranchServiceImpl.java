package com.prista.netbanking.service.impl;

import com.prista.netbanking.dao.api.IBranchDao;
import com.prista.netbanking.dao.api.filter.BranchFilter;
import com.prista.netbanking.dao.api.model.IBranch;
import com.prista.netbanking.service.IBranchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BranchServiceImpl implements IBranchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BranchServiceImpl.class);

    @Autowired
    private IBranchDao dao;

    @Override
    public IBranch createEntity() {
        return dao.createEntity();
    }

    @Override
    public void save(final IBranch entity) {
        final Date modifedOn = new Date();
        entity.setUpdated(modifedOn);
        if (entity.getId() == null) {
            entity.setCreated(modifedOn);
            dao.insert(entity);
            LOGGER.info("new saved branch: {}", entity);
        } else {
            dao.update(entity);
            LOGGER.info("updated branch: {}", entity);
        }
    }

    @Override
    public IBranch getFullInfo(final Integer id) {
        final IBranch entity = dao.getFullInfo(id);
        LOGGER.info("entityById: {}", entity);
        return entity;
    }

    @Override
    public IBranch get(final Integer id) {
        return dao.get(id);
    }

    @Override
    public void delete(final Integer id) {
        LOGGER.info("delete entity: {}", id);

        // remove all refereces
        final IBranch branch = dao.get(id);
        branch.getUserProfiles().clear();
        dao.update(branch);

        dao.delete(id);
    }

    @Override
    public void deleteAll() {
        LOGGER.info("delete all branch entities");
        dao.deleteAll();
    }

    @Override
    public List<IBranch> getAll() {
        final List<IBranch> all = dao.selectAll();
        LOGGER.debug("total count in DB:{}", all.size());
        return all;
    }

    public List<IBranch> find(final BranchFilter filter) {
        return dao.find(filter);
    }

    @Override
    public long getCount(final BranchFilter filter) {
        return dao.getCount(filter);
    }

}
