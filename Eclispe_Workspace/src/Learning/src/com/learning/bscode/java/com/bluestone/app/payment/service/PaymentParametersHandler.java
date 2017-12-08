package com.bluestone.app.payment.service;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PaymentParametersHandler {
	
	private static final Logger log = LoggerFactory.getLogger(PaymentParametersHandler.class);
	
	public StringBuilder generateDebugMessage(HttpServletRequest httpServletRequest) {		
		log.debug("PaymentParametersHandler.generateDebugMessage()");
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        final Set<String> set = parameterMap.keySet();
        StringBuilder stringBuilder = new StringBuilder();
        for (String eachKey : set) {
            stringBuilder.append(eachKey).append("=").append(Arrays.toString(parameterMap.get(eachKey))).append('\n');
        }
        log.debug("PaymentParametersHandler.generateDebugMessage(): {}", stringBuilder.toString());
        return stringBuilder;
    }
	
	public Map<String,Object> buildResponseParameterMap(HttpServletRequest httpServletRequest){		
		Map<String, Object> responseFields = new HashMap<String, Object>();
		for (Enumeration<?> en = httpServletRequest.getParameterNames(); en.hasMoreElements();) {
			String fieldName = (String) en.nextElement();
			String fieldValue = httpServletRequest.getParameter(fieldName);
			responseFields.put(fieldName, fieldValue);
		}
		log.debug("PaymentParametersHandler.buildResponseParameterMap(): {}", responseFields);
		return responseFields;
	}

}
