package com.prista.netbanking.service;

import com.prista.netbanking.dao.api.filter.UserProfileFilter;
import com.prista.netbanking.dao.api.model.IUserProfile;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;


public class UserProfileServiceTest extends AbstractTest {

    @Before
    public void cleanTables() {
        getTransactionService().deleteAll();
        getAccountService().deleteAll();
        getUserProfileService().deleteAll();
    }

    @Test
    public void testCreate() {
        final IUserProfile entity = saveNewUserProfile();

        final IUserProfile entityFromDB = getUserProfileService().get(entity.getId());

        assertEquals(entity.getUsername(), entityFromDB.getUsername());
        assertEquals(entity.getPassword(), entityFromDB.getPassword());
        assertEquals(entity.getRole(), entityFromDB.getRole());
        assertNotNull(entityFromDB.getId());
        assertNotNull(entityFromDB.getCreated());
        assertNotNull(entityFromDB.getUpdated());
        assertEquals(entity.getUpdated(), entityFromDB.getUpdated());
    }

    @Test
    public void simpleFindTest() { // just checks the query syntax and makes
        // basic assertions
        for (int i = 0; i < 10; i++) {
            saveNewUserProfile();
        }

        final UserProfileFilter userProfileFilter = new UserProfileFilter();
        userProfileFilter.setLimit(3);
        userProfileFilter.setSortColumn("username");
        userProfileFilter.setSortOrder(Boolean.FALSE);
        final List<IUserProfile> findResultList = getUserProfileService().find(userProfileFilter);
        assertEquals(findResultList.size(), 3);
    }

    @Test
    public void searchTest() {
        for (int i = 0; i < 2; i++) {
            saveNewUserProfile();
        }
        IUserProfile user = saveNewUserProfile();
        String userName = user.getUsername();
        String role = user.getRole();

        final UserProfileFilter userProfileFilter = new UserProfileFilter();
        userProfileFilter.setLimit(3);
        userProfileFilter.setUserName(userName);
        List<IUserProfile> findResultList = getUserProfileService().find(userProfileFilter);
        assertEquals(findResultList.size(), 1);
        assertEquals(findResultList.get(0).getUsername(), userName);

        userProfileFilter.setRole(role);
        findResultList = getUserProfileService().find(userProfileFilter);
        assertEquals(findResultList.size(), 1);
        assertEquals(findResultList.get(0).getUsername(), userName);
        assertEquals(findResultList.get(0).getRole(), role);
    }

    @Test
    public void testUpdate() {
        final IUserProfile entity = saveNewUserProfile();

        entity.setUsername("new-username");
        entity.setPassword("new-password");
        entity.setRole("new-role");
        getUserProfileService().save(entity);

        final IUserProfile entityFromDB = getUserProfileService().get(entity.getId());

        assertEquals(entity.getUsername(), entityFromDB.getUsername());
        assertEquals(entity.getPassword(), entityFromDB.getPassword());
        assertEquals(entity.getRole(), entityFromDB.getRole());
        assertEquals(entity.getUpdated(), entityFromDB.getUpdated());
        assertNotNull(entityFromDB.getId());
        assertNotNull(entityFromDB.getCreated());
        assertNotNull(entityFromDB.getUpdated());
        assertTrue(entity.getCreated().before(entity.getUpdated()));
    }

    @Test
    public void testGetAll() {
        final int initialCount = getUserProfileService().getAll().size();

        final int randomObjectsCount = getRandomObjectsCount();
        for (int i = 0; i < randomObjectsCount; i++) {
            saveNewUserProfile();
        }

        final List<IUserProfile> allEntities = getUserProfileService().getAll();

        for (final IUserProfile entityFromDB : allEntities) {
            assertNotNull(entityFromDB.getId());
            assertNotNull(entityFromDB.getUsername());
            assertNotNull(entityFromDB.getPassword());
            assertNotNull(entityFromDB.getRole());
            assertNotNull(entityFromDB.getCreated());
            assertNotNull(entityFromDB.getUpdated());
        }

        assertEquals(randomObjectsCount + initialCount, allEntities.size());
    }

    @Test
    public void testDelete() {
        final IUserProfile entity = saveNewUserProfile();
        getUserProfileService().delete(entity.getId());
        assertNull(getUserProfileService().get(entity.getId()));
    }

    @Test
    public void testDeleteAll() {
        saveNewUserProfile();
        getUserProfileService().deleteAll();
        assertEquals(0, getUserProfileService().getAll().size());
    }
}
