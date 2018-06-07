package com.netbanking.dao.orm;

import com.netbanking.dao.orm.model.Bank;
import com.netbanking.dao.orm.model.Bank_;
import com.prista.netbanking.dao.api.IBankDao;
import com.prista.netbanking.dao.api.filter.BankFilter;
import com.prista.netbanking.dao.api.model.IBank;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.criteria.OrderImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.List;

@Repository
public class BankDaoImpl extends AbstractDaoImpl<IBank, Integer> implements IBankDao {

    protected BankDaoImpl() {
        super(Bank.class);
    }

    @Override
    public IBank createEntity() {
        return new Bank();
    }

    @Override
    public long getCount(final BankFilter filter) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        final Root<Bank> from = cq.from(Bank.class);

        // append 'where' condition for search form
        final String name = filter.getName();
        if (!StringUtils.isBlank(name)) {
            cq.where(cb.equal(from.get(Bank_.name), name));
        }

        cq.select(cb.count(from));
        final TypedQuery<Long> q = em.createQuery(cq);
        return q.getSingleResult();
    }

    @Override
    public List<IBank> find(BankFilter filter) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();

        // create empty query and define returning type
        final CriteriaQuery<IBank> cq = cb.createQuery(IBank.class);

        // define target entity(table)
        final Root<Bank> from = cq.from(Bank.class);

        // define what will be added to result set
        cq.select(from); // select * from bank

        // append 'where' condition for search form
        final String name = filter.getName();
        if (!StringUtils.isBlank(name)) {
            cq.where(cb.equal(from.get(Bank_.name), name));
        }

        if (filter.getSortColumn() != null) {
            final SingularAttribute<? super Bank, ?> sortProperty = toMetamodelFormat(filter.getSortColumn());
            final Path<?> expression = from.get(sortProperty);
            cq.orderBy(new OrderImpl(expression, filter.getSortOrder()));
        }

        final TypedQuery<IBank> q = em.createQuery(cq);
        setPaging(filter, q);
        return q.getResultList();
    }

    private SingularAttribute<? super Bank, ?> toMetamodelFormat(String sortColumn) {
        switch (sortColumn) {
            case "created":
                return Bank_.created;
            case "updated":
                return Bank_.updated;
            case "id":
                return Bank_.id;
            case "name":
                return Bank_.name;
            default:
                throw new UnsupportedOperationException("sorting is not supported by column:" + sortColumn);
        }
    }
}
