package com.bluestone.app.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.dao.ReindexDao;
import com.bluestone.app.design.model.Customization;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.googlecode.ehcache.annotations.When;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ReindexService {

	private static final Logger log = LoggerFactory.getLogger(ReindexService.class);

	@Autowired
	private ReindexDao reindexDao;

    @TriggersRemove(cacheName = {"browsePageProductsCache", "filtersCache", "searchPageCustomizationCache", "seoCategoryListCache", "childTagsCache", "tagListByNames", "seoMinMaxPriceCache", "seoCountCache","tagSeoCache"} , when = When.AFTER_METHOD_INVOCATION, removeAll = true)
	public void reindex() {
		reindexDao.reindex();
	}

	public void reindex(Class clazz) {
		reindexDao.reindex(clazz);
	}

    @TriggersRemove(cacheName = {"browsePageProductsCache", "filtersCache", "searchPageCustomizationCache", "seoCategoryListCache", "childTagsCache", "tagListByNames", "seoMinMaxPriceCache", "seoCountCache","tagSeoCache"} , when = When.AFTER_METHOD_INVOCATION, removeAll = true)
	public void reindexProducts() {
		reindexDao.reindex(Customization.class);
	}

}
