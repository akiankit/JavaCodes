package com.bluestone.app.design.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.design.dao.ParentChildCategoryDao;
import com.bluestone.app.design.dao.ProductDao;
import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.shipping.service.HolidayService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private ParentChildCategoryDao parentChildCategoryDao;

    @Autowired
    private ReadyToShipService readyToShipService;

    @Autowired
    private ProductDao productDao;



    public boolean isReadyToShipItem(Product product) {
        String skuCode = product.getSkuCode();
        final boolean isReadyToShipItem = readyToShipService.isReadyToShipItem(skuCode);
        return isReadyToShipItem;
    }

    public String getFormattedExpectedDeliveryDate(Product product) {
        boolean readyToShipItem = isReadyToShipItem(product);
        String formattedDate;
        if (readyToShipItem) {
            formattedDate = readyToShipService.getFormattedDeliveryDate();
        } else {
            formattedDate = holidayService.getFormattedStandardDeliveryDate();
        }
        return formattedDate;
    }

    // checks if category of customization is a child category
    public boolean isCustomizationCategoryChild(Product product) {
        log.debug("ProductService.isCustomizationCategoryChild(): Customization Id={}", product.getSkuCode());
        String customizationCategoryType = product.getCategory();
        List<Object[]> childDesignCategories = parentChildCategoryDao.getChildDesignCategoriesIdAndName();
        for (Object[] childCategory : childDesignCategories) {
            String categoryName = childCategory[0].toString();
            log.debug("ProductService.isCustomizationCategoryChild():");
            if (categoryName.equalsIgnoreCase(customizationCategoryType)) {
                return true;
            }
        }
        return false;
    }

    public Product getProduct(Long productId) {
        return productDao.find(Product.class, productId, true);
    }
    
    public Product getProductWithoutActiveFilter(Long productId) {
        return productDao.findAny(Product.class, productId );
    }

    public List<String> getChildCategoryByParent(String parentCategoryType) {
        return parentChildCategoryDao.getChildCategoryByParent(parentCategoryType);
    }

    public Product updateProduct(Product product) {
        return productDao.update(product);
    }
    
    public List<Product> listAllJewelleryProduct(){
    	return productDao.listAllJewelleryProduct();
    }
}
