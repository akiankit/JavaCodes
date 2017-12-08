package com.bluestone.app.admin.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.bluestone.app.admin.model.Carrier;
import com.bluestone.app.admin.model.CarrierZipcode;
import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.dao.BaseDao;

@Repository("carrierDao")
public class CarrierDao extends BaseDao {
    
    public void disableCarrier(Long carrierId) {
        Carrier carrier = find(Carrier.class, carrierId, true);
        markEntityAsDisabled(carrier);
    }
    
    public void enableCarrier(Long carrierId) {
        Carrier carrier = find(Carrier.class, carrierId, false);
        markEntityAsEnabled(carrier);
    }
    
    public void deletePincodesForCarrier(Set<Long> carrierIds) {
        Query deletePincodesForCarrierQuery = getEntityManagerWithoutFilter().createNamedQuery("carrierZipcode.deleteByCarrierIdList");
        deletePincodesForCarrierQuery.setParameter("carrierIds", carrierIds);
        deletePincodesForCarrierQuery.executeUpdate();
    }
    
    public List<Carrier> getCarrierListByCarrierIds(Set<Long> carrierIds) {
        Query getCarrierListByCarrierIdsQuery = getEntityManagerWithoutFilter().createNamedQuery("carrier.getCarrierListByCarrierIdsQuery");
        getCarrierListByCarrierIdsQuery.setParameter("carrierIds", carrierIds);
        return getCarrierListByCarrierIdsQuery.getResultList();
    }
    
    public List<Carrier> getSelectedCarrierZipcodeListByPincode(String zipcode) {
        Query getCarrierZipListByPincodeQuery = getEntityManagerForActiveEntities().createNamedQuery("carrierZipcode.getSelectedCarrierZipcodeListByPincode");
        getCarrierZipListByPincodeQuery.setParameter("zipcode", zipcode);
        return getCarrierZipListByPincodeQuery.getResultList();
    }
    
    public List<Carrier> getUnselectedCarrierListByPincode(String zipcode) {
        Query getCarrierListByPincodeQuery = getEntityManagerForActiveEntities().createNamedQuery("carrierZipcode.getUnselectedCarrierListByPincode");
        getCarrierListByPincodeQuery.setParameter("zipcode", zipcode);
        return getCarrierListByPincodeQuery.getResultList();
    }
    
    public void deleteCarrierZipcodes(String zipcode) {
        Query deleteCarrierZipcodesByZipcodeQuery = getEntityManagerForActiveEntities().createNamedQuery("carrierZipcode.deleteCarrierZipcodesByZipcode");
        deleteCarrierZipcodesByZipcodeQuery.setParameter("zipcode", zipcode);
        deleteCarrierZipcodesByZipcodeQuery.executeUpdate();
    }
    
    public void disableCarrierZipcode(Long carrierZipcodeId) {
        CarrierZipcode carrierZipcode = find(CarrierZipcode.class, carrierZipcodeId, true);
        markEntityAsDisabled(carrierZipcode);
    }
    
    public List<Carrier> getApplicableCarrier(String zipcode, double cartTotal) {
        Query applicableCarrier = getEntityManagerForActiveEntities().createNamedQuery("carrier.getApplicableCarrier");
        applicableCarrier.setParameter("zipcode", zipcode);
        applicableCarrier.setParameter("cartTotal", cartTotal);
        return applicableCarrier.getResultList();
    }
    
    public List<Carrier> getApplicableCarrierForCOD(String zipcode, double cartTotal) {
        Query applicableCarrier = getEntityManagerForActiveEntities().createNamedQuery("carrier.getApplicableCarrierForCOD");
        applicableCarrier.setParameter("zipcode", zipcode);
        applicableCarrier.setParameter("cartTotal", cartTotal);
        return applicableCarrier.getResultList();
    }
    
    public List<Double> getMaxCODLimit(String zipcode, double cartTotal) {
        Query applicableCarrier = getEntityManagerForActiveEntities().createNamedQuery("carrier.getMaxCODLimit");
        applicableCarrier.setParameter("zipcode", zipcode);
        applicableCarrier.setParameter("cartTotal", cartTotal);
        return applicableCarrier.getResultList();
    }
    
    public List<Double> getMaxPrepaidLimit(String zipcode) {
        Query applicableCarrier = getEntityManagerForActiveEntities().createNamedQuery("carrier.getMaxPrepaidLimit");
        applicableCarrier.setParameter("zipcode", zipcode);
        return applicableCarrier.getResultList();
    }
    
    public List<Carrier> getCarrierList(boolean forActive){
    	EntityManager entityManager = forActive == true ? getEntityManagerForActiveEntities() : getEntityManagerForNonActiveEntities();
    	Query getAllCarrierListQuery = entityManager.createQuery("select c from Carrier c");
    	return (List<Carrier>)getAllCarrierListQuery.getResultList();
    }
    
	public Map<Object, Object> getCarrierListAndTotalCount(int start,ListFilterCriteria filterCriteria, boolean forActive) {

		EntityManager entityManager = forActive == true ? getEntityManagerForActiveEntities() : getEntityManagerForNonActiveEntities();
		Query carrierCountQuery = entityManager.createQuery("select count(c.id) from Carrier c where c."
															+ filterCriteria.getColumn() + " like '%" + filterCriteria.getValueToSearch() + "%'");

		Query carrierListQuery = entityManager.createQuery("select c from Carrier c where c." + filterCriteria.getColumn()
															+ " like '%" + filterCriteria.getValueToSearch() + "%' order by " 
															+ filterCriteria.getSortBy() + " " + filterCriteria.getSortOrder());
		carrierListQuery.setFirstResult(start);
		carrierListQuery.setMaxResults(filterCriteria.getItemsPerPage());

		List<Carrier> carriersList = carrierListQuery.getResultList();
		Map<Object, Object> carrierDetails = new HashMap<Object, Object>();

		carrierDetails.put("list", carriersList);
		carrierDetails.put("totalCount", ((Long) carrierCountQuery.getSingleResult()).intValue());
		return carrierDetails;
	}
	
	public Map<Object, Object> getPincodesListAndTotalCount(int start,ListFilterCriteria filterCriteria, boolean forActive) {

		EntityManager entityManager = forActive == true	? getEntityManagerForActiveEntities() : getEntityManagerForNonActiveEntities();
		Query pincodesCountQuery = entityManager.createQuery("select count(p.id) from CarrierZipcode p where p." + filterCriteria.getColumn()
															+ " like '%" + filterCriteria.getValueToSearch() + "%'");

		Query pincodesListQuery = entityManager.createQuery("select p from CarrierZipcode p where p." 
															+ filterCriteria.getColumn() + " like '%" + filterCriteria.getValueToSearch() 
															+ "%' order by " + filterCriteria.getSortBy() + " " + filterCriteria.getSortOrder());
		pincodesListQuery.setFirstResult(start);
		pincodesListQuery.setMaxResults(filterCriteria.getItemsPerPage());

		List<Carrier> pincodesList = pincodesListQuery.getResultList();
		Map<Object, Object> pincodeDetails = new HashMap<Object, Object>();

		pincodeDetails.put("list", pincodesList);
		pincodeDetails.put("totalCount", ((Long) pincodesCountQuery.getSingleResult()).intValue());
		return pincodeDetails;
	}
    
}
