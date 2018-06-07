package com.prista.netbanking.dao.jdbc;

import com.prista.netbanking.dao.api.ICardDao;
import com.prista.netbanking.dao.api.model.ICard;
import com.prista.netbanking.dao.jdbc.exception.SQLExecutionException;
import com.prista.netbanking.dao.jdbc.model.Card;
import com.prista.netbanking.dao.jdbc.utils.PreparedStatemenAction;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class CardDaoImpl extends AbstractDaoImpl<ICard, Integer> implements ICardDao {

    @Override
    protected ICard parseRow(final ResultSet resultSet) throws SQLException {
        final ICard entity = createEntity();

        entity.setId((Integer) resultSet.getObject("id"));
        entity.setCardType(resultSet.getString("card_type"));
        entity.setExpirationDate(resultSet.getTimestamp("expiration_date"));
        entity.setCreated(resultSet.getTimestamp("created"));
        entity.setUpdated(resultSet.getTimestamp("updated"));

        return entity;
    }

    @Override
    protected String getTableName() {
        return "card";
    }

    @Override
    public ICard createEntity() {
        return new Card();
    }

    @Override
    public void update(final ICard entity) {

        try (Connection c = getConnection();
             PreparedStatement pStmt = c.prepareStatement(
                     String.format("update %s set card_type=?, expiration_date=?, updated=? where id=?", getTableName()))) {
            c.setAutoCommit(false);
            try {
                pStmt.setString(1, entity.getCardType());
                pStmt.setObject(2, entity.getExpirationDate(), Types.TIMESTAMP);
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
    public void insert(final ICard entity) {
        executeStatement(new PreparedStatemenAction<ICard>(
                "insert into card (id, card_type, expiration_date, created, updated) "
                        + "values(?,?,?,?,?)") {
            @Override
            public ICard doWithPreparedStatement(final PreparedStatement pStmt)
                    throws SQLException {
                pStmt.setInt(1, entity.getId());
                pStmt.setString(2, entity.getCardType());
                pStmt.setObject(3, entity.getExpirationDate(), Types.TIMESTAMP);
                pStmt.setObject(4, entity.getCreated(), Types.TIMESTAMP);
                pStmt.setObject(5, entity.getUpdated(), Types.TIMESTAMP);
                pStmt.executeUpdate();

                return entity;
            }
        });
    }

}
