package com.prista.netbanking.service.impl;

import com.prista.netbanking.dao.api.IBankDao;
import com.prista.netbanking.dao.api.filter.BankFilter;
import com.prista.netbanking.dao.api.model.IBank;
import com.prista.netbanking.service.IBankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BankServiceImpl implements IBankService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankServiceImpl.class);
    
    @Autowired
    private IBankDao dao;

    @Override
    public IBank createEntity() {
        return dao.createEntity();
    }

    @Override
    public void save(final IBank entity) {
        final Date modifiedOn = new Date();
        entity.setUpdated(modifiedOn);
        if (entity.getId() == null) {
            entity.setCreated(modifiedOn);
            dao.insert(entity);
            LOGGER.info("new saved bank: {}", entity);
        } else {
            dao.update(entity);
            LOGGER.info("updated bank: {}", entity);
        }
    }

    @Override
    public IBank get(final Integer id) {
        final IBank entity = dao.get(id);
        LOGGER.debug("bank by id: {}", entity);
        return entity;
    }

    @Override
    public void delete(final Integer id) {
        LOGGER.info("delete bank: {}", id);
        dao.delete(id);
    }

    @Override
    public void deleteAll() {
        LOGGER.info("delete all bank entities");
        dao.deleteAll();
    }

    @Override
    public List<IBank> getAll() {
        final List<IBank> all = dao.selectAll();
        LOGGER.debug("total count of bank entities in DB: {}", all.size());
        return all;
    }

    @Override
    public List<IBank> find(final BankFilter filter) {
        return dao.find(filter);
    }

    @Override
    public long getCount(final BankFilter filter) {
        return dao.getCount(filter);
    }

}
