package com.netbanking.dao.orm;

import com.netbanking.dao.orm.model.UserProfile;
import com.netbanking.dao.orm.model.UserProfile_;
import com.prista.netbanking.dao.api.IUserProfileDao;
import com.prista.netbanking.dao.api.filter.UserProfileFilter;
import com.prista.netbanking.dao.api.model.IUserProfile;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.criteria.OrderImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class UserProfileDaoImpl extends AbstractDaoImpl<IUserProfile, Integer> implements IUserProfileDao {

    protected UserProfileDaoImpl() {
        super(UserProfile.class);
    }

    @Override
    public IUserProfile createEntity() {
        return new UserProfile();
    }

    @Override
    public long getCount(UserProfileFilter filter) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        final Root<UserProfile> from = cq.from(UserProfile.class);
        cq.select(cb.count(from));

        // append 'where' condition for search form
        applyFilter(filter, cb, cq, from);

        final TypedQuery<Long> q = em.createQuery(cq);
        return q.getSingleResult();
    }

    private void applyFilter(UserProfileFilter filter, CriteriaBuilder cb, CriteriaQuery<?> cq, Root<UserProfile> from) {
        final List<Predicate> ands = new ArrayList<>();

        final String name = filter.getUserName();
        if (!StringUtils.isBlank(name)) {
            cq.where(cb.equal(from.get(UserProfile_.username), name));
        }
        final String role = filter.getRole();
        if (!StringUtils.isBlank(role)) {
            cq.where(cb.equal(from.get(UserProfile_.role), role));
        }

        if (!ands.isEmpty()) {
            cq.where(cb.and(ands.toArray(new Predicate[0])));
        }
    }

    @Override
    public List<IUserProfile> find(UserProfileFilter filter) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<IUserProfile> cq = cb.createQuery(IUserProfile.class);
        final Root<UserProfile> from = cq.from(UserProfile.class);
        cq.select(from);

        // append 'where' condition for search form
        applyFilter(filter, cb, cq, from);

        final String sortColumn = filter.getSortColumn();
        if (sortColumn != null) {
            final Path<?> expression = getSortPath(from, sortColumn);
            cq.orderBy(new OrderImpl(expression, filter.getSortOrder()));
        }

        final TypedQuery<IUserProfile> q = em.createQuery(cq);
        setPaging(filter, q);
        return q.getResultList();
    }

    private Path<?> getSortPath(Root<UserProfile> from, String sortColumn) {
        switch (sortColumn) {
            case "username":
                return from.get(UserProfile_.username);
            case "created":
                return from.get(UserProfile_.created);
            case "updated":
                return from.get(UserProfile_.updated);
            case "id":
                return from.get(UserProfile_.id);
            case "role":
                return from.get(UserProfile_.role);
            default:
                throw new UnsupportedOperationException("sorting is not supported by column:" + sortColumn);
        }
    }

    @Override
    public Set<IUserProfile> getByBranch(Integer id) {
        return null;
    }
}
