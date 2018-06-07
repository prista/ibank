package com.prista.netbanking.dao.jdbc;

import com.prista.netbanking.dao.api.IAccountDao;
import com.prista.netbanking.dao.api.filter.AccountFilter;
import com.prista.netbanking.dao.api.model.IAccount;
import com.prista.netbanking.dao.api.model.IUserProfile;
import com.prista.netbanking.dao.api.model.enums.AccountType;
import com.prista.netbanking.dao.api.model.enums.CurrencyType;
import com.prista.netbanking.dao.jdbc.exception.SQLExecutionException;
import com.prista.netbanking.dao.jdbc.model.Bank;
import com.prista.netbanking.dao.jdbc.model.UserProfile;
import com.prista.netbanking.dao.jdbc.model.Account;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Repository
public class AccountDaoImpl extends AbstractDaoImpl<IAccount, Integer> implements IAccountDao {

    @Override
    protected IAccount parseRow(final ResultSet resultSet, final Set<String> columns) throws SQLException {
        final IAccount entity = createEntity();

        entity.setId((Integer) resultSet.getObject("id"));
        entity.setName(resultSet.getString("name"));

        final Integer userProfileId = (Integer) resultSet.getObject("user_profile_id");
        if (userProfileId != null) {
            final IUserProfile userProfile = new UserProfile();
            userProfile.setId(userProfileId);
            if (columns.contains("user_profile_username")) {
                userProfile.setUsername(resultSet.getString("user_profile_username"));
            }
            entity.setUserProfile(userProfile);
        }

        entity.setAccountType(AccountType.valueOf(resultSet.getString("account_type")));
        entity.setBalance(resultSet.getDouble("balance"));
        entity.setCurrency(CurrencyType.valueOf(resultSet.getString("currency")));
        entity.setLocked(resultSet.getBoolean("locked"));

        final Integer bankId = (Integer) resultSet.getObject("bank_id");
        if (bankId != null) {
            final Bank bank = new Bank();
            bank.setId(bankId);
            if (columns.contains("bank_name")) {
                bank.setName(resultSet.getString("bank_name"));
            }
            entity.setBank(bank);
        }

        entity.setDeleted(resultSet.getBoolean("deleted"));
        entity.setCreated(resultSet.getDate("created"));
        entity.setUpdated(resultSet.getDate("updated"));

        return entity;
    }

    @Override
    protected String getTableName() {
        return "account";
    }

    @Override
    public IAccount createEntity() {
        return new Account();
    }

    @Override
    public void update(final IAccount entity) {

        try (Connection c = getConnection();
             PreparedStatement pStmt = c.prepareStatement(
                     String.format("update %s set name=?, user_profile_id=?, account_type=?, balance=?, currency=?, locked=?, bank_id=?, deleted=?, updated=? where id=?", getTableName()))) {
            c.setAutoCommit(false);
            try {
                pStmt.setString(1, entity.getName());
                pStmt.setInt(2, entity.getUserProfile().getId());
                pStmt.setString(3, entity.getAccountType().name());
                pStmt.setDouble(4, entity.getBalance());
                pStmt.setString(5, entity.getCurrency().name());
                pStmt.setBoolean(6, entity.getLocked());
                pStmt.setInt(7, entity.getBank().getId());
                pStmt.setBoolean(8, entity.getDeleted());
                pStmt.setObject(9, entity.getUpdated(), Types.TIMESTAMP);
                pStmt.setInt(10, entity.getId());
                pStmt.executeUpdate();
                c.commit();
            } catch (final Exception e) {
                c.rollback();
                throw new RuntimeException(e);
            }

        } catch (final SQLException e) {
            throw new SQLExecutionException(e);
        }
    }

    @Override
    public void insert(final IAccount entity) {
        try (Connection c = getConnection();
             PreparedStatement pStmt = c.prepareStatement(String
                             .format("insert into %s (name, account_type, balance, currency, locked, deleted, created, updated, user_profile_id, bank_id) values(?,?,?,?,?,?,?,?,?,?)", getTableName()),
                     Statement.RETURN_GENERATED_KEYS)) {
            c.setAutoCommit(false);
            try {
                pStmt.setString(1, entity.getName());
                pStmt.setString(2, entity.getAccountType().name());
                pStmt.setDouble(3, entity.getBalance());
                pStmt.setString(4, entity.getCurrency().name());
                pStmt.setBoolean(5, entity.getLocked());
                pStmt.setBoolean(6, entity.getDeleted());
                pStmt.setObject(7, entity.getCreated(), Types.TIMESTAMP);
                pStmt.setObject(8, entity.getUpdated(), Types.TIMESTAMP);
                pStmt.setInt(9, entity.getUserProfile().getId());
                pStmt.setInt(10, entity.getBank().getId());

                pStmt.executeUpdate();

                final ResultSet rs = pStmt.getGeneratedKeys();
                rs.next();
                final int id = rs.getInt("id");

                rs.close();

                entity.setId(id);
                c.commit();
            } catch (final Exception e) {
                c.rollback();
                throw new RuntimeException(e);
            }
        } catch (final SQLException e) {
            throw new SQLExecutionException(e);
        }
    }

    @Override
    public long getCount(AccountFilter filter) {
        final StringBuilder sql = new StringBuilder("");
        appendWHEREs(sql, filter);
        return executeCountQuery(sql.toString());
    }

    @Override
    public List<IAccount> find(AccountFilter filter) {
        final StringBuilder sql;
        sql = new StringBuilder("select account.*");
        if (filter.getFetchUserProfile()) {
            sql.append(", user_profile.username as user_profile_username");
        }
        if (filter.getFetchBank()) {
            sql.append(", bank.name as bank_name");
        }
        sql.append(String.format(" from %s ", getTableName()));
        appendJOINs(sql, filter);
        appendWHEREs(sql, filter);
        appendSort(filter, sql);
        appendPaging(filter, sql);
        return executeFindQueryWithCustomSelect(sql.toString());
    }

    private void appendJOINs(StringBuilder sb, AccountFilter filter) {
        if (filter.getFetchUserProfile()) {
            sb.append(" join user_profile on (user_profile.id=account.user_profile_id) ");
        }
        if (filter.getFetchBank()) {
            sb.append(" join bank on (bank.id=account.bank_id) ");
        }
    }


    private void appendWHEREs(StringBuilder sb, AccountFilter filter) {
        final List<String> ands = new ArrayList<String>();
        if (StringUtils.isNotBlank(filter.getName())) {
            ands.add(String.format("%s.name='%s'", getTableName(), filter.getName())); // SQL
        }

        final Iterator<String> andsIter = ands.iterator();
        if (andsIter.hasNext()) {
            final String firtsCondition = andsIter.next();

            sb.append(String.format("where %s", firtsCondition));

            while (andsIter.hasNext()) {
                final String condition = andsIter.next();
                sb.append(String.format(" and %s", condition));
            }
        }
    }

    @Override
    public IAccount getFullInfo(Integer id) {
        throw new UnsupportedOperationException();
    }

}
