package com.bluestone.app.design.solitaire;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.design.solitairemount.SolitaireMountDao;
import com.google.gson.Gson;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class SolitaireService {
    
    private static final Logger log = LoggerFactory.getLogger(SolitaireService.class);
    
    @Autowired
    private SolitaireDao solitaireDao;
    
    @Autowired
    private SolitaireMountDao solitaireMountDao;
    
    public Map<String, SolitaireColumnMetaData> getColumnMetaData(boolean adjustCaratValues,HttpSession session, String sessionKey) {
        Map<String, SolitaireColumnMetaData> solitaireColumnMetaDatas = new LinkedHashMap<String, SolitaireColumnMetaData>();
        SolitaireColumnEnum[] values = SolitaireColumnEnum.values();
        for (SolitaireColumnEnum solitaireColumnEnum : values) {
            SolitaireColumnMetaData eachColumnMetaData = new SolitaireColumnMetaData(solitaireColumnEnum);                       
            if(solitaireColumnEnum.isSliderFilter()) {
            	BigDecimal[] minMaxValues = null;
            	String dbColumnName = solitaireColumnEnum.getDbColumnName();
                if(adjustCaratValues && dbColumnName.equalsIgnoreCase("carat")){
                	String buildFlowCategory = (String) session.getAttribute("buildFlowCategory");
                	if(StringUtils.isBlank(buildFlowCategory)) {
                	    String substringAfterLast = StringUtils.substringAfterLast(sessionKey, "solitairelist"); // this will give ring or pendant
                        buildFlowCategory = StringUtils.capitalize(substringAfterLast);
                	}
                	minMaxValues = solitaireMountDao.getMinMaxCaratValues(buildFlowCategory);            	
                }else{
                	minMaxValues = solitaireDao.getMinAndMaxValues(dbColumnName);
                }            	
            	eachColumnMetaData.setMinRange(minMaxValues[0]);
                eachColumnMetaData.setMaxRange(minMaxValues[1]);
            }            
            solitaireColumnMetaDatas.put(eachColumnMetaData.getDbColumnName(), eachColumnMetaData);
        }
        return solitaireColumnMetaDatas;
    }
    
    public List<Solitaire> getSolitaireList(String offset, String limit, String sortColumnDbName, String sortColumnDirection, Map<String, List<String>> searchData, Map<String, Number[]> rangeData) {
        return solitaireDao.getSolitaireList(offset, limit, sortColumnDbName, sortColumnDirection, searchData, rangeData);
    }
    
    public long getSolitaireListCount(Map<String, List<String>> searchData, Map<String, Number[]> rangeData) {
        return solitaireDao.getSolitaireListCount(searchData, rangeData);
    }
    
    public Solitaire getByItemId(String itemId) {		
		return solitaireDao.getByItemId(itemId);
	}
    
    public Map<String, Object> getSolitareFilterData(HttpSession session, String sessionKey,boolean adjustCaratValues) {
    	Map<String, Object> responseData = new HashMap<String, Object>();
        Map<String,SolitaireColumnMetaData> solitaireData = getColumnMetaData(adjustCaratValues,session,sessionKey);
        
        //Restore initialization values from session
        Map<String,Map<String,String>> solitaireMap = (Map<String, Map<String,String>>) session.getAttribute(sessionKey);
        if(solitaireMap!=null && !solitaireMap.isEmpty()){
        	for (String key : solitaireData.keySet()) {
    			SolitaireColumnMetaData solitaireColumnMetaData = solitaireData.get(key);
    			if(solitaireColumnMetaData.isSliderFilter()){
    				Map<String,String> sliderPropsMap = (Map<String, String>) solitaireMap.get("sliderProps");
    				if(!sliderPropsMap.isEmpty()){
    					String minVal = (String) sliderPropsMap.get(solitaireColumnMetaData.getDbColumnName() + "dataindex0");
    					String maxVal = (String) sliderPropsMap.get(solitaireColumnMetaData.getDbColumnName() + "dataindex1");
    					if(StringUtils.isNotBlank(minVal) && StringUtils.isNotBlank(maxVal)){
    						solitaireColumnMetaData.setFilterValues(minVal+","+maxVal);
    					}else{
    						solitaireColumnMetaData.setFilterValues(solitaireColumnMetaData.getMinRange()+","+solitaireColumnMetaData.getMaxRange());
    					}
    				}
    			}else{
    				Map<String,String> nonSliderPropsMap = (Map<String, String>) solitaireMap.get("nonSliderProps");
    				if(!nonSliderPropsMap.isEmpty()){
    					String searchVal = (String) nonSliderPropsMap.get(solitaireColumnMetaData.getDbColumnName());
    					solitaireColumnMetaData.setFilterValues(searchVal);
    				}
    			}
    		}
        }else{
        	resetSolitaireSessionParameters(session, "round", sessionKey,adjustCaratValues);
        }
        
        List<String> allColumnNames = new ArrayList<String>();
        
        for (SolitaireColumnMetaData solitaireColumnMetaData : solitaireData.values()) {
            allColumnNames.add(solitaireColumnMetaData.getDisplayColumnName());
        }
        
        responseData.put("columnNames", allColumnNames);
        responseData.put("columnData", new Gson().toJson(solitaireData));        
        return responseData;
    }
    
    public void resetSolitaireSessionParameters(HttpSession session, String shape, String sessionKey,boolean adjustCaratValues){
    	Map<String,Map<String,String>> solitaireMap = new HashMap<String, Map<String,String>>();
        Map<String,String> sliderPropsMap = new HashMap<String, String>();
        Map<String,String> nonsliderPropsMap = new HashMap<String, String>();        
        Map<String,SolitaireColumnMetaData> solitaireData = getColumnMetaData(adjustCaratValues,session, sessionKey);
        for (String key : solitaireData.keySet()) {
			SolitaireColumnMetaData solitaireColumnMetaData = solitaireData.get(key);
			if(solitaireColumnMetaData.isSliderFilter()){				
				sliderPropsMap.put(solitaireColumnMetaData.getDbColumnName() + "dataindex0",solitaireColumnMetaData.getMinRange().toPlainString());
				sliderPropsMap.put(solitaireColumnMetaData.getDbColumnName() + "dataindex1",solitaireColumnMetaData.getMaxRange().toPlainString());
				solitaireColumnMetaData.setFilterValues(solitaireColumnMetaData.getMinRange()+","+solitaireColumnMetaData.getMaxRange());				
			}else{				
				if(solitaireColumnMetaData.getColumnIndex()==0){
					nonsliderPropsMap.put(solitaireColumnMetaData.getDbColumnName(), shape);
					solitaireColumnMetaData.setFilterValues(shape);
				}else{
					nonsliderPropsMap.put(solitaireColumnMetaData.getDbColumnName(), "");
					solitaireColumnMetaData.setFilterValues("");
				}				
			}
		}
        solitaireMap.put("sliderProps", sliderPropsMap);
        solitaireMap.put("nonSliderProps", nonsliderPropsMap);
        session.setAttribute(sessionKey, solitaireMap);
    }

    
}
