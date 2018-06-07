package com.prista.netbanking.dao.jdbc;

import com.prista.netbanking.dao.api.IPaymentTypeDao;
import com.prista.netbanking.dao.api.filter.PaymentTypeFilter;
import com.prista.netbanking.dao.api.model.IPaymentType;
import com.prista.netbanking.dao.jdbc.exception.SQLExecutionException;
import com.prista.netbanking.dao.jdbc.model.PaymentType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Repository
public class PaymentTypeDaoImpl extends AbstractDaoImpl<IPaymentType, Integer> implements IPaymentTypeDao {

    @Override
    protected IPaymentType parseRow(final ResultSet resultSet, final Set<String> columns) throws SQLException {
        final IPaymentType entity = createEntity();
        entity.setId((Integer) resultSet.getObject("id"));
        entity.setName(resultSet.getString("name"));
        entity.setCreated(resultSet.getTimestamp("created"));
        entity.setUpdated(resultSet.getTimestamp("updated"));

        if (resultSet.getObject("parent_id") == null) {
            entity.setParent(null);
        } else {
            final IPaymentType parent = new PaymentType();
            parent.setId((Integer) resultSet.getObject("parent_id"));
            if (columns.contains("p2_name")) {
                parent.setName(resultSet.getString("p2_name"));
                entity.setParent(parent);
            }
        }

        return entity;
    }

    @Override
    protected String getTableName() {
        return "payment_type";
    }

    @Override
    public IPaymentType createEntity() {
        return new PaymentType();
    }

    @Override
    public void update(final IPaymentType entity) {
        try (Connection c = getConnection();
             PreparedStatement pStmt = c.prepareStatement(
                     String.format("update %s set parent_id=?, name=?, updated=? where id=?", getTableName()))) {
            c.setAutoCommit(false);
            try {
                if (entity.getParent() == null) {
                    pStmt.setNull(1, Types.INTEGER);
                } else {
                    pStmt.setInt(1, entity.getParent().getId());
                }
                pStmt.setString(2, entity.getName());
                pStmt.setObject(3, entity.getUpdated(), Types.TIMESTAMP);
                pStmt.setInt(4, entity.getId());
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
    public void insert(final IPaymentType entity) {
        try (Connection c = getConnection();
             PreparedStatement pStmt = c.prepareStatement(String
                             .format("insert into %s (parent_id, name, created, updated) values(?,?,?,?)", getTableName()),
                     Statement.RETURN_GENERATED_KEYS)) {
            c.setAutoCommit(false);
            try {
                if (entity.getParent() == null) { //why does fucking PaymentType-entity exist by default?
                    pStmt.setNull(1, Types.INTEGER);
                } else {
                    if (entity.getParent().getId() == null) {
                        pStmt.setNull(1, Types.INTEGER);
                    } else {
                        pStmt.setInt(1, entity.getParent().getId());
                    }

                }
                pStmt.setString(2, entity.getName());
                pStmt.setObject(3, entity.getCreated(), Types.TIMESTAMP);
                pStmt.setObject(4, entity.getUpdated(), Types.TIMESTAMP);

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
    public IPaymentType get(Integer id) {
        try (Connection c = getConnection();
             Statement statement = c.createStatement()) {
            try {

                final StringBuilder sqlPart = new StringBuilder("");
                sqlPart.append(String.format("select payment_type.*, p2.name as p2_name from %s", getTableName()));
                appendJOINs(sqlPart);
                sqlPart.append(String.format(" where payment_type.id=%s", id));
                statement.executeQuery(sqlPart.toString());

                final ResultSet resultSet = statement.getResultSet();
                final Set<String> columns = resolveColumnNames(resultSet);

                final boolean hasNext = resultSet.next();
                IPaymentType result = null;
                if (hasNext) {
                    result = parseRow(resultSet, columns);
                }

                resultSet.close();
                return result;
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        } catch (final SQLException e) {
            throw new SQLExecutionException(e);
        }
    }

    @Override
    public long getCount(PaymentTypeFilter filter) {
        final StringBuilder sqlPart = new StringBuilder("");
        appendWHEREs(sqlPart, filter);
        return executeCountQuery(sqlPart.toString());
    }

    @Override
    public List<IPaymentType> find(PaymentTypeFilter filter) {
        final StringBuilder sqlPart = new StringBuilder("");
        sqlPart.append(String.format("select payment_type.*, p2.name as p2_name from %s", getTableName()));
        appendJOINs(sqlPart);
        appendWHEREs(sqlPart, filter);
        appendSort(filter, sqlPart);
        appendPaging(filter, sqlPart);
        return executeFindQueryWithCustomSelect(sqlPart.toString());
    }

    private void appendJOINs(StringBuilder sqlPart) {
        sqlPart.append(" left join payment_type p2 on (p2.id=payment_type.parent_id) ");
    }

    private void appendWHEREs(StringBuilder sb, PaymentTypeFilter filter) {
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
    public IPaymentType getFullInfo(Integer id) {
        throw new UnsupportedOperationException();
    }

}
