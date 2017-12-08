package com.bluestone.app.design.dao;

import java.math.BigDecimal;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.design.model.StoneSpecification;

@Repository("stoneSpecificationsDao")
public class StoneSpecificationsDao extends BaseDao {

    private static final Logger log = LoggerFactory.getLogger(StoneSpecificationsDao.class);

    private final String STONESPEC_FIND_QUERY = "select s from StoneSpecification s where " +
                                                "s.size=:size " +
                                                "and s.shape=:shape " +
                                                "and s.stoneType=:stoneType " +
                                                "and s.color=:color " +
                                                "and s.clarity=:clarity " +
                                                "and s.stoneWeight=:stoneWeight";

    private final String SOLITAIRE_STONESPEC_FIND_QUERY = "select s from StoneSpecification s where " +
                                                          "s.minCarat=:minCarat and s.maxCarat=:maxCarat and s.shape=:shape";

    public StoneSpecification getStoneSpecifications(StoneSpecification stoneSpecification) {
        StoneSpecification result;

        if (isSolitaire(stoneSpecification)) {
            result = checkForExistingSolitaireStone(stoneSpecification);
        } else {
            result = checkForExistingStone(stoneSpecification);
        }
        return result;
    }

    private boolean isSolitaire(StoneSpecification stoneSpecification) {
        boolean isSolitaire = false;
        final BigDecimal minCarat = stoneSpecification.getMinCarat();
        final BigDecimal maxCarat = stoneSpecification.getMaxCarat();
        if (minCarat != null && maxCarat != null) {
            isSolitaire = true;
        }
        log.info("StoneSpecificationsDao.isSolitaire()=[{}]", isSolitaire);
        return isSolitaire;
    }

    private StoneSpecification checkForExistingSolitaireStone(StoneSpecification stoneSpecification) {
        StoneSpecification result;
        final BigDecimal minCarat = stoneSpecification.getMinCarat();
        final BigDecimal maxCarat = stoneSpecification.getMaxCarat();
        final String shape = stoneSpecification.getShape();
        log.info("StoneSpecificationsDao.checkForExistingSolitaireStone() with Shape=[{}] , Min-Max carat range =[{} - {}]", shape, minCarat, maxCarat);
        Query findQuery = getEntityManagerForActiveEntities().createQuery(SOLITAIRE_STONESPEC_FIND_QUERY);
        findQuery.setParameter("shape", shape);
        findQuery.setParameter("minCarat", minCarat);
        findQuery.setParameter("maxCarat", maxCarat);
        try {
            result = (StoneSpecification) findQuery.getSingleResult();
        } catch (NoResultException nre) {
            result = stoneSpecification; // there is not existing record, so we will create a new record.
            log.info("StoneSpecificationsDao.checkForExistingSolitaireStone(): No existing Solitaire Stone with Shape=[{}] Min-Max range=[{} : {}]",
                     shape, minCarat.toString(), maxCarat.toString());
        } catch (Exception e) {
            log.error("Error: StoneSpecificationsDao.checkForExistingSolitaireStone(): For Solitaire Stone with Shape=[{}] Min-Max range=[{} : {}]",
                      shape, minCarat.toString(), maxCarat.toString(), e);
            throw new RuntimeException("Failed while verifying if we have an existing stone in the stone specification table. Reason=" + e.toString(),
                                       Throwables.getRootCause(e));
        }
        return result;
    }


    public StoneSpecification checkForExistingStone(StoneSpecification stoneSpecification) {
        log.info("StoneSpecificationsDao.checkForExistingStone()");
        StoneSpecification result;
        String size = stoneSpecification.getSize();
        String shape = stoneSpecification.getShape();
        String type = stoneSpecification.getStoneType();
        String color = stoneSpecification.getColor();
        String clarity = stoneSpecification.getClarity();
        BigDecimal stoneWeight = stoneSpecification.getStoneWeight();
        Query findQuery = getEntityManagerForActiveEntities().createQuery(STONESPEC_FIND_QUERY);
        findQuery.setParameter("size", size);
        findQuery.setParameter("shape", shape);
        findQuery.setParameter("stoneType", type);
        findQuery.setParameter("color", color);
        findQuery.setParameter("clarity", clarity);
        findQuery.setParameter("stoneWeight", stoneWeight);
        findQuery.setMaxResults(1);
        log.info("StoneSpecificationsDao.checkForExistingStone(): *** Deliberately setting the max results to ONE. So it means even if we have more than 1 record, we are getting just 1 record.");
        try {
            result = (StoneSpecification) findQuery.getSingleResult();
        } catch (NoResultException noResultException) {
            result = stoneSpecification;
            log.info("Database does not have any existing stone specification with Size=[{}] Shape=[{}] Type=[{}] Color=[{}] Clarity=[{}] Weight=[{}]",
                     size, shape, type, color, clarity, stoneWeight);
        } catch (Exception e) {
            throw new RuntimeException("Failed while verifying if we have an existing stone in the stone specification table. Reason=" + e.toString(),
                                       Throwables.getRootCause(e));
        }
        return result;
    }

    /*public StoneSpecification getStoneSpecification(String size, String shape, String type, String color, String clarity, BigDecimal stoneWeight) {
        log.info("StoneSpecificationsDao.getStoneSpecifications(): Size=[{}] Shape=[{}] Type=[{}]", size, shape, type);
        Query findQuery = getEntityManagerForActiveEntities().createQuery(STONESPEC_FIND_QUERY);
        findQuery.setParameter("size", size);
        findQuery.setParameter("shape", shape);
        findQuery.setParameter("stoneType", type);
        findQuery.setParameter("color", color);
        findQuery.setParameter("clarity", clarity);
        findQuery.setParameter("stoneWeight", stoneWeight);
        findQuery.setMaxResults(1);
        try {
            return (StoneSpecification) findQuery.getSingleResult();
        } catch (NoResultException noResultException) {
            log.info("Database does not have any existing stonespecification with Size=[{}] Shape=[{}] Type=[{}] Color=[{}] Clarity=[{}] Weight=[{}]", size, shape, type, color, clarity, stoneWeight);
            return null;
        }
    }*/

}

