package com.prista.netbanking.dao.jdbc;

import com.prista.netbanking.dao.api.IBranchDao;
import com.prista.netbanking.dao.api.IUserProfileDao;
import com.prista.netbanking.dao.api.filter.BranchFilter;
import com.prista.netbanking.dao.api.model.IBranch;
import com.prista.netbanking.dao.api.model.IUserProfile;
import com.prista.netbanking.dao.jdbc.exception.SQLExecutionException;
import com.prista.netbanking.dao.jdbc.model.Bank;
import com.prista.netbanking.dao.jdbc.model.Branch;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Repository
public class BranchDaoImpl extends AbstractDaoImpl<IBranch, Integer> implements IBranchDao {

    @Autowired
    private IUserProfileDao userProfileDao;

    @Override
    protected IBranch parseRow(final ResultSet resultSet, final Set<String> columns) throws SQLException {
        final IBranch entity = createEntity();

        entity.setId((Integer) resultSet.getObject("id"));
        entity.setName(resultSet.getString("name"));
        entity.setStreetAddress(resultSet.getString("street_address"));
        entity.setCity(resultSet.getString("city"));
        entity.setPostCode((Integer) resultSet.getObject("post_code"));
        entity.setCreated(resultSet.getTimestamp("created"));
        entity.setUpdated(resultSet.getTimestamp("updated"));

        final Integer bankId = (Integer) resultSet.getObject("bank_id");
        if (bankId != null) {
            final Bank bank = new Bank();
            bank.setId(bankId);
            if (columns.contains("bank_name")) {
                bank.setName(resultSet.getString("bank_name"));
            }
            entity.setBank(bank);
        }
        return entity;
    }

    @Override
    protected String getTableName() {
        return "branch";
    }

    @Override
    public IBranch createEntity() {
        return new Branch();
    }

    @Override
    public void update(final IBranch entity) {
        try (Connection c = getConnection();
             PreparedStatement pStmt = c.prepareStatement(
                     String.format("update %s set bank_id=?, name=?, street_address=?, city=?, post_code=?, updated=? where id=?", getTableName()))) {
            c.setAutoCommit(false);
            try {
                pStmt.setInt(1, entity.getBank().getId());
                pStmt.setString(2, entity.getName());
                pStmt.setString(3, entity.getStreetAddress());
                pStmt.setString(4, entity.getCity());
                pStmt.setInt(5, entity.getPostCode());
                pStmt.setObject(6, entity.getUpdated(), Types.TIMESTAMP);
                pStmt.setInt(7, entity.getId());
                pStmt.executeUpdate();

                updateUserProfiles(entity, c);

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
    public void insert(final IBranch entity) {
        try (Connection c = getConnection();
             PreparedStatement pStmt = c.prepareStatement(String
                             .format("insert into %s (bank_id, name, street_address, city, post_code, created, updated) values(?,?,?,?,?,?,?)", getTableName()),
                     Statement.RETURN_GENERATED_KEYS)) {
            c.setAutoCommit(false);
            try {
                pStmt.setInt(1, entity.getBank().getId());
                pStmt.setString(2, entity.getName());
                pStmt.setString(3, entity.getStreetAddress());
                pStmt.setString(4, entity.getCity());
                pStmt.setInt(5, entity.getPostCode());
                pStmt.setObject(6, entity.getCreated(), Types.TIMESTAMP);
                pStmt.setObject(7, entity.getUpdated(), Types.TIMESTAMP);


                pStmt.executeUpdate();

                final ResultSet rs = pStmt.getGeneratedKeys();
                rs.next();
                final int id = rs.getInt("id");

                rs.close();

                entity.setId(id);
                // the same should be done in 'update' DAO method
                updateUserProfiles(entity, c);

                c.commit();
            } catch (final Exception e) {
                c.rollback();
                throw new RuntimeException(e);
            }
        } catch (final SQLException e) {
            throw new SQLExecutionException(e);
        }
    }

    private void updateUserProfiles(final IBranch entity, final Connection c) throws SQLException {
        // clear all existing records related to the current model
        final PreparedStatement deleteStmt = c.prepareStatement("DELETE FROM user_profile_2_branch WHERE branch_id=?");
        deleteStmt.setInt(1, entity.getId());
        deleteStmt.executeUpdate();
        deleteStmt.close();

        if (entity.getUserProfiles().isEmpty()) {
            return;
        }

        // insert actual list of records using 'batch'
        final PreparedStatement pStmt = c
                .prepareStatement("INSERT INTO user_profile_2_branch (branch_id, user_profile_id) VALUES(?,?)");

        for (final IUserProfile e : entity.getUserProfiles()) {
            pStmt.setInt(1, entity.getId());
            pStmt.setInt(2, e.getId());
            pStmt.addBatch();
        }
        pStmt.executeBatch();
        pStmt.close();
    }

    @Override
    public long getCount(BranchFilter filter) {
        final StringBuilder sql = new StringBuilder("");
        appendWHEREs(sql, filter);
        return executeCountQuery(sql.toString());
    }

    @Override
    public void deleteAll() {
        try (Connection c = getConnection();
             PreparedStatement deleteUserProfileRefsStmt = c.prepareStatement("DELETE FROM user_profile_2_branch");
             PreparedStatement deleteAllStmt = c.prepareStatement("DELETE FROM branch");) {
            c.setAutoCommit(false);
            try {
                deleteUserProfileRefsStmt.executeUpdate();
                deleteAllStmt.executeUpdate();

                deleteUserProfileRefsStmt.close();
                deleteAllStmt.close();

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
    public IBranch getFullInfo(final Integer id) {
        final IBranch branch = get(id);
        final Set<IUserProfile> userProfiles = userProfileDao.getByBranch(id);
        branch.setUserProfiles(userProfiles);

        return branch;
    }

    @Override
    public List<IBranch> find(BranchFilter filter) {

        final StringBuilder sql;
        if (filter.getFetchBank()) {
            sql = new StringBuilder(String.format("select branch.*, bank.name as bank_name from %s ", getTableName()));
        } else {
            sql = new StringBuilder(String.format("select branch.* from %s ", getTableName()));
        }
        appendJOINs(sql, filter);
        appendWHEREs(sql, filter);
        appendSort(filter, sql);
        appendPaging(filter, sql);
        return executeFindQueryWithCustomSelect(sql.toString());
    }

    private void appendJOINs(final StringBuilder sb, final BranchFilter filter) {
        if (filter.getFetchBank()) {
            sb.append(" join bank on (bank.id=branch.bank_id) ");
        }
    }

    private void appendWHEREs(final StringBuilder sb, final BranchFilter filter) {
        final List<String> ands = new ArrayList<String>();
        if (StringUtils.isNotBlank(filter.getName())) {
            ands.add(String.format("%s.name='%s'", getTableName(), filter.getName())); // SQL
            // injections???
        }
        if (StringUtils.isNotBlank(filter.getBankName())) {
            ands.add(String.format("%s.name='%s'", "bank", filter.getBankName())); // SQL
            // injections???
        }
/*        if (filter.getSold() != null) {
            ands.add(String.format("sold='%s'", filter.getSold()));
        }*/

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

}
