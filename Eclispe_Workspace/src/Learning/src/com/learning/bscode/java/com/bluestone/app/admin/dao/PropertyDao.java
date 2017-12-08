package com.bluestone.app.admin.dao;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.admin.model.Property;
import com.bluestone.app.core.dao.BaseDao;

@Repository("PropertyDao")
public class PropertyDao extends BaseDao{

	private static final Logger log = LoggerFactory.getLogger(PropertyDao.class);
	
	public List<Property> getPropertyList() {
		Query createQuery = getEntityManagerForActiveEntities().createQuery("select p from Property p");
		List<Property> list = createQuery.getResultList();
		return list;
	}
	
	
}

