package com.bluestone.app.design.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.design.dao.DesignCategoryDao;
import com.bluestone.app.design.model.DesignCategory;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class DesignCategoryService {
	
	private static final Logger  log = LoggerFactory.getLogger(DesignCategoryService.class);
	
	@Autowired
	private DesignCategoryDao designCategoryDao;
	
	public List<DesignCategory> getDesignCategoryFromList(List<String> tags) {
        log.trace("DesignCategoryService.getDesignCategoryFromList() for {}", tags);        
        List<DesignCategory> designCategoryFromList = designCategoryDao.getDesignCategoryFromList(tags);
        return designCategoryFromList;
    }

}
