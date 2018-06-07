package com.prista.netbanking.service;

import com.prista.netbanking.dao.api.filter.BranchFilter;
import com.prista.netbanking.dao.api.model.IBranch;
import com.prista.netbanking.dao.api.model.IUserProfile;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class BranchServiceTest extends AbstractTest {

    @Before
    public void cleanTables() {
        getBranchService().deleteAll();
        getTransactionService().deleteAll();
        getAccountService().deleteAll();
        getBankService().deleteAll();
    }

    @Test
    public void createTest() {
        final IBranch entity = saveNewBranch();

        final IBranch entityFromDB = getBranchService().getFullInfo(entity.getId());

        assertEquals(entity.getBank().getId(), entityFromDB.getBank().getId());
        assertEquals(entity.getName(), entityFromDB.getName());
        assertEquals(entity.getStreetAddress(), entityFromDB.getStreetAddress());
        assertEquals(entity.getCity(), entityFromDB.getCity());
        assertEquals(entity.getPostCode(), entityFromDB.getPostCode());
        assertNotNull(entityFromDB.getId());
        assertNotNull(entityFromDB.getCreated());
        assertNotNull(entityFromDB.getUpdated());
        assertEquals(entity.getUpdated(), entityFromDB.getUpdated());
    }

    @Test
    public void createBranchWithUserProfilesTest() {
        final IBranch entity = getBranchService().createEntity();

        entity.setBank(saveNewBank());
        entity.setName("branch-" + getRandomPrefix());
        entity.setStreetAddress("street-" + getRandomPrefix());
        entity.setCity("city-" + getRandomPrefix());
        entity.setPostCode(getRandomInt(99999));


        final int randomObjectsCount = getRandomObjectsCount();
        final List<IUserProfile> userProfiles = new ArrayList<>();
        for (int i = 0; i < randomObjectsCount; i++) {
            userProfiles.add(saveNewUserProfile());
        }
        entity.getUserProfiles().addAll(userProfiles);

        getBranchService().save(entity);

        final IBranch entityFromDB = getBranchService().getFullInfo(entity.getId());
        final Set<IUserProfile> userProfilesFromDB = entityFromDB.getUserProfiles();
        assertEquals(userProfiles.size(), userProfilesFromDB.size());

        // check that correct (by id) engines were returned
        for (final IUserProfile userProfile : userProfiles) {
            boolean found = false;
            for (final IUserProfile dbUserProfile : userProfilesFromDB) {
                if (userProfile.getId().equals(dbUserProfile.getId())) {
                    found = true;
                    break;
                }

            }
            assertTrue("can't find entity:" + userProfile, found);
        }
    }

    @Test
    public void simpleFindTest() { // just checks the query syntax and makes
        // basic assertions
        for (int i = 0; i < 10; i++) {
            saveNewBranch();
        }

        final BranchFilter branchFilter = new BranchFilter();
        branchFilter.setLimit(3);
        branchFilter.setSortColumn("name");
        branchFilter.setSortOrder(Boolean.FALSE);
        branchFilter.setOffset(3);
        branchFilter.setFetchBank(true);
        final List<IBranch> findResultList = getBranchService().find(branchFilter);
        assertEquals(findResultList.size(), 3);
        for (IBranch branch : findResultList) {
            assertNotNull(branch.getBank().getName());
        }
    }

    @Test
    public void searchTest() {
        for (int i = 0; i < 2; i++) {
            saveNewBranch();
        }
        IBranch branch = saveNewBranch();
        String branchName = branch.getName();

        final BranchFilter branchFilter = new BranchFilter();
        branchFilter.setLimit(3);
        branchFilter.setName(branchName);
        branchFilter.setFetchBank(true);
        List<IBranch> findResultList = getBranchService().find(branchFilter);
        assertEquals(findResultList.size(), 1);
        assertEquals(findResultList.get(0).getName(), branchName);
        assertNotNull(findResultList.get(0).getBank().getName());

        String bankName = branch.getBank().getName();
        branchFilter.setBankName(bankName);
        findResultList = getBranchService().find(branchFilter);
        assertEquals(findResultList.size(), 1);
        assertEquals(findResultList.get(0).getName(), branchName);
        assertEquals(findResultList.get(0).getBank().getName(), bankName);
    }

    @Test
    public void testUpdate() {
        final IBranch entity = saveNewBranch();

        entity.setName("new-name");
        getBranchService().save(entity);

        final IBranch entityFromDB = getBranchService().getFullInfo(entity.getId());


        assertEquals(entity.getName(), entityFromDB.getName());
        assertEquals(entity.getBank().getId(), entityFromDB.getBank().getId());
        assertEquals(entity.getStreetAddress(), entityFromDB.getStreetAddress());
        assertEquals(entity.getCity(), entityFromDB.getCity());
        assertEquals(entity.getPostCode(), entityFromDB.getPostCode());
        assertNotNull(entityFromDB.getId());
        assertNotNull(entityFromDB.getCreated());
        assertNotNull(entityFromDB.getUpdated());
        assertTrue(entity.getCreated().before(entity.getUpdated()));
    }

    @Test
    public void testGetAll() {
        final int initialCount = getBranchService().getAll().size();

        final int randomObjectsCount = getRandomObjectsCount();
        for (int i = 0; i < randomObjectsCount; i++) {
            saveNewBranch();
        }

        final List<IBranch> allEntities = getBranchService().getAll();

        for (final IBranch entityFromDB : allEntities) {
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
        final IBranch entity = saveNewBranch();
        getBranchService().delete(entity.getId());
        assertNull(getBranchService().get(entity.getId()));
    }

    @Test
    public void testDeleteAll() {
        saveNewBranch();
        getBranchService().deleteAll();
        assertEquals(0, getBranchService().getAll().size());
    }

    private IBranch saveNewBranch() {
        final IBranch entity = getBranchService().createEntity();

        entity.setBank(saveNewBank());
        entity.setName("branch-" + getRandomPrefix());
        entity.setStreetAddress("street-" + getRandomPrefix());
        entity.setCity("city-" + getRandomPrefix());
        entity.setPostCode(getRandomInt(99999));

        getBranchService().save(entity);
        return entity;
    }

}
