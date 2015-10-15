package com.bluestone.app.admin.dao;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.admin.model.Banner;
import com.bluestone.app.admin.model.Banner.PageTypeForBanner;
import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.dao.BaseDao;

@Repository("bannerDao")
public class BannerDao extends BaseDao {
	
	private static final Logger log = LoggerFactory.getLogger(BannerDao.class);

	public Map<Object, Object> getBannerListAndTotalCount(int start, ListFilterCriteria filterCriteria) {
		log.debug("BannerDao.getBannerListAndTotalCount() " );
		EntityManager entityManager = getEntityManagerForActiveEntities() ;
		Query bannerCountQuery = entityManager.createNativeQuery("select count(*) from banner b where b."
															+ filterCriteria.getColumn() + " like '%" + filterCriteria.getValueToSearch() + "%'");

		Query bannerListQuery = entityManager.createQuery("select b from Banner b where b." + filterCriteria.getColumn()
															+ " like '%" + filterCriteria.getValueToSearch() + "%' order by " 
															+ filterCriteria.getSortBy() + " " + filterCriteria.getSortOrder());
		bannerListQuery.setFirstResult(start);
		bannerListQuery.setMaxResults(filterCriteria.getItemsPerPage());

		List<Banner> bannersList = bannerListQuery.getResultList();
		Map<Object, Object> bannerDetails = new HashMap<Object, Object>();

		bannerDetails.put("list", bannersList);
		bannerDetails.put("totalCount", ((BigInteger) bannerCountQuery.getSingleResult()).intValue());
		return bannerDetails;
	
	}

	public String getHtmlContent(PageTypeForBanner pageTypeForBanner) {
		log.debug("BannerDao.getHtmlContent() ");
		String htmlContent = "";
		EntityManager entityManager = getEntityManagerForActiveEntities() ;
		Query query = entityManager.createQuery("select c.htmlContent from Banner c where c.pageType=:pageType");
		query.setParameter("pageType", pageTypeForBanner);
		try{
			htmlContent = (String) query.getSingleResult();
		}catch(NoResultException e){
			log.error("Html content could not be found for pageType {}",pageTypeForBanner);
		}
		return htmlContent;
	}
	
}
