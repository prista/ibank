package com.prista.netbanking.service;

import com.prista.netbanking.dao.api.filter.BankFilter;
import com.prista.netbanking.dao.api.model.IBank;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BankServiceTest extends AbstractTest {

    @Before
    public void cleanTables() {
        getTransactionService().deleteAll();
        getAccountService().deleteAll();
        getBranchService().deleteAll();
        getBankService().deleteAll();
    }

    @Test
    public void testCreate() {
        final IBank entity = saveNewBank();

        final IBank entityFromDB = getBankService().get(entity.getId());

        assertEquals(entity.getName(), entityFromDB.getName());
        assertNotNull(entityFromDB.getId());
        assertNotNull(entityFromDB.getCreated());
        assertNotNull(entityFromDB.getUpdated());
        assertTrue(entityFromDB.getCreated().equals(entityFromDB.getUpdated()));
    }

    @Test
    public void simpleFindTest() { // just checks the query syntax and makes
        // basic assertions
        for (int i = 0; i < 10; i++) {
            saveNewBank();
        }

        final BankFilter bankFilter = new BankFilter();
        bankFilter.setLimit(3);
        bankFilter.setSortColumn("name");
        bankFilter.setSortOrder(Boolean.FALSE);
        final List<IBank> findResultList = getBankService().find(bankFilter);
        assertEquals(findResultList.size(), 3);
    }

    @Test
    public void searchTest() {
        for (int i = 0; i < 2; i++) {
            saveNewBank();
        }
        IBank bank = saveNewBank();
        String bankName = bank.getName();

        final BankFilter bankFilter = new BankFilter();
        bankFilter.setLimit(3);
        bankFilter.setName(bankName);
        final List<IBank> findResultList = getBankService().find(bankFilter);
        assertEquals(findResultList.size(), 1);
        assertEquals(findResultList.get(0).getName(), bankName);
    }

    @Test
    public void testUpdate() {
        final IBank entity = saveNewBank();

        entity.setName("new-name");
        getBankService().save(entity);

        final IBank entityFromDb = getBankService().get(entity.getId());

        assertEquals(entity.getName(), entityFromDb.getName());
        assertNotNull(entityFromDb.getId());
        assertNotNull(entityFromDb.getCreated());
        assertNotNull(entityFromDb.getUpdated());
        assertEquals(entity.getId(), entity.getId());
        assertEquals(entity.getCreated(), entity.getCreated());
        assertTrue(entity.getCreated().before(entity.getUpdated()));
    }

    @Test
    public void testGetAll() {
        final int intialCount = getBankService().getAll().size();

        final int randomObjectsCount = getRandomObjectsCount();
        for (int i = 0; i < randomObjectsCount; i++) {
            saveNewBank();
        }

        final List<IBank> allEntities = getBankService().getAll();

        for (final IBank entityFromDB : allEntities) {
            assertNotNull(entityFromDB.getName());
            assertNotNull(entityFromDB.getId());
            assertNotNull(entityFromDB.getCreated());
            assertNotNull(entityFromDB.getUpdated());
        }

        assertEquals(randomObjectsCount + intialCount, allEntities.size());
    }

    @Test
    public void testDelete() {
        final IBank entity = saveNewBank();
        getBankService().delete(entity.getId());
        assertNull(getBankService().get(entity.getId()));
    }

    @Test
    public void testDeleteAll() {
        saveNewBank();
        getBankService().deleteAll();
        assertEquals(0, getBankService().getAll().size());
    }

}
