package com.bluestone.app.admin.dao;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.admin.model.TagFilter;
import com.bluestone.app.core.dao.BaseDao;

@Repository("FilterDao")
public class FilterDao extends BaseDao{

	private static final Logger log = LoggerFactory.getLogger(FilterDao.class);
	
	public List<TagFilter> getFilterList() {
		Query createQuery = getEntityManagerForActiveEntities().createQuery("select f from TagFilter f");
		List<TagFilter> list = createQuery.getResultList();
		return list;
	}
	
	
	/*public List<Tag> getTagList(String [] tagList) {
		Query createQuery = entityManager.createQuery("select t from Tag t where t.name in (:tags)");
		createQuery.setParameter("tags",tagList);
	    List<Tag> list = createQuery.getResultList();
	   
		return list;
	}*/

	
}

