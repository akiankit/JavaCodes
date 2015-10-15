package com.bluestone.app.pricing;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.google.common.base.Throwables;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.MetalSpecification;
import com.bluestone.app.design.model.StoneSpecification;
import com.bluestone.app.integration.RestConnection;


@Service
public class PricingEngineService implements PricingEngine {

    private static final Logger log = LoggerFactory.getLogger(PricingEngineService.class);

    @Resource
    private RestConnection restConnection;

    @Value("${pricing-engine-url}")
    private String pricingEngineUrl;

    /**
     *
     * @param stoneSpecification
     * @return Weight of stone in Carats.
     * @throws Exception
     */
    public BigDecimal getStoneFamilyWeight(StoneSpecification stoneSpecification) throws Exception {
        log.info("**** PricingEngineService.getStoneFamilyWeight()\n");
        if(stoneSpecification.isSolitaire()) {
            log.info("Stone type is solitaire. Not invoking pricing engine to fetch stone weight for {} ", stoneSpecification);
            return BigDecimal.ZERO;
        }
        Map<String, Object> queryParams = new HashMap<String, Object>();
        final String stoneType = stoneSpecification.getStoneType();
        final String stoneSize = stoneSpecification.getSize();
        final String stoneShape = stoneSpecification.getShape();
        QueryParamBuilder.addStoneQueryParamForWeight(stoneSpecification, queryParams);
        String response = "";
        try {
            queryParams.put("action", "getstoneweight");
            response = getResponseFromPricingEngine(queryParams);
            DecimalFormat df = new DecimalFormat("#.###");
            String weightInCarats = df.format((Float.parseFloat(response) * 5)); // PE gives us in grams. 1gm=5carats.
            log.info("PricingEngineService.getStoneFamilyWeight():Converting grams to carats [{}] gm * [5]=[{}] carats.\n", response, weightInCarats);
            final BigDecimal bigDecimal = new BigDecimal(weightInCarats);
            return bigDecimal;
        } catch (NumberFormatException e) {
            log.error("Error in parsing the Stone Weight=[{}] (received from pricing engine) for stone type=[{}] size=[{}] cut=[{}]",
                      response, stoneType, stoneSize, stoneShape, e);
            throw Throwables.propagate(e);
        } catch (Exception e) {
            log.error("Error in fetching the stone weight from pricing engine for StoneType=[{}] Size=[{}] Cut=[{}]", stoneType, stoneSize, stoneShape, e);
            throw Throwables.propagate(e);
        }
    }

    public double getPrice(Customization customization)  {
        log.debug("PricingEngineService.getPrice() for CustomizationId=[{}] Sku=[{}]", customization.getId(), customization.getSkuCode());
        Map<String, Object> queryParams = buildRequiredQueryParams(customization);

        queryParams.put("action", "getprice");
        double price = 0.0d;

        String responseFromPricingEngine = null;
        try {
            responseFromPricingEngine = getResponseFromPricingEngine(queryParams);
        } catch (Exception exception) {
            throw new RuntimeException("Unable to get a response from pricing engine for CustomizationId=" + customization.getId()
                                       + " Sku=" + customization.getSkuCode(),
                                       Throwables.getRootCause(exception));
        }

        try {
            price = Double.parseDouble(responseFromPricingEngine);
            if (price == 0.0d) {
                throw new RuntimeException("Error:PricingEngineService.getPrice() for customization returned a value of Zero");
            }
        } catch (NumberFormatException numberFormatException) {
            log.error("Invalid Response=[{}] returned by the pricing engine.", responseFromPricingEngine, numberFormatException);
            throw new RuntimeException("Invalid Price/Response [" + responseFromPricingEngine + "] returned from pricing engine for CustomizationId=" + customization.getId()
                                       + " Sku=" + customization.getSkuCode(),
                                       numberFormatException);
        }
        return price;
    }

    public double getSizePrice(MetalSpecification metalSpecification, BigDecimal metalWeight) throws Exception {
        log.info("****PricingEngineService.getSizePrice()");
        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("metaltype0", metalSpecification.getType());
        queryParams.put("metalpurity0", metalSpecification.getPurity());
        queryParams.put("metalweight0", metalWeight);
        queryParams.put("action", "getprice");
        double price = 0.0d;
        String responseFromPricingEngine = getResponseFromPricingEngine(queryParams);
        //log.info("PricingEngineService.getSizePrice(): Response from pricing engine={}", responseFromPricingEngine);
        try {
            price = Double.parseDouble(responseFromPricingEngine);
            //@todo : Rahul Agrawal : Discuss this
            /*if(price == 0.0d){
                throw new RuntimeException("Error:PricingEngineService.getSizePrice() returned a price = 0");
            }*/
        } catch (NumberFormatException e) {
            log.error("Invalid price=[{}] returned by the pricing engine.", responseFromPricingEngine, e);
            Throwables.propagate(e);
        }
        return price;
    }

    /*Note about pricing engine from Gaurav's java docs
    The output of the ***getPrice()*** call is a numeric value denoting the offer price for the combination, N if the combination is
    not possible and Y if the combination is possible but it so happens that the PricingAndAvailability spreadsheet
    does not contain the price of that stone at that point.
    */

    public boolean checkAvailability(Customization customization) throws Exception {
        log.debug("PricingEngineService.checkAvailability()");
        log.info("CheckAvailability called . As of now implemented thru getPrice()");
        Map<String, Object> queryParams = buildRequiredQueryParams(customization);
        boolean isAvailable = false;

        //queryParams.put("action", "getavailability");
        queryParams.put("action", "getprice");   //@todo Later this needs to be fixed : Rahul Agrawal

        String responseFromPricingEngine = getResponseFromPricingEngine(queryParams);
        if (StringUtils.isNotBlank(responseFromPricingEngine)) {
            if ("Y".equalsIgnoreCase(responseFromPricingEngine)) {
                isAvailable = false;
                log.info("Combination is a valid but the price is not present in the pricing engine.");
            } else if ("N".equalsIgnoreCase(responseFromPricingEngine)) {
                log.info("Invalid combination of stones");
                isAvailable = false;
            } else {
                isAvailable = true;
            }
        } else {
            log.debug("Got a blank/null/empty <{}> response fom pricing engine", responseFromPricingEngine);
        }
        return isAvailable;
    }

    private String getResponseFromPricingEngine(Map<String, Object> queryParams) throws Exception {
        String response = restConnection.executeGetRequest(pricingEngineUrl, "pricing", queryParams);
        return response;
    }

    Map<String, Object> buildRequiredQueryParams(Customization customization) {
        Map<String, Object> queryParams = new HashMap<String, Object>();
        QueryParamBuilder.addMetalQueryParamsForPrice(customization, queryParams);
        QueryParamBuilder.addStoneQueryParamsForPrice(customization, queryParams);
        return queryParams;
    }
}
