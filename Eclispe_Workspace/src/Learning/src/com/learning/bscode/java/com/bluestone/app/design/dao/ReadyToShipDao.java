package com.bluestone.app.design.dao;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.design.model.ReadyToShip;

@Repository("readyToShipDao")
public class ReadyToShipDao extends BaseDao{

	public List<ReadyToShip> getReadyToShipProductsSkus(){
		Query query = getEntityManagerForActiveEntities().createQuery("select p from ReadyToShip p order by p.skuCode");
		return query.getResultList();
	}
	
	public ReadyToShip getReadyToShipBySkuCode(String skuCode){
		Query query = getEntityManagerForActiveEntities().createQuery("select p from ReadyToShip p where p.skuCode = :skuCode");
		query.setParameter("skuCode", skuCode);
		List<ReadyToShip> list = (List<ReadyToShip>)query.getResultList();
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public Map<Object, Object> getReadyToShipListAndTotalCount(int start, ListFilterCriteria filterCriteria) {
		EntityManager entityManager = getEntityManagerForActiveEntities();
		
		List<ReadyToShip> readyToShipList = getReadyToShipList(start, filterCriteria, entityManager);
		int readyToShipCount = getReadyToShipCount(filterCriteria, entityManager);
		
		Map<Object, Object> carrierDetails = new HashMap<Object, Object>();

		carrierDetails.put("list", readyToShipList);
		carrierDetails.put("totalCount", readyToShipCount);
		return carrierDetails;
	}
	
	private List<ReadyToShip> getReadyToShipList(int start,ListFilterCriteria filterCriteria,EntityManager entityManager){
		Query readyToShipListQuery = entityManager.createQuery("select c from ReadyToShip c where c." + filterCriteria.getColumn()
															+ " like '%" + filterCriteria.getValueToSearch() + "%' order by " 
															+ filterCriteria.getSortBy() + " " + filterCriteria.getSortOrder());
		readyToShipListQuery.setFirstResult(start);
		readyToShipListQuery.setMaxResults(filterCriteria.getItemsPerPage());
		List<ReadyToShip> readyToShipsList = readyToShipListQuery.getResultList();
		return readyToShipsList;
	}
	
	private int getReadyToShipCount(ListFilterCriteria filterCriteria,EntityManager entityManager){
		Query readyToShipCountQuery = entityManager.createNativeQuery("select count(id) from ready_to_ship  where "
																		+ filterCriteria.getColumn() 
																		+ " like '%" 
																		+ filterCriteria.getValueToSearch() + "%'");
		
		int singleResult = ((BigInteger)readyToShipCountQuery.getSingleResult()).intValue();
		return singleResult;
	}
}
