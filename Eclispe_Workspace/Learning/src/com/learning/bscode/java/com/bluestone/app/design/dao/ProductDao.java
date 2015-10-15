package com.bluestone.app.design.dao;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.design.model.product.Product;

@Repository("productDao")
public class ProductDao extends BaseDao {
    
    private static final Logger log = LoggerFactory.getLogger(ProductDao.class);

	public List<Product> listAllJewelleryProduct() {
		Query query = getEntityManagerWithoutFilter().createNamedQuery("product.getAllOneTypeProducts");
		query.setParameter("productType", Product.PRODUCT_TYPE.JEWELLERY);
		return query.getResultList();
	}
}
