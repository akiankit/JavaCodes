package com.bluestone.app.search.tag;
import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.search.tag.model.TagCategory;

@Repository("TagCategoryDao")
public class TagCategoryDao extends BaseDao {
    
	private static final Logger log = LoggerFactory.getLogger(TagCategoryDao.class);
	
	public List<TagCategory> getTagCategoryList(List <String> category) {
		Query createQuery = getEntityManagerForActiveEntities().createQuery("select t from TagCategory t where t.name in (:category)");
		createQuery.setParameter("category",category);
	    List<TagCategory> list = createQuery.getResultList();
	   
		return list;
	}
	
	public List<TagCategory> getTagCategoryAllList() {
		Query createQuery = getEntityManagerForActiveEntities().createQuery("select t from TagCategory t");
		List<TagCategory> list = createQuery.getResultList();
		return list;
	}
	
}
