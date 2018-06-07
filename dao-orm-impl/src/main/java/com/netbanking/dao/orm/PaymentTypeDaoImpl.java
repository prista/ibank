package com.netbanking.dao.orm;

import com.netbanking.dao.orm.model.PaymentType;
import com.netbanking.dao.orm.model.PaymentType_;
import com.prista.netbanking.dao.api.IPaymentTypeDao;
import com.prista.netbanking.dao.api.filter.PaymentTypeFilter;
import com.prista.netbanking.dao.api.model.IPaymentType;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.criteria.OrderImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class PaymentTypeDaoImpl extends AbstractDaoImpl<IPaymentType, Integer> implements IPaymentTypeDao {

    protected PaymentTypeDaoImpl() {
        super(PaymentType.class);
    }

    @Override
    public IPaymentType createEntity() {
        return new PaymentType();
    }

    @Override
    public long getCount(PaymentTypeFilter filter) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        final Root<PaymentType> from = cq.from(PaymentType.class);
        cq.select(cb.count(from));

        final String name = filter.getName();
        if (!StringUtils.isBlank(name)) {
            cq.where(cb.equal(from.get(PaymentType_.name), name));
        }

        final TypedQuery<Long> q = em.createQuery(cq);
        return q.getSingleResult();
    }

    @Override
    public List<IPaymentType> find(PaymentTypeFilter filter) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<IPaymentType> cq = cb.createQuery(IPaymentType.class);
        final Root<PaymentType> from = cq.from(PaymentType.class);
        cq.select(from);

        from.fetch(PaymentType_.parent, JoinType.LEFT);

        final String name = filter.getName();
        if (!StringUtils.isBlank(name)) {
            cq.where(cb.equal(from.get(PaymentType_.name), name));
        }

        // set sort params
        if (filter.getSortColumn() != null) {
            final Path<?> expression = getSortPath(from, filter.getSortColumn());
            cq.orderBy(new OrderImpl(expression, filter.getSortOrder()));
        }

        final TypedQuery<IPaymentType> q = em.createQuery(cq);
        setPaging(filter, q);
        return q.getResultList();
    }

    @Override
    public IPaymentType getFullInfo(Integer id) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();

        final CriteriaQuery<IPaymentType> cq = cb.createQuery(IPaymentType.class);
        final Root<PaymentType> from = cq.from(PaymentType.class);

        cq.select(from);

        from.fetch(PaymentType_.parent, JoinType.LEFT);

        //cq.distinct(true); // to avoid duplicate rows in result

        //.. where id=...
        cq.where(cb.equal(from.get(PaymentType_.id), id));

        final TypedQuery<IPaymentType> q = em.createQuery(cq);

        return getSingleResult(q);
    }

    private Path<?> getSortPath(Root<PaymentType> from, String sortColumn) {
        switch (sortColumn) {
            case "id":
                return from.get(PaymentType_.id);
            case "name":
                return from.get(PaymentType_.name);
            case "parentName":
                return from.get(PaymentType_.parent).get(PaymentType_.name);
            case "created":
                return from.get(PaymentType_.created);
            case "updated":
                return from.get(PaymentType_.updated);
            default:
                throw new UnsupportedOperationException("sorting is not supported by column:" + sortColumn);
        }
    }
}
