package com.bluestone.app.admin.controller;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.account.model.User;
import com.bluestone.app.admin.model.OrderListFilterCriteria;
import com.bluestone.app.admin.service.AdminService;
import com.bluestone.app.core.util.DateTimeUtil;
import com.bluestone.app.core.util.DownloadUtil;
import com.bluestone.app.core.util.FileUtil;
import com.bluestone.app.core.util.PaginationUtil;
import com.bluestone.app.core.util.SessionUtils;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.service.OrderService;
import com.bluestone.app.reporting.order.OrderReport;
import com.bluestone.app.reporting.order.OrderReportService;

@Controller
@RequestMapping(value = "/admin/order/")
public class AdminOrderController {

	private static final String FILE_EXTENSION_CSV = ".csv";

	@Autowired
	private OrderService orderService;

	@Autowired
	private AdminService adminService;
	
	@Autowired
	private OrderReportService orderReportService;

	private static final Logger log = LoggerFactory.getLogger(AdminOrderController.class);
	
	private static final String[] columnNames = {"Id", "Code", "Customer Name", "Total", "Payment Gateway", "Payment Instrument", "Status",
												"Order creation Date", "Expected Delivery Date", "Voucher Name", "Discount Price", "Txn Id"};

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		log.trace("AdminOrderController.initBinder()");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	/*@RequestMapping(value = "orderstatus/list")
	@RequiresPermissions("orderstatus:view")
	public ModelAndView getOrderStatusList(
			@RequestParam(defaultValue = "1", required = false, value = "pagenumber") Long pageNumber,
			@RequestParam(defaultValue = "", required = false, value = "searchquery") String searchQuery,
			@RequestParam(defaultValue = "id", required = false, value = "sortby") String sortBy,
			@RequestParam(defaultValue = "false", required = false, value = "isDescending") String isDescending) {
		log.debug("AdminOrderController.getOrderStatusList()");
		Map<String, Object> viewData = new HashMap<String, Object>();
		pageNumber = pageNumber == null ? 1 : pageNumber;
		boolean descending = (StringUtils.isBlank(isDescending) ? false : Boolean.valueOf(isDescending));
		Map<String, Object> searchMap = baseService.search(OrderStatus.class, searchQuery,
				(int) ((pageNumber - 1) * adminItemsPerPage), adminItemsPerPage, sortBy, descending, true, 0);
		viewData.put("totalCount", searchMap.get("resultCount"));
		viewData.put("orderStatusList", searchMap.get("resultList"));
		viewData.put("pageTitle", "Order Status");
		return new ModelAndView("orderStatusList", "viewData", viewData);
	}

	@RequestMapping(value = "orderstatus/disabledlist", method = RequestMethod.GET)
	@RequiresPermissions("orderstatus:view")
	public ModelAndView getDisabledOrderStatusListPage(
			@RequestParam(defaultValue = "1", required = false, value = "pagenumber") Long pageNumber,
			@RequestParam(defaultValue = "", required = false, value = "searchquery") String searchQuery,
			@RequestParam(defaultValue = "id", required = false, value = "sortby") String sortBy,
			@RequestParam(defaultValue = "false", required = false, value = "isDescending") String isDescending) {
		log.debug("AdminOrderController.getCarrierDisabledListPage()");
		Map<String, Object> viewData = new HashMap<String, Object>();
		pageNumber = pageNumber == null ? 1 : pageNumber;
		boolean descending = (StringUtils.isBlank(isDescending) ? false : Boolean.valueOf(isDescending));
		Map<String, Object> searchMap = baseService.search(OrderStatus.class, searchQuery,
				(int) ((pageNumber - 1) * adminItemsPerPage), adminItemsPerPage, sortBy, descending, false, 0);
		viewData.put("totalCount", searchMap.get("resultCount"));
		viewData.put("orderStatusList", searchMap.get("resultList"));
		viewData.put(Constants.MESSAGE, "List of disabled statuses");
		viewData.put("isDisabledList", true);
		viewData.put("pageTitle", "Order Status");
		return new ModelAndView("orderStatusList", "viewData", viewData);
	}

	@RequestMapping(value = "orderstatus/add")
	@RequiresPermissions("orderstatus:add")
	public ModelAndView getOrderStatusAddPage(Model model) {
		log.debug("AdminOrderController.getOrderStatusAddPage()");
		Map<Object, Object> viewData = new HashMap<Object, Object>();
		model.addAttribute(new OrderStatus());
		viewData.put("pageTitle", "Order Status");
		return new ModelAndView("addUpdateOrderStatus");
	}

	@RequestMapping(value = "orderstatus/disable/{orderstatus_id}")
	@RequiresPermissions("orderstatus:delete")
	public ModelAndView disableOrderStatus(@PathVariable("orderstatus_id") String orderStatusId) {
		log.debug("AdminOrderController.disableOrderStatus()");
		Map<Object, Object> viewData = new HashMap<Object, Object>();
		OrderStatus orderStatus = adminService.getorderStatusById(Long.parseLong(orderStatusId), true);
		adminService.disableOrderStatus(orderStatus);
		Map<String, Object> searchMap = baseService.search(OrderStatus.class, "", 0, adminItemsPerPage, "", false,
				true, 0);
		viewData.put("orderStatusList", searchMap.get("resultList"));
		viewData.put(Constants.MESSAGE, "Disabled Successfully");
		viewData.put("pageTitle", "Order Status");
		return new ModelAndView("orderStatusList", "viewData", viewData);
	}

	@RequestMapping(value = "orderstatus/enable/{orderstatus_id}")
	@RequiresPermissions("orderstatus:edit")
	public ModelAndView enableOrderStatus(@PathVariable("orderstatus_id") String orderStatusId) {
		log.debug("AdminOrderController.enableOrderStatus()");
		Map<Object, Object> viewData = new HashMap<Object, Object>();
		OrderStatus orderStatus = adminService.getorderStatusById(Long.parseLong(orderStatusId), false);
		adminService.enable(orderStatus);
		Map<String, Object> searchMap = baseService.search(OrderStatus.class, "", 0, adminItemsPerPage, "", false,
				false, 0);
		viewData.put("orderStatusList", searchMap.get("resultList"));
		viewData.put(Constants.MESSAGE, "Enabled Successfully");
		viewData.put("isDisabledList", true);
		viewData.put("pageTitle", "Order Status");
		return new ModelAndView("orderStatusList", "viewData", viewData);

	}

	@RequestMapping(value = "orderstatus/edit/{orderstatus_id}")
	@RequiresPermissions("orderstatus:edit")
	public ModelAndView getOrderStatusEditPage(@PathVariable("orderstatus_id") String orderStatusId, Model model) {
		log.debug("AdminOrderController.getOrderStatusEditPage()");
		Map<Object, Object> viewData = new HashMap<Object, Object>();
		OrderStatus orderStatus = adminService.getorderStatusById(Long.parseLong(orderStatusId), true);
		model.addAttribute(orderStatus);
		viewData.put("pageTitle", "Order Status");
		return new ModelAndView("addUpdateOrderStatus");
	}

	@RequestMapping(value = "orderstatus/save")
	@RequiresPermissions("orderstatus:edit")
	public ModelAndView saveOrderStatus(@Valid OrderStatus orderStatus, BindingResult result) {
		log.debug("AdminOrderController.saveOrderStatus()");
		String message = "Order Status '" + orderStatus.getStatusName() + "' created successfully";
		Map<String, Object> viewData = new HashMap<String, Object>();
		if (orderStatus.getId() != 0) {
			adminService.updateOrderStatus(orderStatus);
			message = "Status updated to '" + orderStatus.getStatusName() + "' successfully";
		} else {
			adminService.createOrderStatus(orderStatus);
		}
		viewData.put(Constants.MESSAGE, message);
		Map<String, Object> searchMap = baseService.search(OrderStatus.class, "", 0, adminItemsPerPage, "", false,
				true, 0);
		viewData.put("orderStatusList", searchMap.get("resultList"));
		viewData.put("pageTitle", "Order Status");
		return new ModelAndView("orderStatusList", "viewData", viewData);
	}
	
	@RequestMapping(value = "changestatus/{orderid}", method = RequestMethod.POST)
	@RequiresPermissions("order:edit")
	public ModelAndView changeStatusForOrder(@PathVariable("orderid") String id_order,
			@RequestParam(value = "orderStatus") String newStatus) {
		log.debug("AdminOrderController.changeStatusForOrder() : Order Id={} , new Status={}", id_order, newStatus);
		Map<String, Object> viewData = new HashMap<String, Object>();
		Long orderId = Long.parseLong(id_order);
		Order order = orderService.getOrder(orderId);
		Customer customer = order.getCart().getCustomer();
		// updating the order status
        order = orderService.updateOrderStatus(orderId, newStatus, SessionUtils.getAuthenticatedUser().getEmail());

		List<Order> allOrdersForCustomer = orderService.getAllOrdersForCustomer(customer);
		Map<String, Object> searchMap = baseService.search(OrderStatus.class, "", 0, adminItemsPerPage, "", false,
				true, 0);
		List<OrderStatus> allOrderStatusList = (List<OrderStatus>) searchMap.get("resultList");
		Double totalPaid = new Double(0);
		for (Order eachOrder : allOrdersForCustomer) {
			totalPaid = totalPaid + eachOrder.getCart().getTotalPrice().doubleValue();
		}

		viewData.put("orderStatusList", allOrderStatusList);
		viewData.put("orderStatesHistory", orderService.getOrderStatesHistory(orderId));
		viewData.put("totalOrdersPlaced", allOrdersForCustomer.size());
		viewData.put("totalPaidSinceRegistration", totalPaid);
		viewData.put("subPaymentsMap", adminService.getAllLatestSubPaymentsForOrderList(allOrdersForCustomer));
		viewData.put("order", order);
		viewData.put(Constants.MESSAGE, "Status changed successfully");
		viewData.put("pageTitle", "Order Detail");
		return new ModelAndView("adminOrderDetails", "viewData", viewData);
	}*/

	@RequestMapping(value = "orderhistory", method = RequestMethod.GET)
	@RequiresPermissions("order:view")
	public ModelAndView getOrderHistory(
			@ModelAttribute("OrderListFilterCriteria") OrderListFilterCriteria filterCriteria,
			@RequestParam(defaultValue = "", required = false, value = "fromdate") String from,
			@RequestParam(defaultValue = "", required = false, value = "todate") String to,
			HttpServletRequest request) {
		log.debug("AdminOrderController.getOrderHistory()");

		Map<Object,Object> ordersDetails= new HashMap<Object, Object>();
		try {
			Date toDate = DateTimeUtil.getDateFromString(to, false);
			Date fromDate = DateTimeUtil.getDateFromString(from, true);
			int startingFrom = (filterCriteria.getP() - 1) * filterCriteria.getItemsPerPage();
			
			List<OrderReport> listOfRecords = orderReportService.getListOfRecords(filterCriteria, fromDate, toDate, startingFrom);
	        int countOfRecords = orderReportService.getCountOfRecords(filterCriteria, fromDate, toDate);
	        String baseURLForPagination = "/admin/order/orderhistory";
	        
	        ordersDetails = PaginationUtil.generateDataForView(countOfRecords, listOfRecords, filterCriteria,startingFrom, baseURLForPagination);
	        
			/*ordersDetails.put("sinceDaysMap", adminService.getWorkingDaysPassedForOrders(orderList));*/
			ordersDetails.put("totalValue", adminService.getTotalValueOfOrders(listOfRecords, false));
			ordersDetails.put("totalValidValue", adminService.getTotalValueOfOrders(listOfRecords, true));
			ordersDetails.put("requestURL", request.getRequestURL());
			
			Map<String, String> searchFieldMap = getSearchFieldMap();
	        ordersDetails.put("searchFieldMap", searchFieldMap);
	        ordersDetails.put("message", "Orders List");
	        ordersDetails.put("pageTitle", "Order History");
		} catch (Exception e) {
			log.error("Error during AdminOrderController.getOrderHistory(): Reason: {}", e.toString(), e);
		}
		return new ModelAndView("adminOrderList", "viewData", ordersDetails);
	}

    private Map<String, String> getSearchFieldMap() {
        Map<String,String> searchFieldMap = new HashMap<String, String>();
        searchFieldMap.put("Id", "orders.id");
        searchFieldMap.put("Order Id","orders.code");
        searchFieldMap.put("Customer","u.user_name");
        searchFieldMap.put("Voucher Name", "dv.name");
        searchFieldMap.put("Txn Id", "pt.bluestoneTxnId");
        return searchFieldMap;
    }
	
	@RequestMapping(value = "exportOrders", method = RequestMethod.GET)
	@RequiresPermissions("order:view")
	public void exportOrders(
			@ModelAttribute("OrderListFilterCriteria") OrderListFilterCriteria filterCriteria,
			@RequestParam(defaultValue = "", required = false, value = "fromdate") String from,
			@RequestParam(defaultValue = "", required = false, value = "todate") String to,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		User user = SessionUtils.getAuthenticatedUser();
		log.debug("AdminOrderController.exportOrders(),userEmail:{}",user.getEmail());
		Date toDate = DateTimeUtil.getDateFromString(to, false);
		Date fromDate = DateTimeUtil.getDateFromString(from, true);
		try {
			String fileName = FileUtil.generateUniqueFileName("Order_List", FILE_EXTENSION_CSV);
			File reportFile = orderReportService.getCSVReport(filterCriteria, fromDate, toDate,columnNames,fileName);
			DownloadUtil.copyFileDataToHttpResponse(reportFile, response, FILE_EXTENSION_CSV);
		} catch (Exception e) {
			log.error("Error during AdminOrderController.exportOrders: Reason:{}", e.toString(), e);
			throw new Exception();
		}
	}
	

	@RequestMapping(value = "orderdetail/{orderid}", method = RequestMethod.GET)
	@RequiresPermissions("order:view")
	public ModelAndView getOrderDetails(@PathVariable("orderid") String id_order, HttpServletRequest request) {
		log.info("AdminOrderController.getOrderDetails(): Order ID={}", id_order);
		Map<String, Object> viewData = new HashMap<String, Object>();
		Long orderId = Long.parseLong(id_order);
		String queryString = request.getQueryString();
		
		String requestURL = queryString != null ? queryString.substring(queryString.indexOf('=') + 1):"";
		try {
			orderService.syncWithUniwareStatus(orderId.toString());
            orderService.reComputeOrderStatus(orderId, SessionUtils.getAuthenticatedUser().getEmail());
		} catch (Exception exception) {
			log.error("AdminOrderController.getOrderDetails(): Error in updating the latest status from Uniware for OrderId={} . Showing whatever is in database ",
					orderId, exception);
		}

		Order order = orderService.getOrder(orderId);
		Customer customer = order.getCart().getCustomer();

		List<Order> allOrdersForCustomer = orderService.getAllOrdersForCustomer(customer);
		BigDecimal totalPaid = BigDecimal.ZERO;
		for (Order eachOrder : allOrdersForCustomer) {
		    if(!eachOrder.getLatestOrderStatus().getStatusName().equalsIgnoreCase("Cancelled"))
		        totalPaid = totalPaid.add(eachOrder.getMasterPayment().getLatestSubPayment().getPaymentTransaction().getAmount());
		}
		viewData.put("orderStatesHistory", orderService.getOrderStatesHistory(orderId));
		viewData.put("totalOrdersPlaced", allOrdersForCustomer.size());
		viewData.put("totalPaidSinceRegistration", totalPaid);
		viewData.put("order", order);
		viewData.put("pageTitle", "Order Detail");
		viewData.put("orderListURL", requestURL);
		return new ModelAndView("adminOrderDetails", "viewData", viewData);
	}

}
