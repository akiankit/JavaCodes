package com.bluestone.app.design.solitaire;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.core.dao.BaseDao;

@Repository("solitaireDao")
public class SolitaireDao extends BaseDao {
    
    private static final Logger log = LoggerFactory.getLogger(SolitaireDao.class);
    
    private static String       minMaxQuery         = "select min($COLUMN_NAME), max($COLUMN_NAME) from solitaire s, product p where p.id = s.id and p.is_active = 1";
    
    public BigDecimal[] getMinAndMaxValues(String dbColumnName) {
        BigDecimal[] minAndMaxArray = new BigDecimal[2];
        String nativeQuery = minMaxQuery.replace("$COLUMN_NAME", dbColumnName);
        Query minMaxQuery = getEntityManagerForActiveEntities().createNativeQuery(nativeQuery);
        Object[] resultArray = (Object[]) minMaxQuery.getSingleResult();
        
        Object minPrice = resultArray[0];
        if (minPrice != null) {
            //BigDecimal roundedUpMinValue = ((BigDecimal) minPrice);
            minAndMaxArray[0] = ((BigDecimal) minPrice);
        } else {
            minAndMaxArray[0] = new BigDecimal(0);
        }
        
        Object maxPrice = resultArray[1];
        if (maxPrice != null) {
            //BigDecimal roundedUpMaxValue = ((BigDecimal) maxPrice);
            minAndMaxArray[1] = ((BigDecimal) maxPrice);
        } else {
            minAndMaxArray[0] = new BigDecimal(0);
        }
        return minAndMaxArray;
    }
    
    public long getSolitaireListCount(Map<String, List<String>> searchData, Map<String, Number[]> rangeData) {
        StringBuilder queryBuilder = new StringBuilder("Select count(solitaire) from Solitaire solitaire");
        buildSearchQuery(queryBuilder,rangeData, searchData);
        log.debug("Solitaire Count Query {}" , queryBuilder.toString());
        Query createQuery = getEntityManagerForActiveEntities().createQuery(queryBuilder.toString());
        Set<String> searchColumns = searchData.keySet();
        for (String eachColumn : searchColumns) {
            createQuery.setParameter(eachColumn, searchData.get(eachColumn));
        }
        try {
            return (Long) createQuery.getSingleResult();
        } catch (NoResultException e) {
            return 0l;
        }
    }
    
    public List<Solitaire> getSolitaireList(String offset, String limit, String sortColumnDbName, String sortColumnDirection, Map<String, List<String>> searchData, Map<String, Number[]> rangeData) {
        StringBuilder queryBuilder = new StringBuilder("Select solitaire from Solitaire solitaire");
        buildSearchQuery(queryBuilder, rangeData, searchData);
        queryBuilder.append(" order by ").append("solitaire.").append(sortColumnDbName);
        queryBuilder.append(" ").append(sortColumnDirection);
        log.debug("Solitaire Query {}" , queryBuilder.toString());
        Query createQuery = getEntityManagerForActiveEntities().createQuery(queryBuilder.toString());
        Set<String> searchColumns = searchData.keySet();
        for (String eachColumn : searchColumns) {
            createQuery.setParameter(eachColumn, searchData.get(eachColumn));
        }
        createQuery.setFirstResult(Integer.parseInt(offset));
        createQuery.setMaxResults(Integer.parseInt(limit));
        List resultList = createQuery.getResultList();
        return resultList;
    }

    private StringBuilder buildSearchQuery(StringBuilder queryBuilder, Map<String, Number[]> rangeData, Map<String, List<String>> searchData) {
        Set<String> searchColumns = searchData.keySet();
        Set<String> rangeColumns = rangeData.keySet();
        
        int i = 0;
        for (String eachColumnName : searchColumns) {
            if(i == 0) {
                queryBuilder.append(" where ");
            } else {
                queryBuilder.append(" and ");
            }
            queryBuilder.append(" solitaire.").append(eachColumnName).append(" IN (:").append(eachColumnName).append(")");
            i++;
        }
        
        int j = 0;
        for (String eachColumnName : rangeColumns) {
            if(j == 0 && i == 0) {
                queryBuilder.append(" where ");
            } else {
                queryBuilder.append(" and ");
            }
            Number[] numbers = rangeData.get(eachColumnName);
            queryBuilder.append(" solitaire.").append(eachColumnName).append(" >= ");
            queryBuilder.append(numbers[0]).append(" and ").append(" solitaire.").append(eachColumnName).append(" <= ").append(numbers[1]);
            j++;
        }
        return queryBuilder;
    }

	public Solitaire getByItemId(String itemId) {
		try {
			Query createNamedQuery = getEntityManagerForActiveEntities().createQuery("select solitaire from Solitaire solitaire where itemId = :itemId");
			createNamedQuery.setParameter("itemId", itemId);	                    
            Solitaire solitaire = (Solitaire) createNamedQuery.getSingleResult();
            return solitaire;
        } catch (NoResultException e) {
            return null;
        }		
	}
}
