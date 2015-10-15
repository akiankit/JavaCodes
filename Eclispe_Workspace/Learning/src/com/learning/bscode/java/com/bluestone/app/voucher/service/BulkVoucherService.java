package com.bluestone.app.voucher.service;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import au.com.bytecode.opencsv.CSVWriter;

import com.bluestone.app.core.util.DateTimeUtil;
import com.bluestone.app.core.util.FileUtil;
import com.bluestone.app.voucher.dao.VoucherDao;
import com.bluestone.app.voucher.model.DiscountVoucher;
import com.google.common.util.concurrent.ExecutionError;
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class BulkVoucherService {
	private static final Logger log = LoggerFactory.getLogger(BulkVoucherService.class);
	 @Autowired
	 private VoucherDao voucherDao;
	 
	 @Autowired
	private VoucherService voucherService;
	 
	 public List<String> validateBulkVoucherForSave(DiscountVoucher voucher,int length,int noOfVouchers,String prefix) {
	    	List<String> errors = new ArrayList<String>();
	    	errors.addAll(voucherService.validateVoucherForSave(voucher));
	    	if(noOfVouchers<=0){
	    		errors.add("No of voucher should be greater than 0 .");
	    	}
	    	if(length <= prefix.length()){
	    		errors.add("Voucher code length should be greater than prefix length.");
	    	}
	    	if(prefix.length()<=0){
	    		errors.add("Prefix for voucher is mandatory");
	    	}
	    	if(length <=0){
	    		errors.add("Voucher length is mandatory");
	    	}
	    	return errors;
	    }
	 
	  @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	    public List<DiscountVoucher> createBulkVoucher(DiscountVoucher voucher,int length,int noOfVouchers,String prefix) throws Exception {
	    	 List<String> vouchers = new ArrayList<String>();
	    	 List<DiscountVoucher> createdVouchers = new ArrayList<DiscountVoucher>();
	    	 int randomLength = length-prefix.length();
	    	 String prefixIUperCase = prefix.toUpperCase();
	    	 try{
		    	 for(int i=0;i<noOfVouchers;i++){
					String discountVoucherName = prefixIUperCase +  RandomStringUtils.randomAlphanumeric(randomLength).toUpperCase();
		    		 if (vouchers.contains(discountVoucherName)) {
		    			 	throw new Exception("Duplicate voucher code generated. Please try again");
		    	        } else {
		    	        	vouchers.add(discountVoucherName);
		    	        	DiscountVoucher discountVoucher = getDiscountVoucher(voucher, discountVoucherName);
		    	        	voucherDao.create(discountVoucher);
		    	        	createdVouchers.add(discountVoucher);
		    	        }
		    	 }
	    	 }finally{
	    		 vouchers = null;
	    	 }
	       return createdVouchers; 
	    }

		private DiscountVoucher getDiscountVoucher(DiscountVoucher voucher, String discountVoucherName) {
			DiscountVoucher discountVoucher = new DiscountVoucher();
			discountVoucher.setName(discountVoucherName);
			discountVoucher.setDescription(voucher.getDescription());
			discountVoucher.setMaxAllowed(voucher.getMaxAllowed());
			discountVoucher.setMinimumAmount(voucher.getMinimumAmount());
			discountVoucher.setValidFrom(voucher.getValidFrom());
			discountVoucher.setValidTo(voucher.getValidTo());
			discountVoucher.setValue(voucher.getValue());
			discountVoucher.setVoucherType(voucher.getVoucherType());
			discountVoucher.setIsActive(true);
			return discountVoucher;
		}
		
		  public String writeVochersToFile(List<DiscountVoucher> createdVouchers) throws Exception{

		        StringWriter stringWriter = null;
		        CSVWriter csvWriter = null;
		        File file = null;
		        try {
		            stringWriter = new StringWriter();
		            csvWriter = new CSVWriter(stringWriter);
		            String temp[] = {"Name","Description","Max Allowed","Minimum Amount","Value","Voucher Type","Valid From","Valid To"};
		            csvWriter.writeNext(temp);
		            for (DiscountVoucher voucher : createdVouchers) {
		            	temp[0] = voucher.getName();
						temp[1] = voucher.getDescription();
						temp[2] =String.valueOf(voucher.getMaxAllowed());
						temp[3] = voucher.getMinimumAmount().toString();
						temp[4]  = voucher.getValue().toString();
						temp[5] = voucher.getVoucherType().toString();
						temp[6] = DateTimeUtil.formatDate(voucher.getValidFrom());
						temp[7] = DateTimeUtil.formatDate(voucher.getValidTo());
						csvWriter.writeNext(temp);
					}
		            csvWriter.flush();
		            return stringWriter.getBuffer().toString();
		        } catch (Exception exception) {
		            log.error("Error while writing vouchers {}", (file != null ? file.getAbsolutePath() : ""), exception);
		            throw exception;
		        } finally {
		            FileUtil.closeStream(csvWriter);
		            FileUtil.closeStream(stringWriter);
		        }
		    
		    }
}
