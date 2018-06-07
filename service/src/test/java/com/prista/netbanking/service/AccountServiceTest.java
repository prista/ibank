package com.prista.netbanking.service;

import com.prista.netbanking.dao.api.filter.AccountFilter;
import com.prista.netbanking.dao.api.model.IAccount;
import com.prista.netbanking.dao.api.model.ICard;
import com.prista.netbanking.dao.api.model.enums.AccountType;
import com.prista.netbanking.dao.api.model.enums.CurrencyType;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class AccountServiceTest extends AbstractTest {

    @Before
    public void cleanTables() {
        getTransactionService().deleteAll();
        getAccountService().deleteAll();
    }

    @Test
    public void createTest() {
        final IAccount entity = saveNewAccount();

        final IAccount entityFromDb = getAccountService().getFullInfo(entity.getId());

        assertEquals(entity.getName(), entityFromDb.getName());
        assertEquals(entity.getUserProfile().getId(), entityFromDb.getUserProfile().getId());
        assertEquals(entity.getAccountType(), entityFromDb.getAccountType());
        assertEquals(entity.getBalance(), entityFromDb.getBalance());
        assertEquals(entity.getCurrency(), entityFromDb.getCurrency());
        assertEquals(entity.getLocked(), entityFromDb.getLocked());
        assertEquals(entity.getBank().getId(), entityFromDb.getBank().getId());
        assertEquals(entity.getDeleted(), entityFromDb.getDeleted());
        assertNotNull(entityFromDb.getId());
        assertNotNull(entityFromDb.getCreated());
        assertNotNull(entityFromDb.getUpdated());
        assertTrue(entityFromDb.getCreated().equals(entityFromDb.getUpdated()));
    }

    @Test
    public void createCardTest() {
        final IAccount accountEntity = getAccountService().createEntity();
        accountEntity.setName("account-" + getRandomPrefix());
        accountEntity.setUserProfile(saveNewUserProfile());

        final AccountType[] allTypes = AccountType.values();
        int randomIndex = Math.max(0, getRANDOM().nextInt(allTypes.length) - 1);
        accountEntity.setAccountType(allTypes[randomIndex]);

        accountEntity.setBalance(getRandomDoubleValue(100, 121));

        final CurrencyType[] currencyTypes = CurrencyType.values();
        randomIndex = Math.max(0, getRANDOM().nextInt(currencyTypes.length) - 1);
        accountEntity.setCurrency(currencyTypes[randomIndex]);

        accountEntity.setLocked(false);
        accountEntity.setBank(saveNewBank());
        accountEntity.setDeleted(false);


        final ICard cardEntity = saveNewCard(accountEntity);

        final IAccount accountEntityFromDB = getAccountService().getFullInfo(accountEntity.getId());

        assertEquals(accountEntity.getName(), accountEntityFromDB.getName());

        assertEquals(accountEntity.getUserProfile().getId(), accountEntityFromDB.getUserProfile().getId());
        assertEquals(accountEntity.getUserProfile().getUsername(), accountEntityFromDB.getUserProfile().getUsername());

        assertEquals(accountEntity.getAccountType(), accountEntityFromDB.getAccountType());
        assertEquals(accountEntity.getBalance(), accountEntityFromDB.getBalance());
        assertEquals(accountEntity.getCurrency(), accountEntityFromDB.getCurrency());
        assertEquals(accountEntity.getLocked(), accountEntityFromDB.getLocked());

        assertEquals(accountEntity.getBank().getId(), accountEntityFromDB.getBank().getId());
        assertEquals(accountEntity.getBank().getName(), accountEntityFromDB.getBank().getName());

        assertEquals(accountEntity.getDeleted(), accountEntityFromDB.getDeleted());
        assertNotNull(accountEntityFromDB.getId());
        assertNotNull(accountEntityFromDB.getCreated());
        assertNotNull(accountEntityFromDB.getUpdated());
        assertEquals(accountEntityFromDB.getCreated(), accountEntityFromDB.getUpdated());

        final ICard cardEntityFromDB = accountEntityFromDB.getCard();
        assertNotNull(cardEntityFromDB.getId());
        assertEquals(cardEntity.getCardType(), cardEntityFromDB.getCardType());
        assertEquals(cardEntity.getExpirationDate(), cardEntityFromDB.getExpirationDate());
        assertNotNull(cardEntityFromDB.getCreated());
        assertNotNull(cardEntityFromDB.getUpdated());
        assertEquals(cardEntityFromDB.getCreated(), cardEntityFromDB.getUpdated());
    }

    @Test
    public void simpleFindTest() {
        // basic assertions
        for (int i = 0; i < 10; i++) {
            saveNewAccount();
        }

        final AccountFilter accountFilter = new AccountFilter();
        accountFilter.setLimit(3);
        accountFilter.setSortColumn("name");
        accountFilter.setSortOrder(Boolean.FALSE);
        accountFilter.setOffset(3);
        accountFilter.setFetchUserProfile(true);

        List<IAccount> findResultList = getAccountService().find(accountFilter);

        assertEquals(3, findResultList.size());

        accountFilter.setFetchBank(true);
        findResultList = getAccountService().find(accountFilter);
        assertEquals(3, findResultList.size());

        for (IAccount account : findResultList) {
            assertNotNull(account.getUserProfile().getUsername());
            assertNotNull(account.getBank().getName());
        }
    }

    @Test
    public void getFullInfoTest() {
        final IAccount accountEntity = getAccountService().createEntity();
        accountEntity.setName("account-" + getRandomPrefix());
        accountEntity.setUserProfile(saveNewUserProfile());

        final AccountType[] allTypes = AccountType.values();
        int randomIndex = Math.max(0, getRANDOM().nextInt(allTypes.length) - 1);
        accountEntity.setAccountType(allTypes[randomIndex]);

        accountEntity.setBalance(getRandomDoubleValue(100, 121));

        final CurrencyType[] currencyTypes = CurrencyType.values();
        randomIndex = Math.max(0, getRANDOM().nextInt(currencyTypes.length) - 1);
        accountEntity.setCurrency(currencyTypes[randomIndex]);

        accountEntity.setLocked(false);
        accountEntity.setBank(saveNewBank());
        accountEntity.setDeleted(false);

        accountEntity.setCard(saveNewCard(accountEntity));

        getAccountService().save(accountEntity);

        final IAccount accountEntityFromDB = getAccountService().getFullInfo(accountEntity.getId());

        assertEquals(accountEntity.getId(), accountEntityFromDB.getId());
        assertNotNull(accountEntityFromDB.getUserProfile().getUsername());
        assertNotNull(accountEntityFromDB.getBank().getName());
    }

    @Test
    public void searchTest() {
        for (int i = 0; i < 2; i++) {
            saveNewAccount();
        }
        IAccount account = saveNewAccount();
        String accountName = account.getName();

        final AccountFilter accountFilter = new AccountFilter();
        accountFilter.setLimit(3);
        accountFilter.setName(accountName);
        accountFilter.setFetchBank(true);
        accountFilter.setFetchUserProfile(true);
        List<IAccount> findResultList = getAccountService().find(accountFilter);
        assertEquals(findResultList.size(), 1);
        assertEquals(findResultList.get(0).getName(), accountName);
        assertNotNull(findResultList.get(0).getBank().getName());
        assertNotNull(findResultList.get(0).getUserProfile().getUsername());
    }

    @Test
    public void testUpdate() {
        final IAccount entity = saveNewAccount();

        entity.setName("new-name");
        getAccountService().save(entity);

        final IAccount entityFromDB = getAccountService().getFullInfo(entity.getId());


        assertEquals(entity.getName(), entityFromDB.getName());

        assertEquals(entity.getUserProfile().getId(), entityFromDB.getUserProfile().getId());
        assertEquals(entity.getUserProfile().getUsername(), entityFromDB.getUserProfile().getUsername());

        assertEquals(entity.getAccountType(), entityFromDB.getAccountType());
        assertEquals(entity.getBalance(), entityFromDB.getBalance());
        assertEquals(entity.getCurrency(), entityFromDB.getCurrency());
        assertEquals(entity.getLocked(), entityFromDB.getLocked());

        assertEquals(entity.getBank().getId(), entityFromDB.getBank().getId());
        assertEquals(entity.getBank().getName(), entityFromDB.getBank().getName());

        assertEquals(entity.getDeleted(), entityFromDB.getDeleted());
        assertNotNull(entityFromDB.getId());
        assertNotNull(entityFromDB.getCreated());
        assertNotNull(entityFromDB.getUpdated());
        assertTrue(entity.getCreated().before(entity.getUpdated()));
    }

    @Test
    public void testCardUpdate() {
        final IAccount accountEntity = getAccountService().createEntity();
        accountEntity.setName("account-" + getRandomPrefix());
        accountEntity.setUserProfile(saveNewUserProfile());

        final AccountType[] allTypes = AccountType.values();
        int randomIndex = Math.max(0, getRANDOM().nextInt(allTypes.length) - 1);
        accountEntity.setAccountType(allTypes[randomIndex]);

        accountEntity.setBalance(getRandomDoubleValue(100, 121));

        final CurrencyType[] currencyTypes = CurrencyType.values();
        randomIndex = Math.max(0, getRANDOM().nextInt(currencyTypes.length) - 1);
        accountEntity.setCurrency(currencyTypes[randomIndex]);

        accountEntity.setLocked(false);
        accountEntity.setBank(saveNewBank());
        accountEntity.setDeleted(false);

        final ICard cardEntity = saveNewCard(accountEntity);

        cardEntity.setCardType("new-cardType");
        getAccountService().save(accountEntity, cardEntity);

        final IAccount accountEntityFromDB = getAccountService().getCard(accountEntity.getId());

        final ICard cardEntityFromDB = accountEntityFromDB.getCard();
        assertNotNull(cardEntityFromDB.getId());
        assertEquals(cardEntity.getCardType(), cardEntityFromDB.getCardType());
        assertEquals(cardEntity.getExpirationDate(), cardEntityFromDB.getExpirationDate());
        assertNotNull(cardEntityFromDB.getCreated());
        assertNotNull(cardEntityFromDB.getUpdated());
        assertTrue(cardEntity.getCreated().before(cardEntity.getUpdated()));
    }

    @Test
    public void testGetAll() {
        final int initialCount = getAccountService().getAll().size();

        final int randomObjectsCount = getRandomObjectsCount();
        for (int i = 0; i < randomObjectsCount; i++) {
            saveNewAccount();
        }

        final List<IAccount> allEntities = getAccountService().getAll();

        for (final IAccount entityFromDB : allEntities) {
            assertNotNull(entityFromDB.getName());
            assertNotNull(entityFromDB.getId());
            assertNotNull(entityFromDB.getId());
            assertNotNull(entityFromDB.getCreated());
            assertNotNull(entityFromDB.getUpdated());
        }

        assertEquals(randomObjectsCount + initialCount, allEntities.size());
    }

    @Test
    public void testDelete() {
        final IAccount entity = saveNewAccount();
        getAccountService().delete(entity.getId());
        assertNull(getAccountService().get(entity.getId()));
    }

    @Test
    public void testDeleteAll() {
        saveNewAccount();
        getAccountService().deleteAll();
        assertEquals(0, getAccountService().getAll().size());
    }

    private ICard saveNewCard(IAccount account) {
        final ICard entity = getAccountService().createCardEntity();
        entity.setCardType("cardtype-" + getRandomPrefix());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 2);
        Date expDate = calendar.getTime();

        entity.setExpirationDate(expDate);

        getAccountService().save(account, entity);
        return entity;
    }
}
