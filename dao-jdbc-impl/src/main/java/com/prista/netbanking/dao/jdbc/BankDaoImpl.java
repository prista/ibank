package com.prista.netbanking.dao.jdbc;

import com.prista.netbanking.dao.api.IBankDao;
import com.prista.netbanking.dao.api.filter.BankFilter;
import com.prista.netbanking.dao.api.model.IBank;
import com.prista.netbanking.dao.jdbc.model.Bank;
import com.prista.netbanking.dao.jdbc.utils.PreparedStatemenAction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class BankDaoImpl extends AbstractDaoImpl<IBank, Integer> implements IBankDao {

    @Override
    protected IBank parseRow(final ResultSet resultSet) throws SQLException {
        final IBank entity = createEntity();
        entity.setId((Integer) resultSet.getObject("id"));
        entity.setName(resultSet.getString("name"));
        entity.setCreated(resultSet.getTimestamp("created"));
        entity.setUpdated(resultSet.getTimestamp("updated"));
        return entity;
    }

    @Override
    protected String getTableName() {
        return "bank";
    }

    @Override
    public IBank createEntity() {
        return new Bank();
    }

    @Override
    public void update(final IBank entity) {
        executeStatement(new PreparedStatemenAction<IBank>(
                "update " + getTableName() + " set name=?, updated=? where id=?") {
            @Override
            public IBank doWithPreparedStatement(final PreparedStatement pStmt)
                    throws SQLException {
                pStmt.setString(1, entity.getName());
                pStmt.setObject(2, entity.getUpdated(), Types.TIMESTAMP);
                pStmt.setInt(3, entity.getId());

                final int i = pStmt.executeUpdate();
                if (i == 1) {
                    return entity;
                }
                throw new RuntimeException("Wrong Update");
            }
        });
    }

    @Override
    public void insert(final IBank entity) {
        executeStatement(new PreparedStatemenAction<IBank>(
                "insert into " + getTableName() + " (name, created, updated) values(?,?,?)", true) {
            @Override
            public IBank doWithPreparedStatement(final PreparedStatement pStmt)
                    throws SQLException {
                pStmt.setString(1, entity.getName());
                pStmt.setObject(2, entity.getCreated(), Types.TIMESTAMP);
                pStmt.setObject(3, entity.getUpdated(), Types.TIMESTAMP);

                final int i = pStmt.executeUpdate();
                if (i == 1) {
                    final ResultSet rs = pStmt.getGeneratedKeys();
                    rs.next();
                    final int id = rs.getInt("id");

                    rs.close();

                    entity.setId(id);
                    return entity;
                }

                throw new RuntimeException("Wrong Insert");
            }
        });

    }

    @Override
    public long getCount(BankFilter filter) {
        final StringBuilder sqlPart = new StringBuilder("");
        appendWHEREs(sqlPart, filter);
        return executeCountQuery(sqlPart.toString());
    }

    @Override
    public List<IBank> find(BankFilter filter) {
        final StringBuilder sqlPart = new StringBuilder("");
        appendWHEREs(sqlPart, filter);
        appendSort(filter, sqlPart);
        appendPaging(filter, sqlPart);
        return executeFindQuery(sqlPart.toString());
    }

    private void appendWHEREs(final StringBuilder sb, final BankFilter filter) {
        final List<String> ands = new ArrayList<String>();
        if (StringUtils.isNotBlank(filter.getName())) {
            ands.add(String.format("name='%s'", filter.getName())); // SQL
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
