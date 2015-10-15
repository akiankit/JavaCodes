package com.bluestone.app.core.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.core.model.BaseEntity;

@Repository("baseDao")
public class BaseDao {
    
    private static final Logger log = LoggerFactory.getLogger(BaseDao.class);
    
    @PersistenceContext
    private EntityManager       entityManager;
    
    public EntityManager getEntityManagerForNonActiveEntities() {
        Session session = getSession();
        session.enableFilter("isActive").setParameter("isActive", (short) 0);
        return entityManager;
    }
    
    public EntityManager getEntityManagerForActiveEntities() {
        Session session = getSession();
        session.enableFilter("isActive").setParameter("isActive", (short) 1);
        return entityManager;
    }
    
    public EntityManager getEntityManagerWithoutFilter() {
        Session session = getSession();
        session.disableFilter("isActive");
        return entityManager;
    }

    private Session getSession() {
        Session session = entityManager.unwrap(Session.class);
        return session;
    }
    
    public void create(BaseEntity entity) {
        log.debug("BaseDao.create():", entity.getClass().getName());
        getEntityManagerWithoutFilter().persist(entity);
    }
    
    public <T> List<T> list(Class<T> entityClass) {
        Session session = (Session) getEntityManagerWithoutFilter().getDelegate();
        List<T> list = session.createCriteria(entityClass).list();
        return list;
    }
    
    public <T> T find(Class<T> entityClass, long primaryKey, boolean forActiveEntities) {
        log.trace("BaseDao.find for Entity {} primaryKey {} forActive {}", entityClass, primaryKey, forActiveEntities);
        
        // As Jpa/hibernate doesn't honour filter on find calls, explicitly
        // checking whether entity is active or not
        T result = getEntityManagerWithoutFilter().find(entityClass, primaryKey);
        if (result == null) {
            return result;
        }
        BaseEntity baseEntity = (BaseEntity) result;
        log.trace("BaseDao.find result={}", baseEntity);
        if (forActiveEntities) {
            if (baseEntity.isActive()) {
                return result;
            } else {
                return null;
            }
        } else {
            if (!baseEntity.isActive()) {
                return result;
            } else {
                return null;
            }
        }
    }
    
    public <T> T findAny(Class<T> entityClass, long primaryKey) {
        log.trace("BaseDao.findAny(): Class={} , Primary Key={}", entityClass.getName(), primaryKey);
        return getEntityManagerWithoutFilter().find(entityClass, primaryKey);
    }
    
    public <T> T update(T entity) {
        log.debug("BaseDao.update():", entity.getClass().getName());
        return getEntityManagerWithoutFilter().merge(entity);
    }
    
    public void remove(BaseEntity entity) {
        log.debug("BaseDao.remove():", entity.getClass().getName());
        getEntityManagerWithoutFilter().remove(entity);
        
    }
    
    public void remove(Class<? extends BaseEntity> entityClass, long primaryKey) {
        log.debug("BaseDao.remove(): {} primary key={}", entityClass.getName(), primaryKey);
        BaseEntity entity = getEntityManagerWithoutFilter().find(entityClass, primaryKey);
        if (entity != null) {
            getEntityManagerWithoutFilter().remove(entity);
        }
    }
    
    public BaseEntity markEntityAsDisabled(BaseEntity baseEntity) {
        baseEntity.setIsActive(false);
        return update(baseEntity);
    }
    
    public BaseEntity markEntityAsEnabled(BaseEntity baseEntity) {
        baseEntity.setIsActive(true);
        return update(baseEntity);
    }
    
   /* public void updateSearchIndex(BaseEntity baseEntity) {
        log.debug("BaseDao.updateSearchIndex(): {}", baseEntity.getClass().getName());
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(getEntityManagerWithoutFilter());
        fullTextEntityManager.index(baseEntity);
    }*/
}
