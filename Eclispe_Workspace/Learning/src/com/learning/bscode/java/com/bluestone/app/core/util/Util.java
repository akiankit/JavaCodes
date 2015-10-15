package com.bluestone.app.core.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import com.bluestone.app.admin.model.Property;
import com.bluestone.app.admin.service.IProperty;
import com.bluestone.app.admin.service.PropertyService;
import com.bluestone.app.core.MessageContext;

@Component
public class Util {

    public static List<String> getPropertyTypes() {
        List<String> propertyTypes = new ArrayList<String>();
        for (IProperty.PropertyDataTypes eachDataType : IProperty.PropertyDataTypes.values()) {
            propertyTypes.add(eachDataType.toString());
        }
        return propertyTypes;
    }

	public static MessageContext getMessageContext() {
		return (MessageContext) RequestContextHolder.getRequestAttributes().getAttribute("messageContext", 0);
	}

	public static String getContextPath() {
		MessageContext messageContext = getMessageContext();
		return messageContext.getContextPath();
	}

	public static String getSiteUrl() {
		MessageContext messageContext = getMessageContext();
		return messageContext.getSiteUrl();
	}

	public static String getSiteUrlWithContextPath() {
		MessageContext messageContext = getMessageContext();
		return messageContext.getSiteUrlWithContextPath();
	}


	public static boolean isEmptyString(String str) {
		return str == null || str.length() == 0 || str.equalsIgnoreCase("null");
	}

}
