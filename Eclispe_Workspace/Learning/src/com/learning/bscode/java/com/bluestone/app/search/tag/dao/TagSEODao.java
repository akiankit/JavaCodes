package com.bluestone.app.search.tag.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.search.tag.model.TagSEO;

@Repository("TagSEODao")
public class TagSEODao extends BaseDao {
    
    private static final Logger log = LoggerFactory.getLogger(TagSEODao.class);
        
    public Map<Object, Object> getTagSEOsAndTotalCount(int start, ListFilterCriteria filterCriteria,boolean forActive) {
        EntityManager entityManager = forActive == true ? getEntityManagerForActiveEntities() : getEntityManagerForNonActiveEntities();
        Query tagseoCountQuery;
        Query tagseoListQuery;
        String column = filterCriteria.getColumn();
        String sortBy = filterCriteria.getSortBy();
        if (column.equalsIgnoreCase("tag.name") && sortBy.equalsIgnoreCase("tag.name")) {
        	tagseoListQuery = entityManager.createQuery("select u from TagSEO u join u.tags tag  " 
					+ "where tag.name like '%" + filterCriteria.getValueToSearch() + "%' " 
					+ "group by u order by " + sortBy + " " + filterCriteria.getSortOrder());

        	tagseoCountQuery = entityManager.createQuery("select count(u.id) from TagSEO u join u.tags tag"
					+ " where tag.name" + " like '%" + filterCriteria.getValueToSearch() + "%'");
        }else if (column.equalsIgnoreCase("tag.name") && !sortBy.equalsIgnoreCase("tag.name")) {
        	tagseoListQuery = entityManager.createQuery("select u from TagSEO u join u.tags tag  " 
								+ "where tag.name like '%" + filterCriteria.getValueToSearch() + "%' " 
								+ "group by u order by u." + sortBy + " " + filterCriteria.getSortOrder());
        	tagseoCountQuery = entityManager.createQuery("select count(u.id) from TagSEO u join u.tags tag " +
								"where tag.name" + " like '%" + filterCriteria.getValueToSearch() + "%'");
			
		} else if (!column.equalsIgnoreCase("tag.name") && sortBy.equalsIgnoreCase("tag.name")) {
			tagseoListQuery = entityManager.createQuery("select u from TagSEO u join u.tags tag  " +
								"where u." + column	+ " like '%" + filterCriteria.getValueToSearch() + "%' " +
								"group by u order by " + sortBy + " " + filterCriteria.getSortOrder());
			tagseoCountQuery = entityManager.createQuery("select count(u.id) from TagSEO u " +
								"where u." + column + " like '%" + filterCriteria.getValueToSearch() + "%'");
		}else{        	
        	tagseoCountQuery = entityManager.createQuery("select count(tagseo.id) from TagSEO tagseo where tagseo."
                    + filterCriteria.getColumn() + " like '%" + filterCriteria.getValueToSearch() + "%' " +
                        "order by "+ filterCriteria.getSortBy() +" "+ filterCriteria.getSortOrder());

        	tagseoListQuery = entityManager.createQuery("select tagseo from TagSEO tagseo where tagseo."+ filterCriteria.getColumn() + " like '%" + 
                    filterCriteria.getValueToSearch() + "%' order by "+ filterCriteria.getSortBy() +" "+ filterCriteria.getSortOrder());
        	
        }        
        tagseoListQuery.setFirstResult(start);
        tagseoListQuery.setMaxResults(filterCriteria.getItemsPerPage());

        List<TagSEO> tagseoList = tagseoListQuery.getResultList();
        Map<Object, Object> tagseoDetails = new HashMap<Object, Object>();

        tagseoDetails.put("list", tagseoList);
        tagseoDetails.put("totalCount", ((Long) tagseoCountQuery.getSingleResult()).intValue());
        return tagseoDetails;
    }        
    
}
