package com.gitofolio.api.aop.cache;

import org.aspectj.lang.annotation.*;
import org.aspectj.lang.JoinPoint;

import com.gitofolio.api.aop.AnnotationExtractor;
import com.gitofolio.api.aop.cache.setter.*;
import com.gitofolio.api.aop.cache.annotation.*;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class WebCacheAop{
	
	@Autowired
	private AnnotationExtractor<WebCache> annotationExtractor;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@After(value="@annotation(com.gitofolio.api.aop.cache.annotation.WebCache)")
	public void setCache(JoinPoint joinPoint){
		WebCache webCache = this.annotationExtractor.extractAnnotation(joinPoint, WebCache.class);
		webCache.cacheType().setCache(this.httpServletResponse);
	}
	
}