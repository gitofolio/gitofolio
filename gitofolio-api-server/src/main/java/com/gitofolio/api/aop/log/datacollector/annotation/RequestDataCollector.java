package com.gitofolio.api.aop.log.datacollector.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestDataCollector{
	
	String path();
	
}