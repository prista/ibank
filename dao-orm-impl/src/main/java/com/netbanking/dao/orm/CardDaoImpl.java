package com.netbanking.dao.orm;

import com.netbanking.dao.orm.model.Card;
import com.prista.netbanking.dao.api.ICardDao;
import com.prista.netbanking.dao.api.model.ICard;
import org.springframework.stereotype.Repository;

@Repository
public class CardDaoImpl extends AbstractDaoImpl<ICard, Integer> implements ICardDao {

    protected CardDaoImpl() {
        super(Card.class);
    }

    @Override
    public ICard createEntity() {
        return new Card();
    }
}
