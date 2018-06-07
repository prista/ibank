package com.prista.netbanking.service;

import com.prista.netbanking.dao.api.filter.PaymentTypeFilter;
import com.prista.netbanking.dao.api.model.IPaymentType;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PaymentTypeServiceTest extends AbstractTest {

    @Before
    public void cleanTables() {
        getTransactionService().deleteAll();
        getPaymentTypeService().deleteAll();
    }

    @Test
    public void createTest() {
        final IPaymentType entity = saveNewPaymentType();

        final IPaymentType entityFromDB = getPaymentTypeService().getFullInfo(entity.getId());

        assertEquals(entity.getName(), entityFromDB.getName());
        assertEquals(entity.getParent().getId(), entityFromDB.getParent().getId());
        assertNotNull(entityFromDB.getId());
        assertNotNull(entityFromDB.getCreated());
        assertNotNull(entityFromDB.getUpdated());
        assertEquals(entity.getUpdated(), entityFromDB.getUpdated());
    }

    @Test
    public void createWithoutParentTest() {
        final IPaymentType entity = saveNewPaymentTypeWithoutParent();

        final IPaymentType entityFromDB = getPaymentTypeService().getFullInfo(entity.getId());

        assertEquals(entity.getName(), entityFromDB.getName());

        assertNull(entity.getParent());
        assertNull(entityFromDB.getParent());
        assertNotNull(entityFromDB.getId());
        assertNotNull(entityFromDB.getCreated());
        assertNotNull(entityFromDB.getUpdated());
        assertEquals(entity.getUpdated(), entityFromDB.getUpdated());
    }

    @Test
    public void simpleFindTest() {
        for (int i = 0; i < 10; i++) {
            saveNewPaymentType();
        }

        final PaymentTypeFilter paymentTypeFilter = new PaymentTypeFilter();
        paymentTypeFilter.setLimit(3);
        paymentTypeFilter.setSortColumn("name");
        paymentTypeFilter.setSortOrder(Boolean.FALSE);
        final List<IPaymentType> findResultList = getPaymentTypeService().find(paymentTypeFilter);
        assertEquals(3, findResultList.size());
    }

    @Test
    public void searchTest() {
        for (int i = 0; i < 2; i++) {
            saveNewPaymentType();
        }
        IPaymentType paymentType = saveNewPaymentType();
        String paymentTypeName = paymentType.getName();

        final PaymentTypeFilter paymentTypeFilter = new PaymentTypeFilter();
        paymentTypeFilter.setLimit(3);
        paymentTypeFilter.setName(paymentTypeName);
        List<IPaymentType> findResultList = getPaymentTypeService().find(paymentTypeFilter);
        assertEquals(findResultList.size(), 1);
        assertEquals(findResultList.get(0).getName(), paymentTypeName);
    }

    @Test
    public void testUpdate() {
        final IPaymentType entity = saveNewPaymentType();

        entity.setName("new-name");
        getPaymentTypeService().save(entity);

        final IPaymentType entityFromDB = getPaymentTypeService().getFullInfo(entity.getId());


        assertEquals(entity.getName(), entityFromDB.getName());
        assertEquals(entity.getParent().getId(), entityFromDB.getParent().getId());
        assertNotNull(entityFromDB.getId());
        assertNotNull(entityFromDB.getCreated());
        assertNotNull(entityFromDB.getUpdated());
        assertTrue(entity.getCreated().before(entity.getUpdated()));
    }

    @Test
    public void getFullInfoTest() {
        final IPaymentType parent = getPaymentTypeService().createEntity();
        parent.setParent(null);
        parent.setName("parentType-" + getRandomPrefix());
        getPaymentTypeService().save(parent);

        final IPaymentType entity = getPaymentTypeService().createEntity();
        entity.setParent(parent);
        entity.setName("paymentType-" + getRandomPrefix());

        getPaymentTypeService().save(entity);

        final IPaymentType entityFromDB = getPaymentTypeService().getFullInfo(entity.getId());

        assertEquals(entity.getId(), entityFromDB.getId());
        assertNotNull(entity.getParent().getName());
        assertEquals(entity.getParent().getName(), entityFromDB.getParent().getName());
    }

    @Test
    public void testGetAll() {
        final int initialCount = getPaymentTypeService().getAll().size();

        final int randomObjectsCount = getRandomObjectsCount();
        for (int i = 0; i < randomObjectsCount; i++) {
            saveNewPaymentType();
        }

        final List<IPaymentType> allEntities = getPaymentTypeService().getAll();

        for (final IPaymentType entityFromDB : allEntities) {
            assertNotNull(entityFromDB.getName());
            assertNotNull(entityFromDB.getId());
            assertNotNull(entityFromDB.getId());
            assertNotNull(entityFromDB.getCreated());
            assertNotNull(entityFromDB.getUpdated());
        }

        assertEquals(randomObjectsCount * 2 + initialCount, allEntities.size());
    }

    @Test
    public void testDelete() {
        final IPaymentType entity = saveNewPaymentType();
        getPaymentTypeService().delete(entity.getId());
        assertNull(getPaymentTypeService().get(entity.getId()));
    }

    @Test
    public void testDeleteAll() {
        saveNewPaymentType();
        getPaymentTypeService().deleteAll();
        assertEquals(0, getPaymentTypeService().getAll().size());
    }

    private IPaymentType saveNewPaymentType() {

        final IPaymentType parent = getPaymentTypeService().createEntity();
        parent.setParent(null);
        parent.setName("parentName-" + getRandomPrefix());
        getPaymentTypeService().save(parent);

        final IPaymentType entity = getPaymentTypeService().createEntity();
        entity.setParent(parent);
        entity.setName("childName-" + getRandomPrefix());

        getPaymentTypeService().save(entity);
        return entity;
    }

    private IPaymentType saveNewPaymentTypeWithoutParent() {

        final IPaymentType entity = getPaymentTypeService().createEntity();
        entity.setParent(null);
        entity.setName("name-" + getRandomPrefix());
        getPaymentTypeService().save(entity);
        return entity;
    }
}
