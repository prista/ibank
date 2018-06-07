package com.prista.netbanking.dao.jdbc;

import com.prista.netbanking.dao.api.ITransactionDao;
import com.prista.netbanking.dao.api.filter.TransactionFilter;
import com.prista.netbanking.dao.api.model.IPaymentType;
import com.prista.netbanking.dao.api.model.ITransaction;
import com.prista.netbanking.dao.api.model.enums.TransactionType;
import com.prista.netbanking.dao.jdbc.model.PaymentType;
import com.prista.netbanking.dao.jdbc.exception.SQLExecutionException;
import com.prista.netbanking.dao.jdbc.model.Account;
import com.prista.netbanking.dao.jdbc.model.Transaction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Repository
public class TransactionDaoImpl extends AbstractDaoImpl<ITransaction, Integer> implements ITransactionDao {
    @Override
    protected ITransaction parseRow(final ResultSet resultSet, final Set<String> columns) throws SQLException {
        final ITransaction entity = createEntity();
        entity.setId((Integer) resultSet.getObject("id"));
        entity.setAmount(resultSet.getDouble("amount"));
        entity.setNote(resultSet.getString("note"));
        entity.setCreated(resultSet.getTimestamp("created"));
        entity.setUpdated(resultSet.getTimestamp("updated"));

        final Integer fromId = (Integer) resultSet.getObject("from_account_id");
        if (fromId != null) {
            final Account fromAccount = new Account();
            fromAccount.setId(fromId);
            if (columns.contains("a1_name")) {
                fromAccount.setName(resultSet.getString("a1_name"));
            }
            entity.setFromAccount(fromAccount);
        }

        final Integer toId = (Integer) resultSet.getObject("to_account_id");
        if (toId != null) {
            final Account toAccount = new Account();
            toAccount.setId(toId);
            if (columns.contains("a2_name")) {
                toAccount.setName(resultSet.getString("a2_name"));
            }
            entity.setToAccount(toAccount);
        }

        final Integer paymentTypeId = (Integer) resultSet.getObject("payment_type_id");
        if (paymentTypeId != null) {
            final PaymentType paymentType = new PaymentType();
            paymentType.setId(paymentTypeId);
            if (columns.contains("p_name")) {
                paymentType.setName(resultSet.getString("p_name"));
            }
            entity.setPaymentType(paymentType);
        }

        final IPaymentType paymentType = new PaymentType();
        paymentType.setId((Integer) resultSet.getObject("payment_type_id"));
        entity.setPaymentType(paymentType);
        entity.setTransactionType(TransactionType.valueOf(resultSet.getString("transaction_type")));

        return entity;
    }

    @Override
    protected String getTableName() {
        return "transaction";
    }

    @Override
    public ITransaction createEntity() {
        return new Transaction();
    }

    @Override
    public void update(final ITransaction entity) {
        try (Connection c = getConnection();
             PreparedStatement pStmt = c.prepareStatement(
                     String.format("update %s set from_account_id=?, to_account_id=?, amount=?, note=?, payment_type_id=?, transaction_type=?, updated=? where id=?", getTableName()))) {
            c.setAutoCommit(false);
            try {
                pStmt.setInt(1, entity.getFromAccount().getId());
                pStmt.setInt(2, entity.getToAccount().getId());
                pStmt.setDouble(3, entity.getAmount());
                pStmt.setString(4, entity.getNote());
                pStmt.setInt(5, entity.getPaymentType().getId());
                pStmt.setString(6, entity.getTransactionType().name());
                pStmt.setObject(7, entity.getUpdated(), Types.TIMESTAMP);
                pStmt.setInt(8, entity.getId());
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
    public void insert(final ITransaction entity) {
        try (Connection c = getConnection();
             PreparedStatement pStmt = c.prepareStatement(String
                             .format("insert into %s (from_account_id, to_account_id, amount, note, transaction_type, payment_type_id, created, updated) values(?,?,?,?,?,?,?,?)", getTableName()),
                     Statement.RETURN_GENERATED_KEYS)) {
            c.setAutoCommit(false);
            try {
                pStmt.setInt(1, entity.getFromAccount().getId());
                pStmt.setInt(2, entity.getToAccount().getId());
                pStmt.setDouble(3, entity.getAmount());
                pStmt.setString(4, entity.getNote());
                pStmt.setString(5, entity.getTransactionType().name());

                if (entity.getPaymentType() == null) {
                    pStmt.setNull(6, Types.INTEGER);
                } else {
                    pStmt.setInt(6, entity.getPaymentType().getId());
                }

                pStmt.setObject(7, entity.getCreated(), Types.TIMESTAMP);
                pStmt.setObject(8, entity.getUpdated(), Types.TIMESTAMP);

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
    public long getCount(TransactionFilter filter) {
        final StringBuilder sql = new StringBuilder("");
        appendWHEREs(sql, filter);
        return executeCountQuery(sql.toString());
    }

    @Override
    public List<ITransaction> find(TransactionFilter filter) {
        final StringBuilder sql;
        sql = new StringBuilder("select transaction.*");
        if (filter.getFetchAccount()) {
            sql.append(", a1.name as a1_name, a2.name as a2_name");
        }
        if (filter.getFetchPaymentType() != null && filter.getFetchPaymentType()) {
            sql.append(", p.name as p_name");
        }
        sql.append(String.format(" from %s ", getTableName()));
        appendJOINs(sql, filter);
        appendWHEREs(sql, filter);
        appendSort(filter, sql);
        appendPaging(filter, sql);
        return executeFindQueryWithCustomSelect(sql.toString());
    }

    @Override
    public ITransaction getFullInfo(Integer id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ITransaction getNewestTransaction() {
        final List<ITransaction> executeFindQuery = executeFindQuery(" order by created desc limit 1");
        return executeFindQuery.size() > 0 ? executeFindQuery.get(0) : null;
    }

    private void appendJOINs(final StringBuilder sb, final TransactionFilter filter) {
        if (filter.getFetchAccount()) {
            sb.append(" join account a1 on (a1.id=transaction.from_account_id) ");
            sb.append(" join account a2 on (a2.id=transaction.to_account_id) ");
        }
        if (filter.getFetchPaymentType() != null && filter.getFetchPaymentType()) {
            sb.append(" left join payment_type p on (p.id=transaction.payment_type_id) ");
        }
    }

    private void appendWHEREs(StringBuilder sb, TransactionFilter filter) {
        final List<String> ands = new ArrayList<>();
        if (StringUtils.isNotBlank(filter.getNote())) {
            ands.add(String.format("%s.note='%s'", getTableName(), filter.getNote())); // SQL
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
    public void updateBalance(Integer id) {
        throw new UnsupportedOperationException();
    }

}
