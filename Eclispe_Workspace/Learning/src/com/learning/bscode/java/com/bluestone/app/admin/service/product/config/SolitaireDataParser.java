package com.bluestone.app.admin.service.product.config;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.design.model.StoneSpecification;

/**
 * @author Rahul Agrawal
 *         Date: 3/26/13
 */
class SolitaireDataParser {

    private static final Logger log = LoggerFactory.getLogger(SolitaireDataParser.class);

    static void parse(Object dataFromSpreadSheet, StoneSpecification stoneSpecification) {
        if (dataFromSpreadSheet == null) {
            log.debug("SolitaireDataParser.parse(): No solitaire data to parse");
        } else {
            String minMaxRange = (String) dataFromSpreadSheet;
            log.info("SolitaireDataParser.parse(): [{}]", minMaxRange);
            String[] parts = StringUtils.split(minMaxRange, "-");

            if (parts == null || (parts.length != 2)) {
                throw new RuntimeException("Solitaire min max weight range in carats is not in correct format or is missing");
            }

            try {
                BigDecimal min = BigDecimal.valueOf(Double.parseDouble(parts[0].trim()));
                BigDecimal max = BigDecimal.valueOf(Double.parseDouble(parts[1].trim()));

                if (max.compareTo(min) < 0) {
                    throw new RuntimeException("Error in parsing the Solitaire min max weights. Max value should **not be less** than min value : "
                                               + minMaxRange);
                }
                stoneSpecification.setMinCarat(min);
                stoneSpecification.setMaxCarat(max);
                log.info("SolitaireDataParser.parse(): Min=[{}] Max=[{}]", min.toString(), max.toString());
            } catch (NumberFormatException e) {
                throw new RuntimeException("Solitaire min max weight could not be parse correctly from input=" + minMaxRange, e);
            }
        }
    }
}
