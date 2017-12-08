package com.bluestone.app.admin.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.dao.BannerDao;
import com.bluestone.app.admin.model.Banner;
import com.bluestone.app.admin.model.Banner.PageTypeForBanner;
import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.util.PaginationUtil;
import com.googlecode.ehcache.annotations.Cacheable;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class BannerService {
	
	private static final Logger log = LoggerFactory.getLogger(BannerService.class);
	
	@Autowired
	private BannerDao bannerDao;

	
	public Map<Object, Object> getBannerDetails(ListFilterCriteria filterCriteria) {
		log.debug("BannerService.getBannerDetails() ");
		int start = (filterCriteria.getP() - 1) * filterCriteria.getItemsPerPage();
		Map<Object, Object> bannersListAndTotalCount = bannerDao.getBannerListAndTotalCount(start,filterCriteria);
		String baseURLForPagination = "/admin/banner/list";
		List<Banner> bannersList = (List<Banner>) bannersListAndTotalCount.get("list");
		Map<Object, Object> dataForView = PaginationUtil.generateDataForView(bannersListAndTotalCount, filterCriteria.getItemsPerPage(),
								  				 							filterCriteria.getP(), start, baseURLForPagination,bannersList.size());
		return dataForView;
	}
	
	public Banner find(long bannerId){
		log.debug("BannerService.find() bannerId {}", bannerId);
		return bannerDao.find(Banner.class, bannerId, true);
	}


	//@TriggersRemove(cacheName = {"bannerHtmlContentCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll=true)
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void create(Banner banner) {
		log.debug("BannerService.create()" );
		bannerDao.create(banner);
	}

	//@TriggersRemove(cacheName = {"bannerHtmlContentCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll=true)
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Banner update(Banner banner) {
		log.debug("BannerService.update() " );
		return bannerDao.update(banner);
	}
	
	@Cacheable(cacheName="bannerHtmlContentCache")
	public String getHtmlContent(PageTypeForBanner pageTypeForBanner){
		log.debug("BannerService.getHtmlContent() " );
		return bannerDao.getHtmlContent(pageTypeForBanner);
	}
	
}
