package com.netbanking.dao.orm;

import com.netbanking.dao.orm.model.Account_;
import com.netbanking.dao.orm.model.PaymentType_;
import com.netbanking.dao.orm.model.Transaction;
import com.netbanking.dao.orm.model.Transaction_;
import com.prista.netbanking.dao.api.ITransactionDao;
import com.prista.netbanking.dao.api.filter.TransactionFilter;
import com.prista.netbanking.dao.api.model.ITransaction;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.criteria.OrderImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class TransactionDaoImpl extends AbstractDaoImpl<ITransaction, Integer> implements ITransactionDao {

    protected TransactionDaoImpl() {
        super(Transaction.class);
    }

    @Override
    public ITransaction createEntity() {
        return new Transaction();
    }

    @Override
    public long getCount(TransactionFilter filter) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();

        // create empty query and define returning type
        final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        // define target entity(table)
        final Root<Transaction> from = cq.from(Transaction.class);

        cq.select(cb.count(from));

        // append 'where' condition for search form
        final String note = filter.getNote();
        if (!StringUtils.isBlank(note)) {
            cq.where(cb.equal(from.get(Transaction_.note), note));
        }

        final TypedQuery<Long> q = em.createQuery(cq);
        return q.getSingleResult();
    }

    @Override
    public void insert(ITransaction entity) {
        super.insert(entity);
        updateBalance(entity.getToAccount().getId());
        if (entity.getFromAccount().getId() != -1) {
            updateBalance(entity.getFromAccount().getId());
        }
    }

    @Override
    public List<ITransaction> find(TransactionFilter filter) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();

        // create empty query and define returning type
        final CriteriaQuery<ITransaction> cq = cb.createQuery(ITransaction.class);

        // define target entity(table)
        final Root<Transaction> from = cq.from(Transaction.class); // select from transaction

        // define what will be added to result set
        cq.select(from); // select * from transaction

        if (filter.getFetchAccount()) {
            from.fetch(Transaction_.fromAccount, JoinType.LEFT);
            from.fetch(Transaction_.toAccount, JoinType.LEFT);
        }

        if (filter.getFetchPaymentType()) {
            from.fetch(Transaction_.paymentType, JoinType.LEFT);
        }

        // append 'where' condition for search form
        final String note = filter.getNote();
        if (!StringUtils.isBlank(note)) {
            cq.where(cb.equal(from.get(Transaction_.note), note));
        }


        final String sortColumn = filter.getSortColumn();
        if (sortColumn != null) {
            final Path<?> expression = getSortPath(from, sortColumn);
            cq.orderBy(new OrderImpl(expression, filter.getSortOrder()));
        }

        final TypedQuery<ITransaction> q = em.createQuery(cq);
        setPaging(filter, q);
        final List<ITransaction> resultList = q.getResultList();
        return resultList;
    }

    @Override
    public ITransaction getFullInfo(Integer id) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();

        final CriteriaQuery<ITransaction> cq = cb.createQuery(ITransaction.class);
        final Root<Transaction> from = cq.from(Transaction.class);

        cq.select(from);

        from.fetch(Transaction_.fromAccount, JoinType.LEFT);

        from.fetch(Transaction_.toAccount, JoinType.LEFT);

        from.fetch(Transaction_.paymentType, JoinType.LEFT);
        cq.distinct(true); // to avoid duplicate rows in result

        //.. where id=...
        cq.where(cb.equal(from.get(Transaction_.id), id));

        final TypedQuery<ITransaction> q = em.createQuery(cq);

        return getSingleResult(q);
    }

    private Path<?> getSortPath(Root<Transaction> from, String sortColumn) {
        switch (sortColumn) {
            case "accountFromName":
                return from.get(Transaction_.fromAccount).get(Account_.name);
            case "accountToName":
                return from.get(Transaction_.toAccount).get(Account_.name);
            case "note":
                return from.get(Transaction_.note);
            case "amount":
                return from.get(Transaction_.amount);
            case "id":
                return from.get(Transaction_.id);
            case "created":
                return from.get(Transaction_.created);
            case "updated":
                return from.get(Transaction_.updated);
            case "transactionType":
                return from.get(Transaction_.transactionType);
            case "paymentTypeName":
                return from.get(Transaction_.paymentType).get(PaymentType_.name);
            default:
                throw new UnsupportedOperationException("sorting is not supported by column:" + sortColumn);
        }
    }

    @Override
    public ITransaction getNewestTransaction() {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();

        // create empty query and define returning type
        final CriteriaQuery<ITransaction> cq = cb.createQuery(ITransaction.class);
        // define target entity(table)
        final Root<Transaction> from = cq.from(Transaction.class);

        cq.select(from);
        cq.orderBy(cb.desc(from.get(Transaction_.created)));

        final TypedQuery<ITransaction> q = em.createQuery(cq);
        q.setMaxResults(1);
        return getSingleResult(q);
    }

    @Override
    public void updateBalance(Integer id) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();

        // create empty query and define returning type
        final CriteriaQuery<Double> cq = cb.createQuery(Double.class);
        // define target entity(table)
        final Root<Transaction> from = cq.from(Transaction.class);

        cq.select(cb.sum(from.get(Transaction_.amount)));

        // append 'where' condition
        cq.where(cb.equal(from.get(Transaction_.fromAccount).get(Account_.id), id));

        TypedQuery<Double> q = em.createQuery(cq);
        Double fromAmount = q.getSingleResult();
        if (fromAmount == null) {
            fromAmount = 0.0;
        }
        Double toAmount = getToAmount(id);

        String sqlString = String.format("update account set balance=((%s)-(%s)) where id=%s", toAmount, fromAmount, id);
        em.createNativeQuery(sqlString).executeUpdate();
    }

    private Double getToAmount(Integer id) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Double> cq = cb.createQuery(Double.class);
        final Root<Transaction> from = cq.from(Transaction.class);
        cq.select(cb.sum(from.get(Transaction_.amount)));
        cq.where(cb.equal(from.get(Transaction_.toAccount).get(Account_.id), id));
        TypedQuery<Double> q = em.createQuery(cq);
        Double amount = q.getSingleResult();
        if (amount == null) {
            amount = 0.0;
        }
        return amount;
    }
}
