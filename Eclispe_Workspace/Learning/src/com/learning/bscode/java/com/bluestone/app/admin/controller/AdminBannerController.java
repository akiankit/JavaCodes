
package com.bluestone.app.admin.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.admin.model.Banner;
import com.bluestone.app.admin.model.Banner.PageTypeForBanner;
import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.admin.service.BannerService;
import com.bluestone.app.design.service.HomePageService;

@Controller
@RequestMapping(value = { "/admin/banner*" })
public class AdminBannerController {
	
	@Autowired
	private BannerService bannerService;
	
	@Autowired
    private HomePageService homePageService;

	private static final Logger log = LoggerFactory.getLogger(AdminBannerController.class);
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		log.trace("AdminBannerController.initBinder()");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	@RequiresPermissions("banner:view")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView getBannererListPage() {
        log.debug("AdminBannerController.getBannererListPage() ");
		Map<Object, Object> bannerDetails = new HashMap<Object, Object>();
		try {
			ListFilterCriteria filterCriteria = new ListFilterCriteria();  
			bannerDetails = bannerService.getBannerDetails(filterCriteria); 
		} catch (Exception e) {
			log.error("Error during AdminBannerController.getBannererListPage: Reason:{}", e.toString(), e);
		}
		bannerDetails.put("pageTitle", "Banners Management");
		return new ModelAndView("bannersList", "viewData", bannerDetails);
	}
	
	@RequiresPermissions("banner:add")
	@RequestMapping(value = "add", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView getBannerAddPage(@ModelAttribute("Banner") Banner banner) {
        log.debug("AdminBannerController.getBannererAddPage() ");
		Map<Object, Object> bannerDetails = new HashMap<Object, Object>();
		PageTypeForBanner[] pageTypes = PageTypeForBanner.values();
		if(banner.getPageType() != null){
			bannerDetails.put("banner",banner);
		}
		bannerDetails.put("pageTypes", pageTypes);
		bannerDetails.put("pageTitle", "Banners Management");
		return new ModelAndView("addUpdateBanner", "viewData", bannerDetails);
	}
	
	@RequiresPermissions("banner:edit")
	@RequestMapping(value = "edit/{bannerId}", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView getBannererEditPage(@PathVariable("bannerId") Long bannerId,@ModelAttribute("Banner") Banner banner) {
        log.debug("AdminBannerController.getBannererEditPage() bannerId {}", bannerId);
		Map<Object, Object> bannerDetails = new HashMap<Object, Object>();
		if(banner.getId() ==0){
			banner = bannerService.find(bannerId);
		}
		PageTypeForBanner[] pageTypes = PageTypeForBanner.values();
		bannerDetails.put("banner",banner);
		bannerDetails.put("pageTypes", pageTypes);
		bannerDetails.put("pageTitle", "Banners Management");
		return new ModelAndView("addUpdateBanner", "viewData", bannerDetails);
	}
	
	@RequiresPermissions(value={"banner:add","banner:edit"},logical=Logical.OR)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView saveBanner(@ModelAttribute("Banner") Banner banner) {
		log.debug("AdminBannerController.saveBanner() ");
		Map<Object, Object> bannerDetails = new HashMap<Object, Object>();
		String message ="Banner Updated Successfully";
		bannerDetails.put("pageTitle", "Banners Management");
		try{
			if(banner.getId()==0){
				bannerService.create(banner);
				message ="Banner created successfully";
			} else{
				bannerService.update(banner);
			}
		}catch(Exception e){
			log.error("Error during AdminBannerController.saveBanner: Reason:{}", e.toString(), e);
			message = "Some error occured while saving banner.";
			PageTypeForBanner[] pageTypes = PageTypeForBanner.values();
			bannerDetails.put("banner",banner);
			bannerDetails.put("pageTypes", pageTypes);
			bannerDetails.put("message", message);
			return new ModelAndView("addUpdateBanner", "viewData", bannerDetails);
		}
		
		ListFilterCriteria filterCriteria = new ListFilterCriteria();  
		bannerDetails = bannerService.getBannerDetails(filterCriteria);
		bannerDetails.put("message", message);
		return new ModelAndView("bannersList", "viewData", bannerDetails);
	}
	
	@RequiresPermissions("banner:view")
	@RequestMapping(value = "preview", method = RequestMethod.POST)
	public ModelAndView getBannererPreviewPage(@ModelAttribute("Banner") Banner banner,HttpServletRequest request) {
        log.debug("AdminBannerController.getBannererPreviewPage() ");
		String pageName = banner.getPageName();
		Map<String, Object> details = new HashMap<String, Object>();
		if(pageName.equalsIgnoreCase(PageTypeForBanner.HOME.name())){
			details = homePageService.getHomePageProducts();
			details.put("banner", banner);
			details.put("bannersHtml", banner.getHtmlContent());
			details.put("preview", true);
			details.put("pageType", "home");
			details.put("backUrl", request.getHeader("referer"));
		}
		return new ModelAndView(pageName, "home", details);
	}
}
