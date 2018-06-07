package com.prista.netbanking.dao.jdbc;

import com.prista.netbanking.dao.api.BaseDao;
import com.prista.netbanking.dao.api.filter.AbstractFilter;
import com.prista.netbanking.dao.jdbc.exception.SQLExecutionException;
import com.prista.netbanking.dao.jdbc.utils.PreparedStatemenAction;
import com.prista.netbanking.dao.jdbc.utils.StatemenAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractDaoImpl<ENTITY, ID> implements BaseDao<ENTITY, ID> {

    private static final Logger LOGGER = LoggerFactory.getLogger("abstractDAOLogger");

    @Value("${db.url}")
    private String url;

    @Value("${db.user}")
    private String user;

    @Value("${db.password}")
    private String password;

    @PostConstruct
    private void init() throws IllegalAccessException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        if (url == null) {
            throw new IllegalAccessException("[url] cant be null");
        }

        if (password == null) {
            throw new IllegalAccessException("[password] cant be null");
        }

        if (user == null) {
            throw new IllegalAccessException("[user] cant be null");
        }
    }

    @Override
    public ENTITY get(final ID id) {
        return executeStatement((statement) -> {
            statement.executeQuery("select * from " + getTableName() + " where id=" + id);

            final ResultSet resultSet = statement.getResultSet();
            final Set<String> columns = resolveColumnNames(resultSet);

            final boolean hasNext = resultSet.next();
            ENTITY result = null;
            if (hasNext) {
                result = parseRow(resultSet, columns);
            }

            resultSet.close();
            return result;
        });
    }

    @Override
    public List<ENTITY> selectAll() {
        return executeStatement(new StatemenAction<List<ENTITY>>() {
            @Override
            public List<ENTITY> doWithStatement(final Statement statement) throws SQLException {
                statement.executeQuery("select * from " + getTableName());

                final ResultSet resultSet = statement.getResultSet();
                final Set<String> columns = resolveColumnNames(resultSet);
                final List<ENTITY> result = new ArrayList<>();
                boolean hasNext = resultSet.next();
                while (hasNext) {
                    result.add(parseRow(resultSet, columns));
                    hasNext = resultSet.next();
                }
                resultSet.close();
                return result;
            }
        });
    }

    @Override
    public void delete(final ID id) {
        executeStatement(
                new PreparedStatemenAction<Integer>(String.format("delete from %s where id=?", getTableName())) {
                    @Override
                    public Integer doWithPreparedStatement(final PreparedStatement prepareStatement)
                            throws SQLException {
                        prepareStatement.setObject(1, id);
                        return prepareStatement.executeUpdate();
                    }
                });
    }

    @Override
    public void deleteAll() {
        executeStatement(new PreparedStatemenAction<Integer>("delete from " + getTableName()) {
            @Override
            public Integer doWithPreparedStatement(final PreparedStatement prepareStatement) throws SQLException {
                final int executeUpdate = prepareStatement.executeUpdate();
                return executeUpdate;
            }
        });
    }

    protected <T> T executeStatement(final StatemenAction<T> action) {
        try (Connection c = getConnection(); Statement stmt = c.createStatement()) {
            c.setAutoCommit(false);
            return action.doWithStatement(stmt);

        } catch (final SQLException e) {
            throw new SQLExecutionException(e); // wrap catchable exception with
            // runtime
        }
    }

    protected <T> T executeStatement(final PreparedStatemenAction<T> action) {
        try (Connection c = getConnection();
                PreparedStatement pStmt = action.isReturnGeneratedKeys()
                        ? c.prepareStatement(action.getSql(), Statement.RETURN_GENERATED_KEYS)
                        : c.prepareStatement(action.getSql())) {
            c.setAutoCommit(false);
            try {
                final T doWithPreparedStatement = action.doWithPreparedStatement(pStmt);
                c.commit();
                return doWithPreparedStatement;
            } catch (final Exception e) {
                c.rollback();
                throw new RuntimeException(e);
            }

        } catch (final SQLException e) {
            throw new SQLExecutionException(e);
        }
    }

    protected void appendPaging(final AbstractFilter filter, final StringBuilder sql) {
        final Integer limit = filter.getLimit();
        final Integer offset = filter.getOffset();

        if (limit != null) {
            sql.append(" limit " + limit);
        }

        if (offset != null) {
            sql.append(" offset " + offset);
        }
    }

    protected void appendSort(final AbstractFilter filter, final StringBuilder sql) {
        final String sortColumn = filter.getSortColumn();
        final Boolean sortOrder = filter.getSortOrder();

        if (sortColumn != null) {
            sql.append(String.format(" order by %s.%s", getTableName(), sortColumn));

            if (sortOrder != null) {
                sql.append(" ");
                sql.append(sortOrder ? "asc" : "desc");
            }
        }
    }

    protected List<ENTITY> executeFindQuery(final String sqlFragment) {
        return executeFindQueryWithCustomSelect(String.format("select * from %s %s", getTableName(), sqlFragment));
    }

    protected List<ENTITY> executeFindQueryWithCustomSelect(final String fullSql) {
        return executeStatement(new StatemenAction<List<ENTITY>>() {
            @Override
            public List<ENTITY> doWithStatement(final Statement statement) throws SQLException {
                LOGGER.debug("execute select:" + fullSql);
                statement.executeQuery(fullSql);

                final ResultSet resultSet = statement.getResultSet();

                final Set<String> columns = resolveColumnNames(resultSet);

                final List<ENTITY> result = new ArrayList<>();
                boolean hasNext = resultSet.next();
                while (hasNext) {
                    result.add(parseRow(resultSet, columns));
                    hasNext = resultSet.next();
                }
                resultSet.close();
                return result;
            }

        });
    }

    protected long executeCountQuery(final String sql) {
        return executeStatement(new StatemenAction<Long>() {
            @Override
            public Long doWithStatement(final Statement statement) throws SQLException {
                final String fullSql = String.format("select count(*) as total from %s %s", getTableName(), sql);
                LOGGER.debug("execute count:" + fullSql);

                statement.executeQuery(fullSql);

                final ResultSet resultSet = statement.getResultSet();
                resultSet.next();

                final long total = resultSet.getLong("total");
                resultSet.close();
                return total;
            }
        });
    }

    protected Set<String> resolveColumnNames(final ResultSet resultSet) throws SQLException {
        final ResultSetMetaData rsMetaData = resultSet.getMetaData();
        final int numberOfColumns = rsMetaData.getColumnCount();
        final Set<String> columns = new HashSet<>();
        for (int i = 1; i <= numberOfColumns; i++) {
            columns.add(rsMetaData.getColumnName(i));
        }
        return columns;
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    protected ENTITY parseRow(final ResultSet resultSet) throws SQLException {
        throw new UnsupportedOperationException(
                "this method should be overriden in particular *Impl class or use alternative "
                        + "com.prista.netbanking.dao.jdbc.AbstractDaoImpl.parseRow(ResultSet, List<String>)");
    }

    ;

    protected ENTITY parseRow(final ResultSet resultSet, final Set<String> columns) throws SQLException {
        // this method allows to specify in particular DAO the parser which
        // accepts list of columns. but by default it will fall back to
        // com.prista.netbanking.dao.jdbc.AbstractDaoImpl.parseRow(ResultSet)
        return parseRow(resultSet);
    }

    ;

    protected abstract String getTableName();
}
