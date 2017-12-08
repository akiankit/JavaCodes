package com.bluestone.app.pricing;

import java.math.BigDecimal;

import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.MetalSpecification;
import com.bluestone.app.design.model.StoneSpecification;

/**
 * @author Rahul Agrawal
 *         Date: 2/13/13
 */
public interface PricingEngine {

    public BigDecimal getStoneFamilyWeight(StoneSpecification stoneSpecification) throws Exception;

    public double getPrice(Customization customization) throws Exception;

    public double getSizePrice(MetalSpecification metalSpecification, BigDecimal metalWeight) throws Exception;

    public boolean checkAvailability(Customization customization) throws Exception;

}
