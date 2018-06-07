package com.prista.netbanking.service.impl;

import com.prista.netbanking.dao.api.IPaymentTypeDao;
import com.prista.netbanking.dao.api.filter.PaymentTypeFilter;
import com.prista.netbanking.dao.api.model.IPaymentType;
import com.prista.netbanking.service.IPaymentTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PaymentTypeServiceImpl implements IPaymentTypeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            PaymentTypeServiceImpl.class);
    
    @Autowired
    private IPaymentTypeDao dao;

    @Override
    public IPaymentType createEntity() {
        return dao.createEntity();
    }

    @Override
    public void save(final IPaymentType entity) {
        final Date modifedOn = new Date();
        entity.setUpdated(modifedOn);
        if (entity.getId() == null) {
            entity.setCreated(modifedOn);
            dao.insert(entity);
            LOGGER.info("new saved payment_type: {}", entity);
        } else {
            dao.update(entity);
            LOGGER.info("updated payment_type: {}", entity);
        }
    }

    @Override
    public IPaymentType get(final Integer id) {
        final IPaymentType entity = dao.get(id);
        LOGGER.debug("entityById: {}", entity);
        return entity;
    }

    @Override
    public IPaymentType getFullInfo(Integer id) {
        final IPaymentType entity = dao.getFullInfo(id);
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
        LOGGER.info("delete all payment_type entities");
        dao.deleteAll();
    }

    @Override
    public List<IPaymentType> getAll() {
        final List<IPaymentType> all = dao.selectAll();
        LOGGER.debug("total count in DB:{}", all.size());
        return all;
    }

    @Override
    public List<IPaymentType> find(PaymentTypeFilter filter) {
        return dao.find(filter);
    }

    @Override
    public long getCount(PaymentTypeFilter filter) {
        return dao.getCount(filter);
    }
}
