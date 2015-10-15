package com.bluestone.app.design.dao;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.design.model.MetalSpecification;

@Repository("metalSpecificationDao")
public class MetalSpecificationDao extends BaseDao {

    private static final Logger log = LoggerFactory.getLogger(MetalSpecificationDao.class);

    public MetalSpecification getMetalSpecifications(MetalSpecification metalSpecification) {
        final String color = metalSpecification.getColor();
        final int purity = metalSpecification.getPurity();
        final String type = metalSpecification.getType();
        MetalSpecification metalSpecificationFromDb = getMetalSpecification(color, purity, type);
        if(metalSpecificationFromDb != null) {
            return metalSpecificationFromDb;
        } else {
            return metalSpecification;
        }
    }

    public MetalSpecification getMetalSpecification(final String color, final int purity, final String type) {
        log.debug("MetalSpecificationDao.getMetalSpecifications(): color = {} purity = {} type = {}",
                  color, purity, type);

        Query createQuery = getEntityManagerForActiveEntities().createQuery("select m from MetalSpecification m where m.color=:color and m.type=:type and m.purity=:purity");
        createQuery.setParameter("color", color);
        createQuery.setParameter("type", type);
        createQuery.setParameter("purity", purity);
        try {
            return (MetalSpecification) createQuery.getSingleResult();
        } catch (NoResultException noResultException) {
            log.info("MetalSpecificationDao.getMetalSpecifications(): No record found in metalspecification table where color = {} purity = {} type = {}", color, purity, type);
            return null;
        }
    }

}
