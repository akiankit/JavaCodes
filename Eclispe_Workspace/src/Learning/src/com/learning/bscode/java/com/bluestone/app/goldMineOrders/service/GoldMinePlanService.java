package com.bluestone.app.goldMineOrders.service;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import au.com.bytecode.opencsv.CSVWriter;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.event.EventType;
import com.bluestone.app.core.event.RaiseEvent;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.CryptographyUtil;
import com.bluestone.app.core.util.DateTimeUtil;
import com.bluestone.app.core.util.FileUtil;
import com.bluestone.app.core.util.PaginationUtil;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.design.goldmine.GoldMineService;
import com.bluestone.app.goldMineOrders.dao.GoldMinePlanDao;
import com.bluestone.app.goldMineOrders.model.GoldMinePlan;
import com.bluestone.app.goldMineOrders.model.GoldMinePlan.GoldMinePlanStatus;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanPayment;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanPayment.GoldMinePlanPaymentStatus;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanSchemes;
import com.bluestone.app.order.service.BaseOrderService;
import com.bluestone.app.payment.service.PaymentMetaDataFactory;
import com.bluestone.app.payment.spi.model.MasterPayment;
import com.bluestone.app.payment.spi.model.PaymentMetaData;
import com.bluestone.app.payment.spi.model.PaymentTransaction;
import com.bluestone.app.payment.spi.model.PaymentTransaction.PaymentTransactionStatus;

@Service
@Transactional(readOnly=true)
public class GoldMinePlanService extends BaseOrderService {
    
	@Autowired
	private GoldMinePlanDao goldMinePlanDao;
	
	@Autowired
	private GoldMinePlanPaymentService goldMinePlanPaymentService;
	
	@Autowired
	private GoldMineService  goldMineService;

	private static final Logger log = LoggerFactory.getLogger(GoldMinePlanService.class);

	public List<GoldMinePlan> getAllGoldMinesForCustomer(Customer loggedInCustomer) {
		log.debug("GoldMinePlanService.getAllGoldMinesForCustomer() :loggedInCustomer {}", loggedInCustomer.getEmail());
		return goldMinePlanDao.getAllGoldMinesForCustomer(loggedInCustomer);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public GoldMinePlan cancelGoldMinePlan(GoldMinePlan goldMinePlan){
		log.debug("GoldMinePlanService.cancelGoldMinePlan() goldMinePlan {} ", goldMinePlan);
		goldMinePlan.setGoldMinePlanStatus(GoldMinePlanStatus.CANCELLED);
		return goldMinePlanDao.update(goldMinePlan);
	}

	public void updateGoldMinePlan(GoldMinePlan goldMinePlan) {
		log.debug("GoldMinePlanService.updateGoldMinePlan() goldMinePlan", goldMinePlan);
		goldMinePlanDao.update(goldMinePlan);
	}
	
	public GoldMinePlan getGoldMinePlanByCode(String goldMinePlanCode){
		GoldMinePlan goldMinePlan = goldMinePlanDao.getGoldMinePlanByCode(goldMinePlanCode);
		return goldMinePlan;
	} 

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @RaiseEvent(eventType = EventType.GOLD_MINE_PLAN_ENROLMENT)
	public GoldMinePlan enrollPlan(PaymentTransaction paymentTransaction, int term) {
		log.info("GoldMinePlanService.createOrder(): Begin for {}", paymentTransaction.toStringBrief());
        MasterPayment masterPayment = getMasterPayment(paymentTransaction);
    	Cart cartToBeUsed = getCartToBeUsed(paymentTransaction, masterPayment);
    	preOrderProcessing(masterPayment, cartToBeUsed);
    	GoldMinePlan goldMinePlan = processGoldMineOrder(paymentTransaction,cartToBeUsed, term, masterPayment);
    	
    	MessageContext messageContext = Util.getMessageContext();
        messageContext.put(MessageContext.GOLD_MINE_ENROLLMENT_ID, goldMinePlan.getId());
        log.info("GoldMinePlanService.createOrder(): *Successfully* created GoldMinePlan=[{}] for PaymentTransactionId={}", goldMinePlan.getId(), paymentTransaction.getId());
        
    	return goldMinePlan;
    }

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @RaiseEvent(eventType = EventType.GOLD_MINE_PLAN_PAYMENT)
	public GoldMinePlanPayment addPaymentToPlan(PaymentTransaction paymentTransaction, String paymentPlanId) {
		log.info("GoldMinePlanService.createOrder(): Begin for {}", paymentTransaction.toStringBrief());
        MasterPayment masterPayment = getMasterPayment(paymentTransaction);
    	Cart cartToBeUsed = getCartToBeUsed(paymentTransaction, masterPayment);
    	preOrderProcessing(masterPayment, cartToBeUsed);
		GoldMinePlanPayment goldMinePlanPayment = goldMinePlanPaymentService.getGoldMinePlanPaymentById(Long.valueOf(paymentPlanId));
		goldMinePlanPayment.setCart(cartToBeUsed);
		GoldMinePlanPaymentStatus goldMinePlanPaymentStatus = null;
		Date paymentDate = DateTimeUtil.getDateWithZeroTime(paymentTransaction.getCreatedAt());
		Date dueDate = DateTimeUtil.getDateWithZeroTime(goldMinePlanPayment.getDueDate());
		if(paymentDate.after(dueDate)){
			goldMinePlanPaymentStatus = GoldMinePlanPaymentStatus.LATE_PAYMENT;
		}
		else{
			goldMinePlanPaymentStatus = GoldMinePlanPaymentStatus.PAID;
		}
		goldMinePlanPayment.setGoldMinePlanPaymentStatus(goldMinePlanPaymentStatus);
		goldMinePlanPayment.setMasterPayment(masterPayment);
		goldMinePlanPayment.setCode(CryptographyUtil.encrypt(goldMinePlanPayment.getId() + "", Constants.ORDER_ID_PADDING));
		goldMinePlanPaymentService.update(goldMinePlanPayment);
		
		MessageContext messageContext = Util.getMessageContext();
        GoldMinePlan goldMinePlan = goldMinePlanPayment.getGoldMinePlan();
        messageContext.put(MessageContext.GOLD_MINE_ENROLLMENT_ID, goldMinePlan.getId());
        messageContext.put(MessageContext.GOLD_MINE_PAYMENT_ID, goldMinePlanPayment.getId());
        log.info("GoldMinePlanService.createOrder(): *Successfully* accepted installment for GoldMinePlan=[{}] for PaymentTransactionId={}", goldMinePlan.getId(), paymentTransaction.getId());
        
    	return goldMinePlanPayment;
    }
	
	private GoldMinePlan processGoldMineOrder(PaymentTransaction paymentTransaction,Cart cart, int term, MasterPayment masterPayment){
		log.debug("GoldMinePlanService.processGoldMineOrder() Begin for {}", paymentTransaction.toStringBrief());
		GoldMinePlan goldMinePlan = createNewPlan(cart, term);

		GoldMinePlanPayment firstGoldMinePlanPayment = goldMinePlanPaymentService.createGoldMinePayment(goldMinePlan,
		                                                                                                cart,
		                                                                                                GoldMinePlanPaymentStatus.PAID,
		                                                                                                Calendar.getInstance(),
		                                                                                                masterPayment);
        MessageContext messageContext = Util.getMessageContext();
        messageContext.put(MessageContext.GOLD_MINE_PAYMENT_ID, firstGoldMinePlanPayment.getId());
		
		for (int i = 1; i < term; i++) { 
			Calendar dueDate = Calendar.getInstance();
			dueDate.add(Calendar.MONTH, i);
			GoldMinePlanPaymentStatus goldMinePlanPaymentStatus;
			if (i < GoldMinePlanSchemes.getUserPayments(term)){
                goldMinePlanPaymentStatus = GoldMinePlanPaymentStatus.DUE;
            } else {
                goldMinePlanPaymentStatus = GoldMinePlanPaymentStatus.PLANNED_PAYMENT;
            }
			//While creating order cart and master payment needs to be set null for all the installments except first one.
			goldMinePlanPaymentService.createGoldMinePayment(goldMinePlan,
			                                                 null,
			                                                 goldMinePlanPaymentStatus,
			                                                 dueDate,
			                                                 null);
		}
		
		return goldMinePlan;
	}

    private GoldMinePlan createNewPlan(Cart cart, int term) {
        Date date = DateTimeUtil.getDate();
        GoldMinePlan goldMinePlan = new GoldMinePlan();
		goldMinePlan.setAmount(cart.getTotalPrice());
		goldMinePlan.setCustomer(cart.getCustomer());
		goldMinePlan.setEnrolledDate(date);
		String schemeName = GoldMinePlanSchemes.getSchemeName(term);
		goldMinePlan.setSchemeName(schemeName);
		goldMinePlan.setGoldMinePlanStatus(GoldMinePlanStatus.RUNNING);
		Calendar maturityDate = Calendar.getInstance();
        maturityDate.add(Calendar.MONTH, term);
        goldMinePlan.setMaturityDate(maturityDate.getTime());
        goldMinePlanDao.create(goldMinePlan);
        goldMinePlan.setCode(CryptographyUtil.encrypt(goldMinePlan.getId() + "", Constants.ORDER_ID_PADDING));
        goldMinePlan = goldMinePlanDao.update(goldMinePlan);
        return goldMinePlan;
    }

    public GoldMinePlan getGoldMinePlan(Long goldMineEnrollmentId) {
        return goldMinePlanDao.findAny(GoldMinePlan.class, goldMineEnrollmentId);
    }
    
    public Map<Object, Object> getGoldMinePlanDetails(ListFilterCriteria filterCriteria, Date fromDate, Date toDate) {
    	int start = (filterCriteria.getP() - 1) * filterCriteria.getItemsPerPage();
		Map<Object, Object> goldMinePlanListAndTotalCount = goldMinePlanDao.getGoldMinePlanListAndTotalCount(start,filterCriteria, fromDate,toDate,false);
		String baseURLForPagination = "/admin/goldmineorder/list";
		List<GoldMinePlan> goldMinePlanList = (List<GoldMinePlan>) goldMinePlanListAndTotalCount.get("list");
		Map<Object, Object> dataForView = PaginationUtil.generateDataForView(goldMinePlanListAndTotalCount, filterCriteria.getItemsPerPage(),
								  				 filterCriteria.getP(), start, baseURLForPagination,goldMinePlanList.size());
		Map<String,String> searchFieldMap = new HashMap<String, String>();
		searchFieldMap.put("Id", "id");
		searchFieldMap.put("Plan Id","code");
		//searchFieldMap.put("Scheme Name","schemeName");
		searchFieldMap.put("Customer Name","customer.userName");
		searchFieldMap.put("Amount","amount");
		dataForView.put("searchFieldMap", searchFieldMap);
		return dataForView;
    }

	public Map<Object, Object> getGoldMineOverDuePaymentDetails(ListFilterCriteria filterCriteria, Date fromDate,Date toDate) {
		int start = (filterCriteria.getP() - 1) * filterCriteria.getItemsPerPage();
		Map<Object, Object> overDuePaymentsListAndTotalCount = goldMinePlanDao.overDuePaymentsListAndTotalCount(start,filterCriteria, fromDate,toDate,false);
		String baseURLForPagination = "/admin/goldmineorder/overduepayments";
		List<GoldMinePlanPayment> overDuePaymentsList = (List<GoldMinePlanPayment>) overDuePaymentsListAndTotalCount.get("list");
		Map<Object, Object> dataForView = PaginationUtil.generateDataForView(overDuePaymentsListAndTotalCount, filterCriteria.getItemsPerPage(),
								  				 filterCriteria.getP(), start, baseURLForPagination,overDuePaymentsList.size());
		
		String[] searchFieldNames = {"Gold Mine Plan Code","Customer Name","Customer Email"};
		String[] searchFieldValues = {"goldMinePlan.code","goldMinePlan.customer.userName","goldMinePlan.customer.email"};
		
		
		String[] columnNames = {"Payment Id", "Due Date", "Amount", "Overdue Days", "Plan Code", "Customer Name",
								"Customer Email", "Enrollment Date"};
		String[] sortColumnNames = {"id", "dueDate", "goldMinePlan.amount", "dueDate", "goldMinePlan.code",
									"goldMinePlan.customer.userName", "goldMinePlan.customer.email", "createdAt"};
		
		Map<String,String> sortFieldMap = createMap(columnNames,sortColumnNames);
		Map<String,String> searchFieldMap = createMap(searchFieldNames, searchFieldValues);
		
		dataForView.put("searchFieldMap", searchFieldMap);
		dataForView.put("sortFieldMap", sortFieldMap);
		return dataForView;
	}
	
	private Map<String, String> createMap(String[] columnNames, String[] sortColumnNames) {
		Map<String,String> map = new LinkedHashMap<String, String>();
		if(columnNames!=null && sortColumnNames !=null){
			for (int i = 0; i < columnNames.length; i++) {
				map.put(columnNames[i], sortColumnNames[i]);
			}
		}
		return map;
	}

	public List<GoldMinePlanPayment> getOverDuePaymentList(ListFilterCriteria filterCriteria, Date fromDate,Date toDate){
		Map<Object, Object> overDuePaymentsListAndTotalCount = goldMinePlanDao.overDuePaymentsListAndTotalCount(0,filterCriteria, fromDate,toDate,true);
		List<GoldMinePlanPayment> overDuePaymentsList = (List<GoldMinePlanPayment>) overDuePaymentsListAndTotalCount.get("list");
		return overDuePaymentsList;
	}

	public String writeOverDuePaymentsAsCSV(List<GoldMinePlanPayment> overDuePayments) throws Exception {
        StringWriter stringWriter = null;
        CSVWriter csvWriter = null;
        try {
        	stringWriter = new StringWriter();
            csvWriter = new CSVWriter(stringWriter);
            String temp[] = {"Payment Id","Due Date","Amount","Status","OverDue Days","Plan Code","Customer Name","Customer Email","Enrollment Date","Contact Number"};
            csvWriter.writeNext(temp);
            if (overDuePayments !=null && overDuePayments.size() > 0) {
				for (GoldMinePlanPayment goldMinePlanPayment : overDuePayments) {
					Date dueDate = goldMinePlanPayment.getDueDate();
					GoldMinePlan goldMinePlan = goldMinePlanPayment.getGoldMinePlan();
					Customer customer = goldMinePlan.getCustomer();
					
					temp[0] = String.valueOf(goldMinePlanPayment.getId());
					temp[1] = DateTimeUtil.formatDate(dueDate);
					temp[2] = goldMinePlan.getAmount().toString();
					temp[3] = GoldMinePlanPaymentStatus.OVERDUE.toString();
					temp[4] = String.valueOf(DateTimeUtil.getNumberOfDays(dueDate));
					temp[5] = goldMinePlan.getCode();
					temp[6] = customer.getUserName();
					temp[7] = customer.getEmail();
					temp[8] = DateTimeUtil.formatDate(goldMinePlan.getEnrolledDate());
					temp[9] = customer.getCustomerPhone();
					csvWriter.writeNext(temp);
				}
			}
            csvWriter.flush();
            return stringWriter.toString();
        } catch (Exception exception) {
            log.error("Error while writing overduepayments to the csv ", exception);
            throw exception;
        } finally {
            FileUtil.closeStream(csvWriter);
            FileUtil.closeStream(stringWriter);
        }
	}
	
	public String writeGoldMineListAsCSV(List<GoldMinePlan> goldMinePlanList) throws Exception {
        StringWriter stringWriter = null;
        CSVWriter csvWriter = null;
        try {
        	stringWriter = new StringWriter();
            csvWriter = new CSVWriter(stringWriter);
            String temp[] = {"Id","Plan Code","Customer Name","Customer Email","Installment Amount","Cumulative Amount","Scheme","Enrollment Date","Status","Maturity Date"};
            csvWriter.writeNext(temp);
            long currentTimeMillis = System.currentTimeMillis();
			log.info("Start time-"+currentTimeMillis);
            if (goldMinePlanList !=null && goldMinePlanList.size() > 0) {
				for (GoldMinePlan plan : goldMinePlanList) {
					Date enrolledDate = plan.getEnrolledDate();
					
					temp[0] = String.valueOf(plan.getId());
					temp[1] = plan.getCode();
					Customer customer = plan.getCustomer();
					temp[2] = customer.getUserName();
					temp[3] = plan.getCustomer().getEmail();
					temp[4] = plan.getAmount().toString();
					BigDecimal paidCount = new BigDecimal(plan.getPaidPaymentsCount());
					BigDecimal amount = plan.getAmount();
					temp[5] = paidCount.multiply(amount).toString();
					temp[6] = plan.getSchemeName();
					temp[7] = DateTimeUtil.formatDate(enrolledDate);
					temp[8] = plan.getGoldMinePlanStatus().name();
					temp[9] = DateTimeUtil.formatDate(plan.getMaturityDate());
					
					csvWriter.writeNext(temp);
				}
			}
            csvWriter.flush();
            long currentTimeMillis2 = System.currentTimeMillis();
			log.info("Total time -"+(currentTimeMillis2-currentTimeMillis));
            return stringWriter.toString();
        } catch (Exception exception) {
            log.error("Error while writing overduepayments to the csv ", exception);
            throw exception;
        } finally {
            FileUtil.closeStream(csvWriter);
            FileUtil.closeStream(stringWriter);
        }
	}

	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public void mergeGoldMinePlans(GoldMinePlan primaryGoldMinePlan, GoldMinePlan secondaryGoldMinePlan) {
		GoldMinePlanPayment secondaryPlanPayment = secondaryGoldMinePlan.getGoldMinePlanPayments().get(0);
		GoldMinePlanPayment nextDuePlanPayment = primaryGoldMinePlan.getNextDuePlanPayment();
		if(nextDuePlanPayment != null){
			nextDuePlanPayment.setCart(secondaryPlanPayment.getCart());
			nextDuePlanPayment.setMasterPayment(secondaryPlanPayment.getMasterPayment());
			if(StringUtils.isBlank(nextDuePlanPayment.getCode())){
				String code = CryptographyUtil.encrypt(nextDuePlanPayment.getId() + "", Constants.ORDER_ID_PADDING);
				nextDuePlanPayment.setCode(code);
			}
			
			Date secondaryPlanPaidDate = secondaryPlanPayment.getCreatedAt();
			Date primaryPlanNextDueDate = nextDuePlanPayment.getDueDate();
			GoldMinePlanPaymentStatus paymentStatus = null;
			if(secondaryPlanPaidDate.after(primaryPlanNextDueDate)){
				paymentStatus = GoldMinePlanPaymentStatus.LATE_PAYMENT;
			}else{
				paymentStatus = GoldMinePlanPaymentStatus.PAID;
			}
			nextDuePlanPayment.setGoldMinePlanPaymentStatus(paymentStatus);
			secondaryGoldMinePlan.setGoldMinePlanStatus(GoldMinePlanStatus.CANCELLED);
			goldMinePlanDao.update(secondaryGoldMinePlan);
			goldMinePlanDao.update(nextDuePlanPayment);
		}
	}

	public List<String> validateBeforeMerge(GoldMinePlan primaryGoldMinePlan, GoldMinePlan secondaryGoldMinePlan,
											 String primaryPlanCode, String secondaryPlanCode) {
		List<String> errorsList = new ArrayList<String>();
		if (primaryGoldMinePlan == null) {
			errorsList.add("Plan not found for planId:" + primaryPlanCode+".");
		}
		if (secondaryGoldMinePlan == null) {
			errorsList.add("Plan not found for planId:" + secondaryPlanCode+".");
		} 
		else if (primaryGoldMinePlan != null && secondaryGoldMinePlan != null) {
			if(primaryGoldMinePlan.getGoldMinePlanStatus().equals(GoldMinePlanStatus.CANCELLED)){
				errorsList.add("Plan "+primaryPlanCode+" is already cancelled.");
			}
			if(secondaryGoldMinePlan.getGoldMinePlanStatus().equals(GoldMinePlanStatus.CANCELLED)){
				errorsList.add("Plan "+secondaryPlanCode+" is already cancelled.");
			}
			if(primaryPlanCode.equalsIgnoreCase(secondaryPlanCode)){
				errorsList.add("Both gold mine plan should be with different code.");
			}
			if (!primaryGoldMinePlan.getCustomer().getEmail().equalsIgnoreCase(secondaryGoldMinePlan.getCustomer().getEmail())) {
				errorsList.add("Both gold mine plan should belong to same customer.");
			}
			if (!primaryGoldMinePlan.getSchemeName().equalsIgnoreCase(secondaryGoldMinePlan.getSchemeName())) {
				errorsList.add("Both gold mine plan should be of same scheme.");
			}
			if (!primaryGoldMinePlan.getAmount().equals(secondaryGoldMinePlan.getAmount())) {
				errorsList.add("Both gold mine plan should be of same amount.");
			}
			if(!secondaryGoldMinePlan.getEnrolledDate().after(primaryGoldMinePlan.getEnrolledDate())){
				errorsList.add("Secondary plan should be enrolled after primary plan.");
			}
			if(primaryGoldMinePlan.getNextDuePlanPayment() == null){
				errorsList.add("No due payments left for plan Id "+primaryPlanCode);
			}
			if(secondaryGoldMinePlan.getPaidPaymentsCount() > 1){
				errorsList.add("Secondary plan should have only one paid payment.");
			}
		}
		return errorsList;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor=Exception.class)
	@RaiseEvent(eventType = EventType.GOLD_MINE_PLAN_PAYMENT)
	public String addGoldMinePaymentToPlan(GoldMinePlan goldMinePlan,String instrumentType,
											String userEmail,String comment, PaymentTransactionStatus paymentTransactionStatus, 
											Date paymentDate) throws Exception {

		Customer customer = goldMinePlan.getCustomer();
		Cart cart = cartService.createCart(Constants.CRM_VISITOR_ID, customer);

		long goldMineProductId = goldMineService.getGoldMineProduct(goldMinePlan.getAmount()).getId();
		cartService.addItemToCart(cart, goldMineProductId, 1);
		
		GoldMinePlanPayment nextDuePlanPayment = goldMinePlan.getNextDuePlanPayment();
		List<PaymentMetaData> paymentMetaDatas = new ArrayList<PaymentMetaData>();
        PaymentMetaDataFactory.addMetaData(PaymentMetaData.Name.PAYMENTPLAN_ID, String.valueOf(nextDuePlanPayment.getId()), paymentMetaDatas);
		if (StringUtils.isNotBlank(comment)) {
			comment = comment + ",user-"+userEmail;
			nextDuePlanPayment.setComment(comment);
		}
		PaymentTransaction paymentTransaction = paymentTransactionService.createPaymentTransaction(cart,instrumentType, "CRM_flow", paymentMetaDatas);
		log.info("GoldMinePlanService.addGoldMinePaymentToPlan(): Setting the payment transaction status to {},userEmail:{}",paymentTransactionStatus, userEmail);
		paymentTransaction.setPaymentTransactionStatus(paymentTransactionStatus);
		paymentTransaction.setCreatedAt(paymentDate);
		GoldMinePlanPayment planPayment = addPaymentToPlan(paymentTransaction,String.valueOf(nextDuePlanPayment.getId()));
		String receiptNumber = planPayment.getCode();
		return receiptNumber;
	}

	public List<GoldMinePlan> getGoldMinePlanList(ListFilterCriteria filterCriteria, Date fromDate, Date toDate,boolean export) {
		Map<Object, Object> planListAndTotalCount = goldMinePlanDao.getGoldMinePlanListAndTotalCount(0,filterCriteria, fromDate,toDate,true);
		List<GoldMinePlan> planList = (List<GoldMinePlan>) planListAndTotalCount.get("list");
		return planList;
	}

}
