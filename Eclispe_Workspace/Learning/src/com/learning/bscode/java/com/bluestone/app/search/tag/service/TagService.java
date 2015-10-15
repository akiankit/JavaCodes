package com.bluestone.app.search.tag.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.search.tag.TagDao;
import com.bluestone.app.search.tag.model.Tag;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class TagService{
	
	private static final Logger log  = LoggerFactory.getLogger(TagService.class);
	
	@Autowired
	private TagDao tagDao;
	
	public List<Tag> getAllTags() {
        return tagDao.getAllTags();
    }
	
	public <T> T findAny(Class<T> entityClass, long primaryKey) {
        return tagDao.findAny(entityClass, primaryKey);
    }
	
	public <T> T find(Class<T> entityClass, long primaryKey, boolean forActiveEntities) {
        return tagDao.find(entityClass, primaryKey, forActiveEntities);
    }

}
