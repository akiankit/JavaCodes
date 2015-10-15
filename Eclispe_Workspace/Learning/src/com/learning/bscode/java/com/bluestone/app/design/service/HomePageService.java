package com.bluestone.app.design.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.model.Banner.PageTypeForBanner;
import com.bluestone.app.admin.model.Property;
import com.bluestone.app.admin.service.BannerService;
import com.bluestone.app.admin.service.PropertyService;
import com.bluestone.app.design.dao.CustomizationDao;
import com.bluestone.app.design.model.Customization;

/**
 * @author Rahul Agrawal
 *         Date: 2/25/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class HomePageService {

    private static final Logger log = LoggerFactory.getLogger(HomePageService.class);

    @Autowired
    private CustomizationDao customizationDao;

    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private BannerService bannerService;


    public Map<String, Object> getHomePageProducts() {
        log.debug("HomePageService.getHomePageProducts()");
        Map<String, Object> home = new HashMap<String, Object>();
        Subject subject = SecurityUtils.getSubject();
        log.debug("getHomePageProducts(): Get products");
        Session session = subject.getSession();
        List<Long> viewedList = (List<Long>) session.getAttribute("viewed");
        /*String[] ids = (Constants.MOST_POPULAR_PRODUCT_IDS).split(",");
        Long intarray[] = new Long[ids.length];
        for (int i = 0; i < ids.length; i++) {
            intarray[i] = (long) Integer.parseInt(ids[i]);
        }
        List<Long> list = Arrays.asList(intarray);
		*/
        Property property = propertyService.getPropertyByName("MOST_POPULAR_PRODUCT_IDS");
        if (property != null) {
            home.put("popular", customizationDao.getProductsList(property.getLongValues()));
        }
        if (viewedList != null && viewedList.size() > 0) {
            List<Customization> viewed = customizationDao.getProductsList(viewedList);
            Collections.reverse(viewed);
            home.put("viewed", viewed);
            home.put("viewedFlag", "true");
        } else {
            home.put("viewedFlag", "false");
        }
        log.debug("getHomePageProducts(): Return ");
        return home;
    }
    
    public String getBannersHtml(PageTypeForBanner pageTypeForBanner){
    	String htmlContent = bannerService.getHtmlContent(pageTypeForBanner);
    	return htmlContent;
    }
}
