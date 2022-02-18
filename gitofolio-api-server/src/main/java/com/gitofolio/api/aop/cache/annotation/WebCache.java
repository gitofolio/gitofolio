package com.gitofolio.api.aop.cache.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebCache{
	
	CacheType cacheType();
	
}