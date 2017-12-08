package com.bluestone.app.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

public class LocalReflectionUtils {


    private static final Logger log = LoggerFactory.getLogger(LocalReflectionUtils.class);

    public static Object getAttribute(String attributeName, Object target) {
        String getterMethodName = "get" + StringUtils.capitalise(attributeName);
        Field field = ReflectionUtils.findField(target.getClass(), attributeName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + attributeName + "] on target [" + target + "] ");
        }
        Method method = ReflectionUtils.findMethod(target.getClass(), getterMethodName);
        if (method == null) {
            throw new IllegalArgumentException("Could not find getter method [" + getterMethodName + "] on target [" + target + "]");
        }
        ReflectionUtils.makeAccessible(method);
        return ReflectionUtils.invokeMethod(method, target);
    }

    public static boolean setAttribute(String attributeName, Object value, Object target) {
        String setterMethodName = "set" + StringUtils.capitalise(attributeName);
        Field field = ReflectionUtils.findField(target.getClass(), attributeName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + attributeName + "] on target [" + target + "] ");
        }
        Method method = ReflectionUtils.findMethod(target.getClass(), setterMethodName, new Class[]{field.getType()});
        if (method == null) {
            throw new IllegalArgumentException("Could not find setter method [" + setterMethodName + "] on target [" + target
                                               + "] with parameter type [" + field.getType() + "].");
        }
        ReflectionUtils.makeAccessible(method);
        if (field.getType().isPrimitive()) {
            if (field.getType().equals(boolean.class)) {
                try {
                    value = Boolean.parseBoolean(value.toString());
                    ReflectionUtils.invokeMethod(method, target, value);
                } catch (IllegalArgumentException e) {
                    log.error("Error: LocalReflectionUtils.setAttribute(): Reason={}", e.toString(), e);
                }
            }
        } else {
            ReflectionUtils.invokeMethod(method, target, new Object[]{value});
        }
        return value.equals(LocalReflectionUtils.getAttribute(attributeName, target));
    }
}
