package com.bluestone.app.voucher.dao;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.voucher.model.DiscountVoucher;

@Repository("VoucherDao")
public class VoucherDao extends BaseDao{
	
	private static final Logger log = LoggerFactory.getLogger(VoucherDao.class);
		
	public DiscountVoucher getVoucher(Long voucherId) {
		return getEntityManagerWithoutFilter().find(DiscountVoucher.class, voucherId);
	}	
	
    public DiscountVoucher getVoucherByCode(String voucherCode) {
        log.debug("VoucherDao.getVoucherByCode({})", voucherCode);
        try {
            Query createNamedQuery = getEntityManagerWithoutFilter().createNamedQuery("Voucher.getVoucherByCode");
            createNamedQuery.setParameter("voucherCode", voucherCode);
            return (DiscountVoucher) createNamedQuery.getSingleResult();  
        } catch (NoResultException e) {
            return null;
        }
    }

	public int ordersCountForVoucher(DiscountVoucher voucher) {
		log.debug("VoucherDao.ordersCountForVoucher({})", voucher.getName());
		Query createNativeQuery = getEntityManagerWithoutFilter()
				.createNativeQuery(
						" select count(dv.id) from discountvoucher dv inner join cart c on (c.discountvoucher_id=dv.id) inner join orders o on (o.cart_id=c.id) where dv.name like '%"
								+ voucher.getName() + "%'");
		int count = ((BigInteger) createNativeQuery.getSingleResult()).intValue();
		return count;
	}
	
	public Map<Object, Object> getVouhersAndTotalCount(int start, ListFilterCriteria filterCriteria,boolean forActive) {
		
		EntityManager entityManager = forActive == true ? getEntityManagerForActiveEntities() : getEntityManagerForNonActiveEntities();
		Query voucherCountQuery = entityManager.createQuery("select count(v.id) from DiscountVoucher v where v."
															+ filterCriteria.getColumn() + " like '%" + filterCriteria.getValueToSearch() + "%' " +
																"order by "+ filterCriteria.getSortBy() +" "+ filterCriteria.getSortOrder());
		
		Query voucherListQuery = entityManager.createQuery("select v from DiscountVoucher v where v."+ filterCriteria.getColumn() + " like '%" + 
															filterCriteria.getValueToSearch() + "%' order by "+ filterCriteria.getSortBy() +" "+ filterCriteria.getSortOrder());
		voucherListQuery.setFirstResult(start);
		voucherListQuery.setMaxResults(filterCriteria.getItemsPerPage());

		List<DiscountVoucher> vouchersList = voucherListQuery.getResultList();
		Map<Object, Object> voucherDetails = new HashMap<Object, Object>();

		voucherDetails.put("list", vouchersList);
		voucherDetails.put("totalCount", ((Long) voucherCountQuery.getSingleResult()).intValue());
		return voucherDetails;
	}
}
