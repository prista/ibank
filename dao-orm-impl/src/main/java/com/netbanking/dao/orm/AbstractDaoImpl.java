package com.netbanking.dao.orm;

import com.prista.netbanking.dao.api.BaseDao;
import com.prista.netbanking.dao.api.filter.AbstractFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public abstract class AbstractDaoImpl<T, ID> implements BaseDao<T, ID> {

    @PersistenceContext
    private EntityManager entityManager;

    private final Class<? extends T> entityClass;

    protected AbstractDaoImpl(final Class<? extends T> entityClass) {
        this.entityClass = entityClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Deprecated
    public List<T> selectAll() {
        final CriteriaQuery<? extends T> query = entityManager.getCriteriaBuilder().createQuery(getEntityClass());
        query.from(getEntityClass());
        final List<? extends T> lst = entityManager.createQuery(query).getResultList();
        return (List<T>) lst;
    }

    @Override
    public T get(final ID id) {
        return entityManager.find(getEntityClass(), id);
    }

    @Override
    public void insert(final T entity) {
        entityManager.persist(entity);
    }

    @Override
    public void update(T entity) {
        entity = entityManager.merge(entity);
        entityManager.flush();
    }

    @Override
    public void delete(final ID id) {
        entityManager.createQuery(String.format("delete from %s e where e.id = :id", entityClass.getSimpleName()))
                .setParameter("id", id).executeUpdate();
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery(String.format("delete from %s", entityClass.getSimpleName())).executeUpdate();

    }

    public Class<? extends T> getEntityClass() {
        return entityClass;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected void setPaging(final AbstractFilter filter, final TypedQuery<?> q) {
        if (filter.getOffset() != null) {
            q.setFirstResult(filter.getOffset());
        }

        if (filter.getLimit() != null) {
            q.setMaxResults(filter.getLimit());
        }
    }

    protected T getSingleResult(final TypedQuery<T> q) {
        final List<T> resultList = q.getResultList();
        final int size = resultList.size();
        if (size != 1) {
            throw new IllegalArgumentException("unexpected result count:" + size);
        }
        return resultList.get(0);
    }

}