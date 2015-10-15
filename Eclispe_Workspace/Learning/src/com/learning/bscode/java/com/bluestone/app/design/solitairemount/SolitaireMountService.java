package com.bluestone.app.design.solitairemount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluestone.app.design.model.Customization;

@Service
public class SolitaireMountService {
    
    private static final Logger log = LoggerFactory.getLogger(SolitaireMountService.class);
    
    @Autowired
    private SolitaireMountDao solitaireMountDao;

    public List<Customization> getSolitaireMountListing(String category, BigDecimal carat,String shape) {
        List<Customization> solitaireMountList = new ArrayList<Customization>();        
        if("Ring".equalsIgnoreCase(category)) {
            solitaireMountList = solitaireMountDao.getSolitaireRingList(carat, shape);
        } else if("Pendant".equalsIgnoreCase(category)) {
            solitaireMountList = solitaireMountDao.getSolitairePendantList(carat, shape);
        }
        return solitaireMountList;
    }
}
