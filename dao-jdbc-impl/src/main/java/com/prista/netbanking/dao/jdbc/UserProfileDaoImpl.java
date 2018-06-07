package com.prista.netbanking.dao.jdbc;

import com.prista.netbanking.dao.api.IUserProfileDao;
import com.prista.netbanking.dao.api.filter.UserProfileFilter;
import com.prista.netbanking.dao.api.model.IUserProfile;
import com.prista.netbanking.dao.jdbc.exception.SQLExecutionException;
import com.prista.netbanking.dao.jdbc.model.UserProfile;
import com.prista.netbanking.dao.jdbc.utils.StatemenAction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class UserProfileDaoImpl extends AbstractDaoImpl<IUserProfile, Integer> implements IUserProfileDao {
    @Override
    protected IUserProfile parseRow(final ResultSet resultSet) throws SQLException {
        final IUserProfile entity = createEntity();
        entity.setId((Integer) resultSet.getObject("id"));
        entity.setUsername(resultSet.getString("username"));
        entity.setPassword(resultSet.getString("password"));
        entity.setRole(resultSet.getString("role"));
        entity.setCreated(resultSet.getTimestamp("created"));
        entity.setUpdated(resultSet.getTimestamp("updated"));
        return entity;
    }

    @Override
    protected String getTableName() {
        return "user_profile";
    }

    @Override
    public IUserProfile createEntity() {
        return new UserProfile();
    }

    @Override
    public void update(final IUserProfile entity) {

        try (Connection c = getConnection();
             PreparedStatement pStmt = c.prepareStatement(
                     String.format("update %s set username=?, password=?, role=?, updated=? where id=?", getTableName()))) {
            c.setAutoCommit(false);
            try {
                pStmt.setString(1, entity.getUsername());
                pStmt.setString(2, entity.getPassword());
                pStmt.setString(3, entity.getRole());
                pStmt.setObject(4, entity.getUpdated(), Types.TIMESTAMP);
                pStmt.setInt(5, entity.getId());
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
    public void insert(final IUserProfile entity) {
        try (Connection c = getConnection();
             PreparedStatement pStmt = c.prepareStatement(String
                             .format("insert into %s (username, password, role, created, updated) values(?,?,?,?,?)", getTableName()),
                     Statement.RETURN_GENERATED_KEYS)) {
            c.setAutoCommit(false);
            try {
                pStmt.setString(1, entity.getUsername());
                pStmt.setString(2, entity.getPassword());
                pStmt.setString(3, entity.getRole());
                pStmt.setObject(4, entity.getCreated(), Types.TIMESTAMP);
                pStmt.setObject(5, entity.getUpdated(), Types.TIMESTAMP);


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
    public long getCount(UserProfileFilter filter) {
        final StringBuilder sqlPart = new StringBuilder("");
        appendWHEREs(sqlPart, filter);
        return executeCountQuery("");
    }

    @Override
    public List<IUserProfile> find(UserProfileFilter filter) {
        final StringBuilder sqlPart = new StringBuilder("");
        appendWHEREs(sqlPart, filter);
        appendSort(filter, sqlPart);
        appendPaging(filter, sqlPart);
        return executeFindQuery(sqlPart.toString());
    }

    @Override
    public Set<IUserProfile> getByBranch(final Integer id) {
        return executeStatement(new StatemenAction<Set<IUserProfile>>() {
            @Override
            public Set<IUserProfile> doWithStatement(final Statement statement) throws SQLException {
                // @formatter:off
                statement.executeQuery(
                        "select * from user_profile u "
                                + "inner join user_profile_2_branch m2e on u.id=m2e.user_profile_id "
                                + "where m2e.branch_id=" + id);
                // @formatter:on
                final ResultSet resultSet = statement.getResultSet();

                final Set<IUserProfile> result = new HashSet<IUserProfile>();
                boolean hasNext = resultSet.next();
                while (hasNext) {
                    result.add(parseRow(resultSet));
                    hasNext = resultSet.next();
                }
                resultSet.close();

                return result;
            }
        });
    }

    private void appendWHEREs(StringBuilder sb, UserProfileFilter filter) {
        final List<String> ands = new ArrayList<String>();
        if (StringUtils.isNotBlank(filter.getUserName())) {
            ands.add(String.format("username='%s'", filter.getUserName())); // SQL
        }
        if (StringUtils.isNotBlank(filter.getRole())) {
            ands.add(String.format("role='%s'", filter.getRole())); // SQL
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
}
