package com.bluestone.app.core.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RaiseEvent {
    
    EventType eventType();
    String value() default "";
    
    boolean isAsync() default false;
    
    boolean queueEvent() default false;
    
}
