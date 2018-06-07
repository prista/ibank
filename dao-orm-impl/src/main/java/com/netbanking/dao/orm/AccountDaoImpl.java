package com.netbanking.dao.orm;

import com.netbanking.dao.orm.model.*;
import com.prista.netbanking.dao.api.IAccountDao;
import com.prista.netbanking.dao.api.filter.AccountFilter;
import com.prista.netbanking.dao.api.model.IAccount;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.criteria.OrderImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountDaoImpl extends AbstractDaoImpl<IAccount, Integer> implements IAccountDao {

    public AccountDaoImpl() {
        super(Account.class);
    }

    @Override
    public IAccount createEntity() {
        return new Account();
    }

    @Override
    public long getCount(AccountFilter filter) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        final Root<Account> from = cq.from(Account.class);
        cq.select(cb.count(from));
        applyFilter(filter, cb, cq, from);
        final TypedQuery<Long> q = em.createQuery(cq);
        return q.getSingleResult();
    }

    @Override
    public IAccount getFullInfo(Integer id) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        // create empty query and define returning type
        final CriteriaQuery<IAccount> criteriaQuery = criteriaBuilder.createQuery(IAccount.class);

        // define target entity(table)
        final Root<Account> from = criteriaQuery.from(Account.class); // select from account

        // define what will be added to result set
        criteriaQuery.select(from); // select * from account

        from.fetch(Account_.card, JoinType.LEFT);

        from.fetch(Account_.userProfile, JoinType.LEFT);

        from.fetch(Account_.bank, JoinType.LEFT);

        // .. where id=..
        criteriaQuery.where(criteriaBuilder.equal(from.get(Account_.id), id)); // all this method is getByIdWithFetch

        final TypedQuery<IAccount> q = em.createQuery(criteriaQuery); // create 'SQL' Query

        final List<IAccount> resultList = q.getResultList();
        final int size = resultList.size();
        if (size != 1) {
            throw new IllegalArgumentException("unexpected result count:" + size);
        }
        return resultList.get(0);
    }

    private void applyFilter(final AccountFilter filter, final CriteriaBuilder cb, final CriteriaQuery<?> cq,
                             final Root<Account> from) {
        final List<Predicate> ands = new ArrayList<>();


        ands.add(cb.notEqual(from.get(Account_.id), -1));

        final String name = filter.getName();
        if (!StringUtils.isBlank(name)) {
            ands.add(cb.equal(from.get(Account_.name), name));
        }

        final Boolean locked = filter.getLocked();
        if (Boolean.FALSE.equals(locked)) {
            ands.add(cb.equal(from.get(Account_.locked), false));
        }

        if (!ands.isEmpty()) {
            cq.where(cb.and(ands.toArray(new Predicate[0])));
        }
    }

    @Override
    public List<IAccount> find(AccountFilter filter) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<IAccount> cq = cb.createQuery(IAccount.class);
        final Root<Account> from = cq.from(Account.class);
        cq.select(from);

        from.fetch(Account_.bank, JoinType.LEFT);
        from.fetch(Account_.userProfile, JoinType.LEFT);

        applyFilter(filter, cb, cq, from);

        // set sort params
        if (filter.getSortColumn() != null) {
            final Path<?> expression = getSortPath(from, filter.getSortColumn());
            cq.orderBy(new OrderImpl(expression, filter.getSortOrder()));
        }

        final TypedQuery<IAccount> q = em.createQuery(cq);
        setPaging(filter, q);
        return q.getResultList();
    }

/*    @Override
    public void delete(Integer id) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();

        // create delete
        CriteriaDelete<Account> cd = cb.
                createCriteriaDelete(Account.class);

        // set the root class
        Root<Account> from = cd.from(Account.class);
        if (from.get(Account_.card).get(Card_.account) != null) {
            cd.where(cb.equal(from.get(Account_.card).get(Card_.id), id));
            em.createQuery(cd).executeUpdate();
        }

        // set where clause
        cd.where(cb.equal(from.get(Account_.id), id));

        // perform update
        em.createQuery(cd).executeUpdate();
    }*/

    @Override
    public void delete(Integer id) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<IAccount> cq = cb.createQuery(IAccount.class);
        Root<Account> from = cq.from(Account.class);


        if (from.get(Account_.card) != null) { // doesn't work if statement
            em.createQuery(String.format("delete from %s e where e.id = :id", Card.class.getSimpleName()))
                    .setParameter("id", id).executeUpdate();
        }

        em.createQuery(String.format("delete from %s e where e.id = :id", Account.class.getSimpleName()))
                .setParameter("id", id).executeUpdate();
    }

    private Path<?> getSortPath(Root<Account> from, String sortColumn) {
        switch (sortColumn) {
            case "id":
                return from.get(Account_.id);
            case "name":
                return from.get(Account_.name);
            case "userProfileUserName":
                return from.get(Account_.userProfile).get(UserProfile_.username);
            case "accountType":
                return from.get(Account_.accountType);
            case "balance":
                return from.get(Account_.balance);
            case "currency":
                return from.get(Account_.currency);
            case "locked":
                return from.get(Account_.locked);
            case "bankName":
                return from.get(Account_.bank).get(Bank_.name);
            case "deleted":
                return from.get(Account_.deleted);
            case "created":
                return from.get(Account_.created);
            case "updated":
                return from.get(Account_.updated);
            default:
                throw new UnsupportedOperationException("sorting is not supported by column:" + sortColumn);
        }
    }
}
