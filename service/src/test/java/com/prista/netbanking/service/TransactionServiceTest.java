package com.prista.netbanking.service;

import com.prista.netbanking.dao.api.filter.TransactionFilter;
import com.prista.netbanking.dao.api.model.IAccount;
import com.prista.netbanking.dao.api.model.IPaymentType;
import com.prista.netbanking.dao.api.model.ITransaction;
import com.prista.netbanking.dao.api.model.enums.TransactionType;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TransactionServiceTest extends AbstractTest {

    @Before
    public void cleanTables() {
        getTransactionService().deleteAll();
    }

    @Test
    public void createTest() {
        final ITransaction entity = saveNewTransaction();

        final ITransaction entityFromDB = getTransactionService().getFullInfo(entity.getId());

        assertEquals(entity.getFromAccount().getId(), entityFromDB.getFromAccount().getId());
        assertEquals(entity.getToAccount().getId(), entityFromDB.getToAccount().getId());
        assertEquals(entity.getAmount(), entityFromDB.getAmount());
        assertEquals(entity.getNote(), entityFromDB.getNote());
        assertEquals(entity.getPaymentType().getId(), entityFromDB.getPaymentType().getId());
        assertEquals(entity.getTransactionType(), entityFromDB.getTransactionType());
        assertNotNull(entityFromDB.getId());
        assertNotNull(entityFromDB.getCreated());
        assertNotNull(entityFromDB.getUpdated());
        assertEquals(entity.getUpdated(), entityFromDB.getUpdated());
    }

    @Test
    public void getFullInfoTest() {
        final ITransaction entity = getTransactionService().createEntity();

        final IAccount from = saveNewAccount();
        final IAccount to = saveNewAccount();

        entity.setFromAccount(from);
        entity.setToAccount(to);
        entity.setAmount(getRandomDoubleValue(100, 221));
        entity.setNote("note-" + getRandomPrefix());
        entity.setPaymentType(saveNewPaymentType());

        final TransactionType[] allTypes = TransactionType.values();
        final int randomIndex = Math.max(0, getRANDOM().nextInt(allTypes.length) - 1);
        entity.setTransactionType(allTypes[randomIndex]);

        getTransactionService().save(entity);

        final ITransaction transactionEntityFromDB = getTransactionService().getFullInfo(entity.getId());

        assertEquals(entity.getId(), transactionEntityFromDB.getId());
        assertNotNull(transactionEntityFromDB.getFromAccount().getName());
        assertNotNull(transactionEntityFromDB.getToAccount().getName());
        assertNotNull(transactionEntityFromDB.getPaymentType().getName());

    }

    @Test
    public void simpleFindTest() { // just checks the query syntax and makes
        // basic assertions
        for (int i = 0; i < 10; i++) {
            saveNewTransaction();
        }

        final TransactionFilter transactionFilter = new TransactionFilter();
        transactionFilter.setLimit(3);
        transactionFilter.setSortColumn("amount");
        transactionFilter.setSortOrder(Boolean.FALSE);
        transactionFilter.setFetchAccount(true);
        transactionFilter.setFetchPaymentType(true);
        final List<ITransaction> findResultList = getTransactionService().find(transactionFilter);
        assertEquals(3, findResultList.size());
        for (ITransaction transaction : findResultList) {
            assertNotNull(transaction.getFromAccount().getName());
            assertNotNull(transaction.getToAccount().getName());
            assertNotNull(transaction.getPaymentType().getName());
        }
    }

    @Test
    public void newestTransactionTest() throws InterruptedException { // check server ping
        saveNewTransaction();
        Thread.sleep(2000);
        final ITransaction firstEntityFromDB = getTransactionService().getNewestTransaction();
        saveNewTransaction();
        final ITransaction newestEntityFromDB = getTransactionService().getNewestTransaction();
        assertNotNull(firstEntityFromDB);
        assertNotNull(newestEntityFromDB);
        assertTrue(newestEntityFromDB.getCreated().after(firstEntityFromDB.getCreated()));
    }

    @Test
    public void searchTest() {
        for (int i = 0; i < 2; i++) {
            saveNewTransaction();
        }
        ITransaction transaction = saveNewTransaction();
        String transactionNote = transaction.getNote();

        final TransactionFilter transactionFilter = new TransactionFilter();
        transactionFilter.setLimit(3);
        transactionFilter.setNote(transactionNote);
        transactionFilter.setFetchAccount(true);
        transactionFilter.setFetchPaymentType(true);
        List<ITransaction> findResultList = getTransactionService().find(transactionFilter);
        assertEquals(findResultList.size(), 1);
        assertEquals(findResultList.get(0).getNote(), transactionNote);
    }

    @Test
    public void testUpdate() {
        final ITransaction entity = saveNewTransaction();

        entity.setNote("new-note");
        getTransactionService().save(entity);

        final ITransaction entityFromDB = getTransactionService().getFullInfo(entity.getId());


        assertEquals(entity.getFromAccount().getId(), entityFromDB.getFromAccount().getId());
        assertEquals(entity.getToAccount().getId(), entityFromDB.getToAccount().getId());
        assertEquals(entity.getAmount(), entityFromDB.getAmount());
        assertEquals(entity.getNote(), entityFromDB.getNote());
        assertEquals(entity.getPaymentType().getId(), entityFromDB.getPaymentType().getId());
        assertEquals(entity.getTransactionType(), entityFromDB.getTransactionType());

        assertNotNull(entityFromDB.getId());
        assertNotNull(entityFromDB.getCreated());
        assertNotNull(entityFromDB.getUpdated());
        assertTrue(entity.getCreated().before(entity.getUpdated()));
    }

    @Test
    public void testGetAll() {
        final int initialCount = getTransactionService().getAll().size();

        final int randomObjectsCount = getRandomObjectsCount();
        for (int i = 0; i < randomObjectsCount; i++) {
            saveNewTransaction();
        }

        final List<ITransaction> allEntities = getTransactionService().getAll();

        for (final ITransaction entityFromDB : allEntities) {
            assertNotNull(entityFromDB.getFromAccount());
            assertNotNull(entityFromDB.getToAccount());
            assertNotNull(entityFromDB.getAmount());
            assertNotNull(entityFromDB.getNote());
            assertNotNull(entityFromDB.getPaymentType());
        }

        assertEquals(randomObjectsCount + initialCount, allEntities.size());
    }

    @Test
    public void testDelete() {
        final ITransaction entity = saveNewTransaction();
        getTransactionService().delete(entity.getId());
        assertNull(getTransactionService().get(entity.getId()));
    }

    @Test
    public void testDeleteAll() {
        saveNewAccount();
        getTransactionService().deleteAll();
        assertEquals(0, getTransactionService().getAll().size());
    }

    private ITransaction saveNewTransaction() {
        final ITransaction entity = getTransactionService().createEntity();

        final IAccount from = saveNewAccount();
        final IAccount to = saveNewAccount();

        entity.setFromAccount(from);
        entity.setToAccount(to);
        entity.setAmount(getRandomDoubleValue(100, 221));
        entity.setNote("note-" + getRandomPrefix());
        entity.setPaymentType(saveNewPaymentType());

        final TransactionType[] allTypes = TransactionType.values();
        final int randomIndex = Math.max(0, getRANDOM().nextInt(allTypes.length) - 1);
        entity.setTransactionType(allTypes[randomIndex]);

        getTransactionService().save(entity);
        return entity;
    }

    private IPaymentType saveNewPaymentType() {

        final IPaymentType parent = getPaymentTypeService().createEntity();
        parent.setParent(null);
        parent.setName("parentType-" + getRandomPrefix());
        getPaymentTypeService().save(parent);

        final IPaymentType entity = getPaymentTypeService().createEntity();
        entity.setParent(parent);
        entity.setName("paymentType-" + getRandomPrefix());

        getPaymentTypeService().save(entity);
        return entity;
    }
}
