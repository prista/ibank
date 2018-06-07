package com.prista.netbanking.service.impl;

import com.prista.netbanking.dao.api.IAccountDao;
import com.prista.netbanking.dao.api.ICardDao;
import com.prista.netbanking.dao.api.filter.AccountFilter;
import com.prista.netbanking.dao.api.model.IAccount;
import com.prista.netbanking.dao.api.model.ICard;
import com.prista.netbanking.service.IAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AccountServiceImpl implements IAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private IAccountDao accountDao;

    @Autowired
    private ICardDao cardDao;

    @Override
    public IAccount createEntity() {
        return accountDao.createEntity();
    }

    @Override
    public ICard createCardEntity() {
        return cardDao.createEntity();
    }

    @Override
    public void save(final IAccount entity) {
        final Date modifedOn = new Date();
        entity.setUpdated(modifedOn);
        if (entity.getId() == null) {
            entity.setCreated(modifedOn);
            accountDao.insert(entity);
            LOGGER.info("new saved account: {}", entity);
        } else {
            accountDao.update(entity);
            LOGGER.info("updated account: {}", entity);
        }
    }

    @Override
    public void save(IAccount account, ICard card) {
        final Date modifiedDate = new Date();
        account.setUpdated(modifiedDate);
        card.setUpdated(modifiedDate);

        if (account.getId() == null) {
            account.setCreated(modifiedDate);
            accountDao.insert(account);
            LOGGER.info("new saved account: {}", account);
            //TODO: 3/31/18   if statement should be here (account_type enum)
            card.setId(account.getId());
            card.setCreated(modifiedDate);
            card.setAccount(account);
            cardDao.insert(card);
            LOGGER.info("new saved card: {}", card);
            account.setCard(card);
        } else {
            accountDao.update(account);
            LOGGER.info("updated account: {}", card);
            cardDao.update(card);
            LOGGER.info("updated card: {}", card);
        }
    }

    @Override
    public void update(final ICard entity) {
        if (entity.getId() == null) {
            LOGGER.error("wrong update card: {}", entity);
            throw new IllegalArgumentException("this method can be used only for UPDATE");
        }
        final Date modifedOn = new Date();
        entity.setUpdated(modifedOn);
        cardDao.update(entity);
        LOGGER.info("updated card: {}", entity);
    }

    @Override
    public IAccount get(final Integer id) {
        final IAccount entity = accountDao.get(id);
        LOGGER.debug("entityById: {}", entity);
        return entity;
    }

    @Override
    public IAccount getCard(final Integer id) {
        final IAccount account = accountDao.get(id);
        final ICard card = cardDao.get(id);

        account.setCard(card);

        if (card != null) {
            card.setAccount(account);
        }
        return account;
    }

    @Override
    public IAccount getFullInfo(Integer id) {
        LOGGER.debug("get entity with fetching: {}", id);
        return accountDao.getFullInfo(id);
    }

    @Override
    public void delete(final Integer id) {
        LOGGER.info("delete entity: {}", id);
        accountDao.delete(id);
    }

    @Override
    public void deleteAll() {
        LOGGER.info("delete all card entities");
        cardDao.deleteAll();
        LOGGER.info("delete all account entities");
        accountDao.deleteAll();
    }

    @Override
    public List<IAccount> getAll() {
        final List<IAccount> all = accountDao.selectAll();
        LOGGER.debug("total count in DB:{}", all.size());
        return all;
    }

    @Override
    public List<IAccount> find(AccountFilter filter) {
        return accountDao.find(filter);
    }

    @Override
    public long getCount(AccountFilter filter) {
        return accountDao.getCount(filter);
    }
}
