package com.bluestone.app.uniware;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.CustomizationMetalSpecification;
import com.bluestone.app.design.model.CustomizationStoneSpecification;
import com.bluestone.app.design.model.Design;
import com.bluestone.app.design.model.DesignStoneSpecification;
import com.bluestone.app.design.model.MetalSpecification;
import com.bluestone.app.design.model.StoneSpecification;

/**
 * @author Rahul Agrawal
 *         Date: 2/5/13
 */
class ItemFeatureBuilder {

    private static final Logger log = LoggerFactory.getLogger(ItemFeatureBuilder.class);

    private final ImmutableList<String> STONE_FEATURES = ImmutableList.of("Type", "Shape", "Size", "Quantity", "Quality", "Weight", "SettingType");

    private ImmutableList<String> METAL_FEATURES = ImmutableList.of("Type", "Color", "Purity", "Weight");


    String execute(Customization customization) {
        log.debug("ItemFeatureBuilder.execute() for customization Sku=[{}]", customization.getSkuCode());
        StringBuilder stringBuilder = new StringBuilder();

        Design design = customization.getDesign();
        float height = design.getHeight();
        float width = design.getWidth();
        String skuCode = customization.getSkuCode();
        BigDecimal price = customization.getPrice();

        float weightOfProduct = customization.getTotalWeightOfProduct();
        String ProductWt = String.valueOf(weightOfProduct); //...
        log.info("ItemFeatureBuilder: SkuCode=[{}] Height=[{}] Width=[{}]  Price=[{}] TotalWeight=[{}]\n",
                 skuCode, height, width, price, ProductWt);

        Map<String, String> outerContainer = new HashMap<String, String>();
        outerContainer.put("ProductWt", ProductWt);
        outerContainer.put("Diamond", Collections.EMPTY_LIST.toString());

        LinkedList<Map<String, String>> metalFeatureList = new LinkedList<Map<String, String>>();
        final String metals = processMetal(customization, metalFeatureList);
        outerContainer.put("Metal", metals);
        extractEachMetal(outerContainer, metalFeatureList);

        LinkedList<Map<String, String>> stoneFeatureList = new LinkedList<Map<String, String>>();
        final String stoneFeatures = processStones(customization, stoneFeatureList);
        outerContainer.put("Stone", stoneFeatures);
        extractEachStone(outerContainer, stoneFeatureList);

        Gson gson = new Gson();
//      GsonBuilder gsonBuilder = new GsonBuilder();
//      gson = gsonBuilder.setPrettyPrinting().create();
        final String jsonString = gson.toJson(outerContainer);
        stringBuilder.append(jsonString);
        final String response = stringBuilder.toString();
        return response;
    }

    private void extractEachStone(Map outerContainer, List<Map<String, String>> stoneFeatureList) {
        log.debug("ItemFeatureBuilder.extractEachStone()");
        final String STONE_ = "Stone_";
        for (int i = 0; i < stoneFeatureList.size(); i++) {
            Map<String, String> eachStoneFeatureMap = stoneFeatureList.get(i);
            final String prefix = STONE_ + (i + 1) + "_";
            for (String eachFeature : STONE_FEATURES) {
                populateContainer(prefix, outerContainer, eachStoneFeatureMap, eachFeature);
            }
            log.info("\n");
        }
    }

    private void extractEachMetal(Map<String, String> outerContainer, List<Map<String, String>> metalFeatureList) {
        log.debug("ItemFeatureBuilder.extractEachMetal()");
        final String METAL_ = "Metal_";
        for (int i = 0; i < metalFeatureList.size(); i++) {
            Map<String, String> eachMetalFeatureMap = metalFeatureList.get(i);
            final String prefix = METAL_ + (i + 1) + "_";
            for (String eachMetalFeature : METAL_FEATURES) {
                populateContainer(prefix, outerContainer, eachMetalFeatureMap, eachMetalFeature);
            }
        }
    }

    private void populateContainer(String prefix, Map<String, String> outerContainer, Map<String, String> featureMap, String feature) {
        final String key = prefix + feature;
        final String value = featureMap.get(feature);
        log.debug("\t[{}]:[{}]", key, value);
        outerContainer.put(key, value);
    }


    private String processStones(Customization customization, List<Map<String, String>> stoneFeatureList) {
        log.debug("");
        //log.info("ItemFeatureBuilder.processStones()");
        List<CustomizationStoneSpecification> customizationStoneSpecifications = customization.getCustomizationStoneSpecification();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < customizationStoneSpecifications.size(); i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            CustomizationStoneSpecification aCustomizationStoneSpecification = customizationStoneSpecifications.get(i);
            DesignStoneSpecification designStoneSpecification = aCustomizationStoneSpecification.getDesignStoneSpecification();
            String SettingType = designStoneSpecification.getSettingType();  //...
            int noofstones = designStoneSpecification.getNoOfStones();
            int Quantity = noofstones; //...

            StoneSpecification stoneSpecification = aCustomizationStoneSpecification.getStoneSpecification();
            String Shape = stoneSpecification.getShape();  //...
            String Size = stoneSpecification.getSize();    //...
            String stonetype = stoneSpecification.getStoneType();
            String clarity = stoneSpecification.getClarity();
            String color = stoneSpecification.getColor();
            StringBuilder sb = new StringBuilder();
            log.debug("Stone Type=[{}] Clarity=[{}] Color=[{}]", stonetype, clarity, color);
            sb.append(stonetype);
            if (StringUtils.isNotBlank(clarity)) {
                sb.append(" ").append(clarity);
            }
            if (StringUtils.isNotBlank(color)) {
                sb.append(" ").append(color);
            }
            //String Type = stonetype + " " + clarity + " " + color;   //...
            String Type = sb.toString();

            BigDecimal eachStoneWeight = stoneSpecification.getStoneWeight();
            DecimalFormat df = new DecimalFormat("#.###");
            String EachStoneWeight = df.format(eachStoneWeight); //...

            final String Quality = "Default"; //...

            String totalWeight = df.format(eachStoneWeight.multiply(new BigDecimal(noofstones)));
            log.debug("ItemFeatureBuilder.processStones(): SettingType=[{}] Quantity=[{}] Type=[{}] Shape=[{}] Size=[{}] Weight of each Stone=[{}] carts, TotalWt=[{}] carats ",
                      SettingType, Quantity, Type, Shape, Size, EachStoneWeight, totalWeight);

            Map<String, String> eachStoneMap = new HashMap<String, String>();
            eachStoneMap.put("Type", Type);
            eachStoneMap.put("Shape", Shape);
            eachStoneMap.put("Size", Size);
            eachStoneMap.put("Quantity", String.valueOf(Quantity));
            eachStoneMap.put("Quality", Quality);
            //eachStoneMap.put("Weight", Weight);
            eachStoneMap.put("Weight", totalWeight);
            eachStoneMap.put("SettingType", SettingType);
            eachStoneMap.put("Rate", "");
            eachStoneMap.put("Color", "");
            // rate & color are not required
            stoneFeatureList.add(eachStoneMap);
            Gson gson = new Gson();
            final String mapToJson = gson.toJson(eachStoneMap);
            stringBuilder.append(mapToJson);
        }

        return "[" + stringBuilder.append("]").toString();
    }

    private String processMetal(Customization customization, LinkedList<Map<String, String>> metalFeatures) {
        StringBuilder stringBuilder = new StringBuilder();
        List<CustomizationMetalSpecification> customizationMetalSpecifications = customization.getCustomizationMetalSpecification();
        for (int i = 0; i < customizationMetalSpecifications.size(); i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }

            CustomizationMetalSpecification aCustomizationMetalSpecification = customizationMetalSpecifications.get(i);
            BigDecimal metalweight = aCustomizationMetalSpecification.getMetalWeight();
            DecimalFormat df = new DecimalFormat("#.###");
            String Weight = df.format(metalweight);
            MetalSpecification metalSpecification = aCustomizationMetalSpecification.getMetalSpecification();
            //String code = metalSpecification.getCode();
            //String fullname = metalSpecification.getFullname();
            int Purity = metalSpecification.getPurity();
            String Color = metalSpecification.getColor();
            String Type = metalSpecification.getType();
            log.debug("Metal Details : Weight=[{}] Purity=[{}] Color=[{}] Type=[{}]", Weight, Purity, Color, Type);

            // all have to be strings
            final Map<String, String> eachMetalMap = new HashMap<String, String>();
            eachMetalMap.put("Type", Type);
            eachMetalMap.put("Color", Color);
            eachMetalMap.put("Purity", String.valueOf(Purity));
            eachMetalMap.put("Weight", Weight);
            eachMetalMap.put("Rate", "");
            Gson gson = new Gson();
            final String mapToJson = gson.toJson(eachMetalMap);
            stringBuilder.append(mapToJson);
            metalFeatures.add(eachMetalMap);
        }
        return "[" + stringBuilder.append("]").toString();
    }

    /*
   Picked from prestashop - for Product id = 1501

   JSON string expected by Uniware =

   {
       "Metal_1_Purity": "18",
       "Stone_1_Shape": "Princess",
       "Stone": "[{\"Type\":\"Diamond SI GH\",\"Shape\":\"Princess\",\"Size\":\"1.5\",\"Quantity\":\"10\",\"Quality\":\"Default\",\"Weight\":\"0.19\",\"SettingType\":\"Channel\",\"Rate\":\"\",\"Color\":\"\"}]",
       "Stone_1_Quantity": "10",
       "Metal_1_Color": "White",
       "ProductWt": "2.49",
       "Metal_1_Type": "Gold",
       "Metal_1_Weight": "2.45",
       "Metal": "[{\"Type\":\"Gold\",\"Color\":\"White\",\"Purity\":\"18\",\"Weight\":\"2.45\",\"Rate\":\"\"}]",
       "Stone_1_Size": "1.5",
       "Stone_1_Type": "Diamond SI GH",
       "Stone_1_SettingType": "Channel",
       "Diamond": "[]",
       "Stone_1_Weight": "0.19",
       "Stone_1_Quality": "Default"
   }

    Prestashop Rest API gives us this.
   Product Id = 1501

       <![CDATA[ Metal ]]>
       <![CDATA[
       [{"Type":"Gold","Color":"White","Purity":"18","Weight":"2.45","Rate":""}]
       ]]>

       <![CDATA[ Diamond ]]>
       <![CDATA[ [] ]]>

       <![CDATA[ Stone ]]>
       <![CDATA[
       [{"Type":"Diamond SI GH","Shape":"Princess","Size":"1.5","Quantity":"10","Quality":"Default","Weight":"0.19","SettingType":"Channel","Rate":"","Color":""}]
       ]]>

       <![CDATA[ ProductWt ]]>
       <![CDATA[ 2.49 ]]>

       <![CDATA[ Metal_1_Type ]]>
       <![CDATA[ Gold ]]>

       <![CDATA[ Metal_1_Color ]]>
       <![CDATA[ White ]]>

       <![CDATA[ Metal_1_Purity ]]>
       <![CDATA[ 18 ]]>

       <![CDATA[ Metal_1_Weight ]]>
       <![CDATA[ 2.45 ]]>

       <![CDATA[ Stone_1_Type ]]>
       <![CDATA[ Diamond SI GH ]]>

       <![CDATA[ Stone_1_Shape ]]>
       <![CDATA[ Princess ]]>


       <![CDATA[ Stone_1_Size ]]>
       <![CDATA[ 1.5 ]]>


       <![CDATA[ Stone_1_Quantity ]]>
       <![CDATA[ 10 ]]>


       <![CDATA[ Stone_1_Quality ]]>
       <![CDATA[ Default ]]>



       <![CDATA[ Stone_1_Weight ]]>
       <![CDATA[ 0.19 ]]>


       <![CDATA[ Stone_1_SettingType ]]>
       <![CDATA[ Channel ]]>
       -----------------------------------------------------------------------------------------

       Nasty product 513 - has emrald, diamonds etc

           <![CDATA[ Metal ]]>
           <![CDATA[
           [{"Type":"Gold","Color":"Yellow","Purity":"18","Weight":"3.12","Rate":""}]
           ]]>

           --same


           <![CDATA[ Stone ]]>
           <![CDATA[[
            {"Type":"Diamond SI GH","Shape":"Round","Size":"2.5","Quantity":"2","Quality":"Default","Weight":"0.12","SettingType":"Prong","Rate":""},
            {"Type":"Diamond SI GH","Shape":"Round","Size":"2.1","Quantity":"2","Quality":"Default","Weight":"0.07","SettingType":"Prong","Rate":""},
            {"Type":"Diamond SI GH","Shape":"Round","Size":"1.7","Quantity":"4","Quality":"Default","Weight":"0.07","SettingType":"Prong","Rate":""},
            {"Type":"Emerald","Shape":"Pear","Size":"5.0X4.0","Quantity":"2","Quality":"","Weight":"0.82","SettingType":"Prong","Rate":""}]
           ]]>


           ---usual

           <![CDATA[ Stone_4_Type ]]>
           <![CDATA[ Emerald ]]>


           <![CDATA[ Stone_4_Shape ]]>
           <![CDATA[ Pear ]]>

           <![CDATA[ Stone_4_Size ]]>
           <![CDATA[ 5.0X4.0 ]]>

           <![CDATA[ 2 ]]>

    */

}
