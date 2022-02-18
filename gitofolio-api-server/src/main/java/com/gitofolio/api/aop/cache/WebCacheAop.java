package com.gitofolio.api.aop.cache;

import org.aspectj.lang.annotation.*;

import com.gitofolio.api.aop.AnnotationExtractor;
import com.gitofolio.api.aop.cache.setter.*;
import com.gitofolio.api.aop.cache.annotation.*;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.JoinPoint;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.*;

@Aspect
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