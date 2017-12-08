package com.bluestone.app.admin.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.account.model.User;
import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.DateTimeUtil;
import com.bluestone.app.core.util.DownloadUtil;
import com.bluestone.app.core.util.FileUtil;
import com.bluestone.app.core.util.SessionUtils;
import com.bluestone.app.goldMineOrders.model.GoldMinePlan;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanPayment;
import com.bluestone.app.goldMineOrders.service.GoldMinePlanService;
import com.bluestone.app.reporting.order.GoldMineReportService;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = { "/admin/goldmineorder/*" })
public class AdminGoldMinePlanController {
	private static final Logger log = LoggerFactory.getLogger(AdminGoldMinePlanController.class);

	@Autowired
	private GoldMinePlanService goldMinePlanService; 
	
	@Autowired
	private GoldMineReportService goldMineReportService;
	
    private final String OVERDUE_FILE_NAME_PREFIX = "OverDuePayments";
    
    private final String GOLD_MINE_PLAN_LIST_FILE_NAME_PREFIX = "GoldMinePlanList";
    
    private static final String FILE_EXTENSION_CSV = ".csv";

	private static final String[] columnNames = {"Id","Code","Customer Name","Customer Email","Installment Amount","SchemeName","Status","Enrollment Date","Maturity Date","Paid Amount"};


	@RequestMapping(value = "list", method = RequestMethod.GET)
	@RequiresPermissions("GoldMinePlan:view")
	public ModelAndView getGoldMinePlanListPage(@ModelAttribute("ListFilterCriteria") ListFilterCriteria filterCriteria,
												@RequestParam(defaultValue = "", required = false, value = "fromdate") String from,
												@RequestParam(defaultValue = "", required = false, value = "todate") String to, 
												HttpServletRequest request) {
		
        log.debug("AdminGoldMinePlanController.getGoldMinePlanListPage()");
		Map<Object, Object> goldMineOrderDetails = new HashMap<Object, Object>();
		try {
			Date toDate = DateTimeUtil.getDateFromString(to, false);
			Date fromDate = DateTimeUtil.getDateFromString(from, true);
			goldMineOrderDetails = goldMinePlanService.getGoldMinePlanDetails(filterCriteria, fromDate,toDate); 
			goldMineOrderDetails.put("requestURL", request.getRequestURL());
			goldMineOrderDetails.put("pageTitle","Gold Mine List");
		} catch (Exception e) {
			log.error("Error during AdminGoldMinePlanController.getGoldMinePlanListPage(): Reason: {}", e.toString(), e);
		}
		return new ModelAndView("goldMineOrders","viewData",goldMineOrderDetails);
	}
	
	@RequiresPermissions("GoldMinePlan:view")
	@RequestMapping(value = "list/export", method = RequestMethod.GET)
	public void exportGoldMinePlanList(@ModelAttribute("ListFilterCriteria") ListFilterCriteria filterCriteria,
										@RequestParam(defaultValue = "", required = false, value = "fromdate") String from,
										@RequestParam(defaultValue = "", required = false, value = "todate") String to, 
										HttpServletRequest request,HttpServletResponse response){
		User user = SessionUtils.getAuthenticatedUser();
		log.info("AdminGoldMinePlanController.exportGoldMinePlanList() ,userEmail:{}",user.getEmail());
		Date toDate = DateTimeUtil.getDateFromString(to, false);
		Date fromDate = DateTimeUtil.getDateFromString(from, true);
		try {
			String fileName = FileUtil.generateUniqueFileName(GOLD_MINE_PLAN_LIST_FILE_NAME_PREFIX, FILE_EXTENSION_CSV);
			File reportFile = goldMineReportService.getCSVReport(filterCriteria, fromDate, toDate, columnNames, fileName);
			DownloadUtil.copyFileDataToHttpResponse(reportFile, response, FILE_EXTENSION_CSV);
		} catch (Exception e) {
			log.error("Error occured while writing gold mine list to file",e);
		} 
	}
	
	@RequiresPermissions("GoldMinePlan:view")
	@RequestMapping(value = "overduepayments/list", method = RequestMethod.GET)
	public ModelAndView getOverDuePaymentsPage(@ModelAttribute("ListFilterCriteria") ListFilterCriteria filterCriteria,
												@RequestParam(defaultValue = "", required = false, value = "fromdate") String from,
												@RequestParam(defaultValue = "", required = false, value = "todate") String to, 
												HttpServletRequest request) {
		log.debug("AdminGoldMinePlanController.getOverDuePaymentsPage() ");
		Map<Object, Object> goldMineOverDuePaymentDetails = new HashMap<Object, Object>();
		try {
			Date toDate = DateTimeUtil.getDateFromString(to, false);
			Date fromDate = DateTimeUtil.getDateFromString(from, true);
			goldMineOverDuePaymentDetails = goldMinePlanService.getGoldMineOverDuePaymentDetails(filterCriteria, fromDate,toDate); 
			goldMineOverDuePaymentDetails.put("requestURL", request.getRequestURL());
			goldMineOverDuePaymentDetails.put("pageTitle","Gold Mine Over Due Payments List");
		} catch (Exception e) {
			log.error("Error during AdminGoldMinePlanController.getOverDuePaymentsPage: Reason:{}", e.toString(), e);
		}
		return new ModelAndView("goldMineOverDuePayments","viewData",goldMineOverDuePaymentDetails);
	}
	
	@RequiresPermissions("GoldMinePlan:view")
	@RequestMapping(value = "overduepayments/export", method = RequestMethod.GET)
	public void exportOverDuePayments(@ModelAttribute("ListFilterCriteria") ListFilterCriteria filterCriteria,
										@RequestParam(defaultValue = "", required = false, value = "fromdate") String from,
										@RequestParam(defaultValue = "", required = false, value = "todate") String to, 
										HttpServletRequest request,HttpServletResponse response){
		User user = SessionUtils.getAuthenticatedUser();
		log.debug("AdminGoldMinePlanController.exportOverDuePayments(),userEmail:{} ",user.getEmail());
		Date toDate = DateTimeUtil.getDateFromString(to, false);
		Date fromDate = DateTimeUtil.getDateFromString(from, true);
		try {
			List<GoldMinePlanPayment> overDuePaymentList = goldMinePlanService.getOverDuePaymentList(filterCriteria, fromDate, toDate);
			String csvData = goldMinePlanService.writeOverDuePaymentsAsCSV(overDuePaymentList);
			DownloadUtil.copyDataToHttpResponse(OVERDUE_FILE_NAME_PREFIX,response,"text/csv",csvData,".csv");
		} catch (Exception e) {
			log.error("Error occured while writing products score to file",e);
		} 
	}
	
	@RequestMapping(value = "details/{goldMineOrderId}", method = RequestMethod.GET)
	@RequiresPermissions("GoldMinePlan:view")
	public ModelAndView getGoldMinePlanDetailsPage(@PathVariable(value = "goldMineOrderId") long goldMineOrderId,
													@ModelAttribute("ListFilterCriteria") ListFilterCriteria filterCriteria, 
													HttpServletRequest request) {
        log.debug("AdminGoldMinePlanController.getGoldMinePlanDetailsPage()");
        Map<String,Object> viewData = new HashMap<String, Object>();
		GoldMinePlan goldMinePlan = goldMinePlanService.getGoldMinePlan(goldMineOrderId);
		try {
			viewData.put("goldMinePlan", goldMinePlan);
			viewData.put("requestURL", request.getRequestURL());
			viewData.put("pageTitle","Gold Mine Plan Details");
		} catch (Exception e) {
			log.error("Error during AdminGoldMinePlanController.getGoldMinePlanDetailsPage(): Reason: {}", e.toString(), e);
		}
		return new ModelAndView("goldMineOrderDetails","viewData",viewData);
	}
	
	@RequestMapping(value = "cancel/{goldMineOrderId}", method = RequestMethod.POST)
	@RequiresPermissions("GoldMinePlan:edit")
	public @ResponseBody String cancelGoldMinePlan(@PathVariable(value = "goldMineOrderId") long goldMineOrderId,HttpServletRequest request) {
		User user = SessionUtils.getAuthenticatedUser();
		log.info("AdminGoldMinePlanController.cancelGoldMinePlan() planId {},user {}",goldMineOrderId,user );
		boolean hasError = false;
		String message = "Plan cancelled successfully";
		String redirectUrl = request.getHeader("referer");
		try {
			GoldMinePlan goldMinePlan = goldMinePlanService.getGoldMinePlan(goldMineOrderId);
			goldMinePlanService.cancelGoldMinePlan(goldMinePlan);
			log.info("Gold Mine plan for planId:{} cancelled successfully.",goldMineOrderId);
		} catch (Exception e) {
			hasError = false;
			log.error("Error during AdminGoldMinePlanController.cancelGoldMinePlan: Reason:{}", e.toString(), e);
		}
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("redirectUrl", redirectUrl);
		response.put(Constants.MESSAGE, message);
		response.put(Constants.HAS_ERROR, hasError);
        return new Gson().toJson(response);
	}
	
	@RequestMapping(value = "/merge", method = RequestMethod.GET)
	@RequiresPermissions("GoldMinePlan:edit")
	public ModelAndView getGoldMineMergePage(HttpServletRequest request) {
        log.debug("AdminGoldMinePlanController.getGoldMinePlanDetailsPage() " );
        Map<String,Object> viewData = new HashMap<String, Object>();
        viewData.put("pageTitle","Merge Gold Mine Plans");
		return new ModelAndView("goldMinePlanMerge","viewData",viewData);
	}
	
	@RequestMapping(value = "/merge", method = RequestMethod.POST)
	@RequiresPermissions("GoldMinePlan:edit")
	public ModelAndView mergeTwoGoldMines(
						@RequestParam(defaultValue = "", required = false, value = "primaryPlanCode") String primaryPlanCode,
						@RequestParam(defaultValue = "", required = false, value = "secondaryPlanCode") String secondaryPlanCode,
						HttpServletRequest request) {
		User user = SessionUtils.getAuthenticatedUser();
		primaryPlanCode = primaryPlanCode !=null ? primaryPlanCode.trim() : primaryPlanCode;
		secondaryPlanCode  = secondaryPlanCode != null ? secondaryPlanCode.trim() :secondaryPlanCode; 
		log.info("AdminGoldMinePlanController.mergeTwoGoldMines() primaryPlanCode {}, secondaryPlanCode {},user {}",primaryPlanCode, secondaryPlanCode,user);
        Map<Object,Object> viewData = new HashMap<Object, Object>();
        GoldMinePlan primaryGoldMinePlan = goldMinePlanService.getGoldMinePlanByCode(primaryPlanCode);
		GoldMinePlan secondaryGoldMinePlan = goldMinePlanService.getGoldMinePlanByCode(secondaryPlanCode);
        List<String> errorsList = goldMinePlanService.validateBeforeMerge(primaryGoldMinePlan,secondaryGoldMinePlan,primaryPlanCode,secondaryPlanCode);
        if(errorsList.size() >0){
        	viewData.put("errors", errorsList);
        	viewData.put("primaryPlanCode", primaryPlanCode);
        	viewData.put("secondaryPlanCode", secondaryPlanCode);
        	log.info("Merge could not happen for Gold Mine primary plan:{}, secondary plan:{}, reason:{}",primaryPlanCode,secondaryPlanCode,errorsList);
        	return new ModelAndView("goldMinePlanMerge","viewData",viewData);
        }else{
        	log.info("Gold Mine plan primary:{} and secondary:{} merged successfully.",primaryPlanCode,secondaryPlanCode);
        	goldMinePlanService.mergeGoldMinePlans(primaryGoldMinePlan,secondaryGoldMinePlan);
        	String message ="Plan '"+primaryPlanCode+"' and '"+secondaryPlanCode+"'  merged successfully";
        	GoldMinePlan goldMinePlan = goldMinePlanService.getGoldMinePlanByCode(primaryPlanCode);
        	viewData.put("message", message);
			viewData.put("goldMinePlan", goldMinePlan);
			viewData.put("requestURL", request.getRequestURL());
    		return new ModelAndView("goldMineOrderDetails","viewData",viewData);
        }
	}
}
