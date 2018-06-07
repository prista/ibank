package com.netbanking.dao.orm;

import com.netbanking.dao.orm.model.Bank_;
import com.netbanking.dao.orm.model.Branch;
import com.netbanking.dao.orm.model.Branch_;
import com.prista.netbanking.dao.api.IBranchDao;
import com.prista.netbanking.dao.api.filter.BranchFilter;
import com.prista.netbanking.dao.api.model.IBranch;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.criteria.OrderImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BranchDaoImpl extends AbstractDaoImpl<IBranch, Integer> implements IBranchDao {

    protected BranchDaoImpl() {
        super(Branch.class);
    }

    @Override
    public IBranch createEntity() {
        return new Branch();
    }

    @Override
    public long getCount(BranchFilter filter) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();

        // create empty query and define returning type
        final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        // define target entity(table)
        final Root<Branch> from = cq.from(Branch.class);

        cq.select(cb.count(from));

        applyFilter(filter, cb, cq, from);

        final TypedQuery<Long> q = em.createQuery(cq);
        return q.getSingleResult();
    }

    private void applyFilter(BranchFilter filter, CriteriaBuilder cb, CriteriaQuery<?> cq, Root<Branch> from) {
        final List<Predicate> ands = new ArrayList<>();

        final String name = filter.getName();
        if (!StringUtils.isBlank(name)) {
            ands.add(cb.equal(from.get(Branch_.name), name));
        }
        final String bankName = filter.getBankName();
        if (!StringUtils.isBlank(bankName)) {
            ands.add(cb.equal(from.get(Branch_.bank).get(Bank_.name), bankName));
        }

        if (!ands.isEmpty()) {
            cq.where(cb.and(ands.toArray(new Predicate[0])));
        }
    }

    @Override
    public List<IBranch> find(BranchFilter filter) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();

        // create empty query and define returning type
        final CriteriaQuery<IBranch> cq = cb.createQuery(IBranch.class);

        // define target entity(table)
        final Root<Branch> from = cq.from(Branch.class); // select from branch

        // define what will be added to result set
        cq.select(from); // select * from branch

        if (filter.getFetchBank()) {
            from.fetch(Branch_.bank, JoinType.LEFT);
        }

        applyFilter(filter, cb, cq, from);


        final String sortColumn = filter.getSortColumn();
        if (sortColumn != null) {
            final Path<?> expression = getSortPath(from, sortColumn);
            cq.orderBy(new OrderImpl(expression, filter.getSortOrder()));
        }

        final TypedQuery<IBranch> q = em.createQuery(cq);
        setPaging(filter, q);
        final List<IBranch> resultList = q.getResultList();
        return resultList;
    }

    @Override
    public IBranch getFullInfo(final Integer id) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();

        final CriteriaQuery<IBranch> cq = cb.createQuery(IBranch.class);
        final Root<Branch> from = cq.from(Branch.class);

        cq.select(from);

        from.fetch(Branch_.bank, JoinType.LEFT);

        from.fetch(Branch_.userProfiles, JoinType.LEFT);
        cq.distinct(true); // to avoid duplicate rows in result

        //.. where id=...
        cq.where(cb.equal(from.get(Branch_.id), id));

        final TypedQuery<IBranch> q = em.createQuery(cq);

        return getSingleResult(q);
    }

    private Path<?> getSortPath(Root<Branch> from, String sortColumn) {
        switch (sortColumn) {
            case "name":
                return from.get(Branch_.name);
            case "created":
                return from.get(Branch_.created);
            case "updated":
                return from.get(Branch_.updated);
            case "id":
                return from.get(Branch_.id);
            case "city":
                return from.get(Branch_.city);
            case "bankName":
                return from.get(Branch_.bank).get(Bank_.name);
            default:
                throw new UnsupportedOperationException("sorting is not supported by column:" + sortColumn);
        }
    }
}
