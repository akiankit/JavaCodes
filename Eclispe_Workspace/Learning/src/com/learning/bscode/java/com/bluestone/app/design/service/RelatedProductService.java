package com.bluestone.app.design.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.design.dao.CustomizationDao;
import com.bluestone.app.design.model.Customization;

@Service
@Transactional(readOnly=true)
public class RelatedProductService {
    
    private static final Logger log = LoggerFactory.getLogger(RelatedProductService.class);
    
    @Autowired
    private CustomizationDao     customizationDao;
    
    public List<Customization> getRelatedProductsByPrice(Customization customization) {
        List<Customization> relatedProductsByPrice = new ArrayList<Customization>();
        int noOfTries = 10;
        int noOfProducts = 4;
        BigDecimal price = customization.getPrice();
        BigDecimal priceDifference = price.divide(new BigDecimal(5));
        for (int i = 0; i < noOfTries; i++) {
            relatedProductsByPrice = customizationDao.getRelatedProductsByPrice(customization, priceDifference, noOfProducts);
            if (relatedProductsByPrice.size() != noOfProducts) {
                priceDifference = priceDifference.add(price.divide(new BigDecimal(5)));
                continue;
            } else {
                break;
            }
        }
        if (relatedProductsByPrice == null || relatedProductsByPrice.isEmpty()) {
            log.debug("Not able to find related products for customization=[{}] ", customization);
        }
        return relatedProductsByPrice;
    }
}

