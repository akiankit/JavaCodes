package com.bluestone.app.design.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.design.model.DesignCategory;

@Repository("designCategoryDao")
public class DesignCategoryDao extends BaseDao {

    private static final Logger log = LoggerFactory.getLogger(DesignCategoryDao.class);

    public DesignCategory getDesignCategory(String name) {
        log.trace("DesignCategoryDao.getDesignCategory() for {}", name);
        Query createQuery = getEntityManagerForActiveEntities().createQuery("select d from DesignCategory d where d.categoryType=:name");
        createQuery.setParameter("name", name);
        try {
            return (DesignCategory) createQuery.getSingleResult();
        } catch (NoResultException e) {
            log.debug("No result for DesignCategoryDao.getDesignCategory({})", name, e);
            return null;
        }
    }
    
    public List<DesignCategory> getDesignCategoryFromList(List<String> name) {
        log.trace("DesignCategoryDao.getDesignCategory() for {}", name);
        Query createQuery = getEntityManagerForActiveEntities().createQuery("select d from DesignCategory d where d.categoryType in (:name)");
        createQuery.setParameter("name", name);
         return createQuery.getResultList();
       
    }
}
