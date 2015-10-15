package com.bluestone.app.core.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import static com.bluestone.app.core.util.DataSheetConstants.DESIGN_CATEGORYTYPE.*;

public class DataSheetConstants {

    public static Map<String, Integer> sheets = new ImmutableMap.Builder<String, Integer>()
            .put("customization", 0)
            .put("data", 1)
            .put("tags", 2)
            .put("title", 3)
            .build();


    public static Map<String, Integer> dataGroups = new ImmutableMap.Builder<String, Integer>()
            .put("customization", sheets.get("customization"))
            .put("general", sheets.get("data"))
            .put("metal", sheets.get("data"))
            .put("stone", sheets.get("data"))
            .put("size", sheets.get("data"))
            .put("tag", sheets.get("tags"))
            .put("title", sheets.get("title"))
            .build();


    public static Map<String, Integer> customization = new HashMap<String, Integer>();

    // excel sheet column no for metal definition
    public static Map<String, Integer> metalColumnIndex = new LinkedHashMap<String, Integer>();

    // excel sheet column no for stone definition
    public static Map<String, Integer> stoneColumnIndex = new LinkedHashMap<String, Integer>();

    static {
        customization.put("variation", 0);
        customization.put("design code", 1);


        metalColumnIndex.put("metal 1", 2);
        metalColumnIndex.put("metal 2", 3);

        customization.put("purity", 4);

        stoneColumnIndex.put("stone 1", 5);
        stoneColumnIndex.put("stone 2", 6);
        customization.put("start", 1);
    }

    public static Map<String, Integer> general = new ImmutableMap.Builder<String, Integer>()
            .put("type", 0)
            .put("size", 1)
            .put("gender", 2)
            .put("finding", 3)
            .put("height", 4)
            .put("width", 5)
            .put("start", 1)
            .build();


    public static Map<String, Integer> metal = new ImmutableMap.Builder<String, Integer>()
            .put("weight", 0)
            .put("treatment", 1)
            .put("start", 4)
            .build();


    public static Map<String, Integer> stone = new ImmutableMap.Builder<String, Integer>()
            .put("cut", 0)
            .put("numbers", 1)
            .put("size", 2)
            .put("setting", 3)
            .put("plating", 4)
            .put("family", 5)
            .put("color", 6)
            .put("sizeIndex", 7)
            .put("start", 8)
            .put("solitaire_wt_range", 9)
            .build();


    public static Map<String, Integer> size = new HashMap<String, Integer>();
    public static Map<String, Integer> metalWeightIndex = new HashMap<String, Integer>();

    static {
        size.put("size", 0);
        metalWeightIndex.put("metal 1", 1);
        metalWeightIndex.put("metal 2", 3);
        size.put("rowAdd", 3);
    }


    public enum DESIGN_CATEGORYTYPE {
        RINGS("Rings"),
        EARRINGS("Earrings"),
        PENDANTS("Pendants"),
        BANGLES("Bangles"),
        CHAINS("Chains"),
        TANMANIYA("Tanmaniya"),
        TANMANIYA_CHAINS("Tanmaniya Chains"),
        SOLITAIRE_PENDANT_MOUNT("Solitaire Pendant Mount"),
        SOLITAIRE_RING_MOUNT("Solitaire Ring Mount"),
        SOLITAIRE("Solitaire"); // added this to keep consistency with the database entries. It would not be used

        private String name;

        private DESIGN_CATEGORYTYPE(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    ;

    public static Map<String, String> categories = new ImmutableMap.Builder<String, String>()
            .put("ring", RINGS.getName())
            .put("earring", EARRINGS.getName())
            .put("pendant", PENDANTS.getName())
            .put("bangle", BANGLES.getName())
            .put("chain", CHAINS.getName())
            .put("tanmaniya", TANMANIYA.getName())
            .put("tanmaniya chain", TANMANIYA_CHAINS.getName())
            .put("solitaire pendant mount", SOLITAIRE_PENDANT_MOUNT.getName())
            .put("solitaire ring mount", SOLITAIRE_RING_MOUNT.getName())
            .put("solitaire", SOLITAIRE.getName())
            .build();


    public static Map<String, String> units = new ImmutableMap.Builder<String, String>()
            .put("gram", "gram")
            .put("grams", "gram")
            .put("default", "gram")
            .build();
}
