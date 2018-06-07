package com.prista.netbanking.service;

import com.prista.netbanking.dao.api.model.IAccount;
import com.prista.netbanking.dao.api.model.IBank;
import com.prista.netbanking.dao.api.model.IUserProfile;
import com.prista.netbanking.dao.api.model.enums.AccountType;
import com.prista.netbanking.dao.api.model.enums.CurrencyType;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public abstract class AbstractTest {

    @Autowired
    private IUserProfileService userProfileService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IBankService bankService;

    @Autowired
    private IPaymentTypeService paymentTypeService;

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private IBranchService branchService;

    private static final Random RANDOM = new Random();

    protected String getRandomPrefix() {
        return RANDOM.nextInt(99999) + "";
    }

    protected int getRandomInt(final int bound) {
        return RANDOM.nextInt(bound);
    }

    protected double getRandomDoubleValue(final int rangeMin, final int rangeMax) {
        final double randomValue = rangeMin + (rangeMax - rangeMin) * RANDOM.nextDouble();
        return Double.parseDouble(String.format(new Locale("en"), "%.2f", randomValue));
    }

    protected int getRandomObjectsCount() {
        return RANDOM.nextInt(9) + 1;
    }

    public IUserProfileService getUserProfileService() {
        return userProfileService;
    }

    public IAccountService getAccountService() {
        return accountService;
    }

    public IBankService getBankService() {
        return bankService;
    }

    public Random getRANDOM() {
        return RANDOM;
    }

    public IPaymentTypeService getPaymentTypeService() {
        return paymentTypeService;
    }

    public ITransactionService getTransactionService() {
        return transactionService;
    }

    public IBranchService getBranchService() {
        return branchService;
    }

    protected IUserProfile saveNewUserProfile() {

        final IUserProfile entity = getUserProfileService().createEntity();
        entity.setUsername("userProfile-" + getRandomPrefix());
        entity.setPassword("password-" + getRandomPrefix());
        entity.setRole("role-" + getRandomPrefix());
        getUserProfileService().save(entity);
        return entity;
    }

    protected IBank saveNewBank() {
        final IBank entity = getBankService().createEntity();
        entity.setName("bank-" + getRandomPrefix());
        getBankService().save(entity);
        return entity;
    }

    protected IAccount saveNewAccount() {
        final IAccount entity = getAccountService().createEntity();
        entity.setName("account-" + getRandomPrefix());
        entity.setUserProfile(saveNewUserProfile());

        final AccountType[] allTypes = AccountType.values();
        int randomIndex = Math.max(0, getRANDOM().nextInt(allTypes.length) - 1);
        entity.setAccountType(allTypes[randomIndex]);

        entity.setBalance(getRandomDoubleValue(100, 121));


        final CurrencyType[] currencyTypes = CurrencyType.values();
        randomIndex = Math.max(0, getRANDOM().nextInt(currencyTypes.length) - 1);
        entity.setCurrency(currencyTypes[randomIndex]);


        entity.setLocked(false);
        entity.setBank(saveNewBank());
        entity.setDeleted(false);

        getAccountService().save(entity);
        return entity;
    }

}
