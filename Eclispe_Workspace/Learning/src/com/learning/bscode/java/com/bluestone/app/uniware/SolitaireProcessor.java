package com.bluestone.app.uniware;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Rahul Agrawal
 *         Date: 2/20/13
 */
@Service
class SolitaireProcessor {

    private static final Logger log = LoggerFactory.getLogger(SolitaireProcessor.class);

    public static final String SOLITAIRE_FIXED_SKUCODE = "DUMMY_SKUCODE";

    @Autowired
    private UniwareItemService uniwareItemService;

    //@PostConstruct
    void createItemTypeSolitaire() {
        log.info("SolitaireProcessor.createItemTypeSolitaire()");
        //UniwareItemType solitaireDummyItem = createEditSolitaireDummyItem();
        //uniwareItemService.createEdit(solitaireDummyItem);
    }

    private UniwareItemType createEditSolitaireDummyItem() {
        UniwareItemType uniwareItemType = new UniwareItemType();
        uniwareItemType.setDescription("Dummy Item for Solitaire");
        uniwareItemType.setCategoryCode("solitaire");
        uniwareItemType.setHeight(1);
        uniwareItemType.setWidth(1);
        uniwareItemType.setImageURL("");
        uniwareItemType.setName("Standard Solitaire Item");
        uniwareItemType.setMRP(BigDecimal.valueOf(1));
        uniwareItemType.setItemSKU(SolitaireProcessor.SOLITAIRE_FIXED_SKUCODE);
        return uniwareItemType;
    }


}
