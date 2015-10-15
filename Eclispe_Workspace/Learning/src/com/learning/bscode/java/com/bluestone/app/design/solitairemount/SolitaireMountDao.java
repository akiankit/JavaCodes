package com.bluestone.app.design.solitairemount;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.core.util.DataSheetConstants;
import com.bluestone.app.design.model.Customization;

@Repository
public class SolitaireMountDao extends BaseDao {
	
	private static final String SOLITAIRE_CATEGORY_TYPE_PARAM_NAME = "solitaireCategoryType";
    
    protected static final String SOLITAIRE_MOUNT_QUERY = " select c from Customization c " 
                                                                + " join c.design d "
                                                                + " join d.designCategory dc" 
                                                                + " join c.customizationStoneSpecification css"
                                                                + " join css.stoneSpecification ss"
                                                                + " where dc.categoryType=:solitaireCategoryType"
                                                                + " and ss.shape=:shape"
                                                                + " and :carat >= ss.minCarat and :carat <= ss.maxCarat"
                                                                + " and  c.priority=:priority";
    
    private String minMaxCaratQueryForDesignCategory = "select min(ss.minCarat), max(ss.maxCarat) from  design d " +
            " inner join design_category dc on (dc.id=d.design_category_id)" +
            " inner join customization c on(c.design_id=d.id) " +
            " inner join product p on(c.id=p.id) " +
            " inner join customization_stone_specification css on(css.customization_id=c.id)" +
            " inner join stone_specification ss on (css.stone_specification_id=ss.id)" +
            " where dc.category_type=:solitaireCategoryType" + 
            " and c.priority=:priority and p.is_active=1 ";
    
    private static final Logger   log                   = LoggerFactory.getLogger(SolitaireMountDao.class);

    //TODO pagination and sorting caching
    public List<Customization> getSolitaireRingList(BigDecimal carat,String shape) {
        Query createQuery = getEntityManagerForActiveEntities().createQuery(SOLITAIRE_MOUNT_QUERY);
        createQuery.setParameter(SOLITAIRE_CATEGORY_TYPE_PARAM_NAME, DataSheetConstants.DESIGN_CATEGORYTYPE.SOLITAIRE_RING_MOUNT.getName());
        setQueryParams(carat,shape, createQuery);
        return createQuery.getResultList();
    }

    private void setQueryParams(BigDecimal carat, String shape, Query createQuery) {
        createQuery.setParameter("priority", 1);
        createQuery.setParameter("carat", carat);
        createQuery.setParameter("shape", shape);
    }
    
    public List<Customization> getSolitairePendantList(BigDecimal carat,String shape) {
        Query createQuery = getEntityManagerForActiveEntities().createQuery(SOLITAIRE_MOUNT_QUERY);
        createQuery.setParameter(SOLITAIRE_CATEGORY_TYPE_PARAM_NAME, DataSheetConstants.DESIGN_CATEGORYTYPE.SOLITAIRE_PENDANT_MOUNT.getName());
        setQueryParams(carat, shape, createQuery);
        return createQuery.getResultList();
    }
    
    public BigDecimal[] getMinMaxCaratValues(String designCategory) {
        BigDecimal[] minAndMaxArray = new BigDecimal[2];
        Query minMaxQuery = getEntityManagerForActiveEntities().createNativeQuery(minMaxCaratQueryForDesignCategory);
        
        String solitaireRingMountCategory = DataSheetConstants.DESIGN_CATEGORYTYPE.SOLITAIRE_RING_MOUNT.getName();
        String solitairePendantMountCategory = DataSheetConstants.DESIGN_CATEGORYTYPE.SOLITAIRE_PENDANT_MOUNT.getName();
        
        if(solitaireRingMountCategory.contains(designCategory)) {
            minMaxQuery.setParameter(SOLITAIRE_CATEGORY_TYPE_PARAM_NAME, solitaireRingMountCategory);
        } else if(solitairePendantMountCategory.contains(designCategory)) {
            minMaxQuery.setParameter(SOLITAIRE_CATEGORY_TYPE_PARAM_NAME, solitairePendantMountCategory);
        } else {
            return null;
        }
        minMaxQuery.setParameter("priority", 1);
        Object[] resultArray = (Object[]) minMaxQuery.getSingleResult();
        
        Object minPrice = resultArray[0];
        if (minPrice != null) {
            minAndMaxArray[0] = ((BigDecimal) minPrice);
        } else {
            minAndMaxArray[0] = new BigDecimal(0);
        }
        
        Object maxPrice = resultArray[1];
        if (maxPrice != null) {
            minAndMaxArray[1] = ((BigDecimal) maxPrice);
        } else {
            minAndMaxArray[1] = new BigDecimal(0);
        }
        return minAndMaxArray;
    }
    
}
