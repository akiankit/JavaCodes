package com.bluestone.app.admin.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.dao.CarrierDao;
import com.bluestone.app.admin.model.Carrier;
import com.bluestone.app.admin.model.CarrierZipcode;
import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.util.PaginationUtil;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class CarrierService {

    private static final Logger log = LoggerFactory.getLogger(CarrierService.class);
    
    @Autowired
    private CarrierDao carrierDao;
    
    public Carrier getCarrierById(Long carrierId, boolean isActive) {
        return carrierDao.find(Carrier.class, carrierId, isActive);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void createCarrier(Carrier newCarrier) {
        carrierDao.create(newCarrier);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateCarrier(Carrier newCarrier) {
        carrierDao.update(newCarrier);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void disableCarrier(Long carrierId) {
        carrierDao.disableCarrier(carrierId);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void savePincodesForCarrier(String[] contentLines) {
        long prevCarrierIdValue = 0;
        Carrier carrierById = null;
        for (String contentLine : contentLines) {
            String carrierZipFields[] = contentLine.split(",");
            String carrierId = carrierZipFields[0];
            long carrierIdValue = Integer.parseInt(carrierId);
            String zipcode = carrierZipFields[1];
            String isCOD = carrierZipFields[2];
            short isCODValue = 0;
            if (isCOD.equalsIgnoreCase("Y")) {
                isCODValue = 1;
            }
            if (prevCarrierIdValue != carrierIdValue) {
                carrierById = getCarrierById(carrierIdValue, true);
                prevCarrierIdValue = carrierIdValue;
            }
            CarrierZipcode newCarrierZip = getNewCarrierZip(carrierById, zipcode, isCODValue);
            carrierDao.create(newCarrierZip);
        }
    }
    
    private CarrierZipcode getNewCarrierZip(Carrier carrierById, String zipcode, short isCODValue) {
        CarrierZipcode newCarrierZip = new CarrierZipcode();
        newCarrierZip.setCarrier(carrierById);
        newCarrierZip.setZipcode(zipcode);
        newCarrierZip.setIsCOD(isCODValue);
        return newCarrierZip;
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void refreshPincodesForCarrier(String[] contentLines, Set<Long> carrierIds) {
        carrierDao.deletePincodesForCarrier(carrierIds);
        savePincodesForCarrier(contentLines);
    }
    
    public String getFormattedErrorMessage(String errorMessage) {
        int startIndex = errorMessage.indexOf("Exception:") + "Exception".length();
        String formattedErrorMessage = errorMessage.substring(startIndex + 2, errorMessage.length());
        return formattedErrorMessage;
    }
    
    public String validateCarrierIds(Set<Long> carrierIds) {
        List<Carrier> carrierList = carrierDao.getCarrierListByCarrierIds(carrierIds);
        StringBuilder invalidCarrierMsg = new StringBuilder("There is no carrier for Carrier Id: ");
        if (carrierList.size() == carrierIds.size()) {
            return null;
        } else {
            int i = 0;
            Iterator<Long> carrierIdsIterator = carrierIds.iterator();
            while (carrierIdsIterator.hasNext()) {
                boolean found = false;
                long idToCheck = carrierIdsIterator.next();
                for (Carrier carrier : carrierList) {
                    if (found == false) {
                        if (carrier.getId() == idToCheck) {
                            found = true;
                        }
                    }
                }
                if (found == false && i == 0) {
                    invalidCarrierMsg.append(idToCheck);
                    i++;
                } else if (found == false) {
                    invalidCarrierMsg.append("," + idToCheck);
                }
            }
            return invalidCarrierMsg.toString();
        }
    }
    
    public Set<Long> getCarrierIdsFromFile(String[] contentLines) {
        Set<Long> carrierIds = new HashSet<Long>();
        for (String contentLine : contentLines) {
            String carrierZipFields[] = contentLine.split(",");
            String carrierId = carrierZipFields[0];
            carrierIds.add(Long.parseLong(carrierId));
        }
        return carrierIds;
    }
    
    public CarrierZipcode getCarrierZipcodeById(long carrierZipId, boolean isActive) {
        return carrierDao.find(CarrierZipcode.class, carrierZipId, isActive);
    }
    
    public List<Carrier> getSelectedCarrierZipcodeListByPincode(String zipcode) {
        return carrierDao.getSelectedCarrierZipcodeListByPincode(zipcode);
    }
    
    public Object getUnselectedCarrierListByPincode(String zipcode) {
        return carrierDao.getUnselectedCarrierListByPincode(zipcode);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveCarrierZipcode(String[] isCarrierSupported, String[] carrierIds, String[] isCodAllowed, String zipcode) {
        int loopCount = isCarrierSupported.length;
        for (int i = 0; i < loopCount; i++) {
            boolean isCarrierSupportedValue = Boolean.parseBoolean(isCarrierSupported[i]);
            if (!isCarrierSupportedValue) {
                continue;
            } else {
                Carrier carrier = getCarrierById(Long.parseLong(carrierIds[i]), true);
                boolean isCodAllowedValue = Boolean.parseBoolean(isCodAllowed[i]);
                CarrierZipcode newCarrierZipcode = new CarrierZipcode();
                newCarrierZipcode.setZipcode(zipcode);
                newCarrierZipcode.setCarrier(carrier);
                newCarrierZipcode.setIsCOD((short) (isCodAllowedValue == true ? 1 : 0));
                carrierDao.create(newCarrierZipcode);
            }
        }
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void deleteCarrierZipcodesByZipcode(String zipcode) {
        carrierDao.deleteCarrierZipcodes(zipcode);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void disableCarrierZipcodeByCarrierZipcodeId(Long carrierZipcodeId) {
        carrierDao.disableCarrierZipcode(carrierZipcodeId);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void enableCarrier(Long carrierId) {
        carrierDao.enableCarrier(carrierId);
    }
    
    public Carrier getApplicableCarrier(String zipcode, double cartTotal) {
        log.debug("CarrierService.getApplicableCarrier() for ZipCode={} , Amount={}", zipcode, cartTotal);
        List<Carrier> applicableCarrier = carrierDao.getApplicableCarrier(zipcode, cartTotal);
        if (!applicableCarrier.isEmpty()) {
            return applicableCarrier.get(0);
        }
        return null;
    }
    
    public Carrier getApplicableCarrierForCOD(String zipcode, double cartTotal) {
        List<Carrier> applicableCarrier = carrierDao.getApplicableCarrierForCOD(zipcode, cartTotal);
        if (!applicableCarrier.isEmpty()) {
            return applicableCarrier.get(0);
        }
        return null;
    }
    
    public Double getMaxCODLimit(String zipcode, double cartTotal) {
        List<Double> maxCODLimit = carrierDao.getMaxCODLimit(zipcode, cartTotal);
        if (!maxCODLimit.isEmpty()) {
            return maxCODLimit.get(0);
        }
        return null;
    }
    
    public Double getMaxPrepaidLimit(String zipcode) {
        List<Double> maxPrepaidLimit = carrierDao.getMaxPrepaidLimit(zipcode);
        if (!maxPrepaidLimit.isEmpty()) {
            return maxPrepaidLimit.get(0);
        }
        return null;
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void deleteCarrierZipcodeByCarrierZipcodeId(Long carrierZipcodeId) {
        carrierDao.remove(CarrierZipcode.class, carrierZipcodeId);
    }
    
	public Map<Object, Object> getCarrierDetails(ListFilterCriteria filterCriteria,boolean forActive) {
    	int start = (filterCriteria.getP() - 1) * filterCriteria.getItemsPerPage();
		Map<Object, Object> carriersListAndTotalCount = carrierDao.getCarrierListAndTotalCount(start,filterCriteria, forActive);
		String baseURLForPagination = forActive == true ? "/admin/carrier/list" : "/admin/carrier/disabledlist";
		List<Carrier> carriersList = (List<Carrier>) carriersListAndTotalCount.get("list");
		Map<Object, Object> dataForView = PaginationUtil.generateDataForView(carriersListAndTotalCount, filterCriteria.getItemsPerPage(),
								  				 filterCriteria.getP(), start, baseURLForPagination,carriersList.size());
		Map<String,String> searchFieldMap = new HashMap<String, String>();
		searchFieldMap.put("Id", "id");
		searchFieldMap.put("Company Name ","companyName");
		searchFieldMap.put("Account Number","accountNumber");
		searchFieldMap.put("codLimit", "codLimit");
		searchFieldMap.put("PrepaidLimit","prepaidLimit");
		searchFieldMap.put("Priority","priority");
		searchFieldMap.put("Url","url");
		
		dataForView.put("searchFieldMap", searchFieldMap);
		return dataForView;
    }
    
    
	
	public Map<Object, Object> getPincodeDetails(ListFilterCriteria filterCriteria ,boolean forActive) {
		int start = (filterCriteria.getP() - 1) * filterCriteria.getItemsPerPage();
		Map<Object, Object> pincodesListAndTotalCount = carrierDao.getPincodesListAndTotalCount(start,filterCriteria, forActive);
		String baseURLForPagination =  "/admin/carrier/pincodes/list";
		List<CarrierZipcode> pincodesList = (List<CarrierZipcode>) pincodesListAndTotalCount.get("list");
		Map<Object, Object> dataForView = PaginationUtil.generateDataForView(pincodesListAndTotalCount, filterCriteria.getItemsPerPage(),
						  													 filterCriteria.getP(), start, baseURLForPagination,pincodesList.size());
		Map<String,String> searchFieldMap = new HashMap<String, String>();
		searchFieldMap.put("Id", "id");
		searchFieldMap.put("Pincode ","zipcode");
		searchFieldMap.put("Carrier","carrier.id");
		dataForView.put("searchFieldMap", searchFieldMap);
		return dataForView;
	}

	public List<Carrier> getCarrierList(boolean forActive){
		return carrierDao.getCarrierList(forActive);
	}
}
