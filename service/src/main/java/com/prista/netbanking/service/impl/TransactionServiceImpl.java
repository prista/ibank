package com.prista.netbanking.service.impl;

import com.prista.netbanking.dao.api.ITransactionDao;
import com.prista.netbanking.dao.api.filter.TransactionFilter;
import com.prista.netbanking.dao.api.model.ITransaction;
import com.prista.netbanking.service.ITransactionService;
import org.omg.IOP.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements ITransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            TransactionService.class);
    
    @Autowired
    private ITransactionDao dao;

    @Override
    public ITransaction createEntity() {
        return dao.createEntity();
    }

    @Override
    public void save(final ITransaction entity) {
        final Date modifedOn = new Date();
        entity.setUpdated(modifedOn);
        if (entity.getId() == null) {
            entity.setCreated(modifedOn);
            dao.insert(entity);
            LOGGER.info("new saved transaction: {}", entity);
        } else {
            dao.update(entity);
        }
    }

    @Override
    public ITransaction get(final Integer id) {
        final ITransaction entity = dao.get(id);
        LOGGER.debug("entityById: {}", entity);
        return entity;
    }

    @Override
    public ITransaction getFullInfo(Integer id) {
        final ITransaction entity = dao.getFullInfo(id);
        LOGGER.debug("entityById fullInfo(): {}", entity);
        return entity;
    }

    @Override
    public void delete(final Integer id) {
        LOGGER.info("delete entity: {}", id);
        dao.delete(id);
    }

    @Override
    public void deleteAll() {
        LOGGER.info("delete all transaction entities");
        dao.deleteAll();
    }

    @Override
    public List<ITransaction> getAll() {
        final List<ITransaction> all = dao.selectAll();
        LOGGER.debug("total count in DB:{}", all.size());
        return all;
    }

    @Override
    public List<ITransaction> find(TransactionFilter filter) {
        return dao.find(filter);
    }

    @Override
    public long getCount(TransactionFilter filter) {
        return dao.getCount(filter);
    }

    @Override
    public ITransaction getNewestTransaction() {
        return dao.getNewestTransaction();
    }

    public static boolean validateTransaction(Integer accountOne, Integer accountTwo) {
        return accountOne.equals(accountTwo);
    }
}
