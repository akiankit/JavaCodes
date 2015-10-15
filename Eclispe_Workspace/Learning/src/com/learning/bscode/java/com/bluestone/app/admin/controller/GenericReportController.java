package com.bluestone.app.admin.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.util.DownloadUtil;
import com.bluestone.app.core.util.FileUtil;
import com.bluestone.app.core.util.SessionUtils;
import com.bluestone.app.reporting.generic.GenericReportService;

@Controller
@RequestMapping(value = {"/admin/genericreport*"})
public class GenericReportController {
	private static final Logger log = LoggerFactory.getLogger(GenericReportController.class);
	
	@Autowired
	private GenericReportService genericReportService;
	
	@RequestMapping(value = "/form*", method = RequestMethod.GET)
	@RequiresPermissions("genericreport:view")
	public ModelAndView getQueryForm() {
		log.info("GenericReportController.getQueryForm() By User = "+ SessionUtils.getAuthenticatedUser().getEmail());
		Map<Object, Object> queryDetails = new HashMap<Object, Object>();
		queryDetails.put("pageTitle", "CSV Report");
		return new ModelAndView("csvReportForQuery","viewData",queryDetails);
	}
	
	@RequestMapping(value = "/get*", method = RequestMethod.POST)
	@RequiresPermissions("genericreport:view")
	public ModelAndView getCsvReport(
			@RequestParam(value = "query", required = false, defaultValue = "") String query,
			HttpServletRequest request,HttpServletResponse response){
		log.info("GenericReportController.getCsvReport() By user  = "+ SessionUtils.getAuthenticatedUser().getEmail());
		Map<Object, Object> queryDetails = new HashMap<Object, Object>();
		try {
			ListFilterCriteria listFilterCriteria = new ListFilterCriteria();
			listFilterCriteria.setQuery(query);
			File csvReport = genericReportService.getCSVReport(listFilterCriteria, null, null, new String[]{}, FileUtil.generateUniqueFileName("CsvReport", ".csv"));
			DownloadUtil.copyFileDataToHttpResponse(csvReport, response, "text/csv");
		} catch (Exception e) {
			String message = "Some problem occurred while saving voucher reason=("+e.getMessage()+")";
			queryDetails.put("errors", message);
			log.error("{} : Reason {} ", message, e.getMessage(), e);
		}
		queryDetails.put("pageTitle", "CSV Report");
		queryDetails.put("query",query);
		return new ModelAndView("csvReportForQuery","viewData",queryDetails);
	}
}
