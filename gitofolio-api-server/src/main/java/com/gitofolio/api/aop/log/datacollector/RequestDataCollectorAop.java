package com.gitofolio.api.aop.log.datacollector;

import org.aspectj.lang.annotation.*;
import org.aspectj.lang.ProceedingJoinPoint;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.*;

import com.gitofolio.api.aop.AnnotationExtractor;
import com.gitofolio.api.aop.log.datacollector.annotation.RequestDataCollector;
import com.gitofolio.api.service.common.UnCaughtException;

import org.slf4j.*;

@Aspect
@Component
public class RequestDataCollectorAop{
	
	private final Logger logger;
	private final AnnotationExtractor<RequestDataCollector> annotationExtractor;
	
	@Around("@annotation(com.gitofolio.api.aop.log.datacollector.annotation.RequestDataCollector)")
	public Object collectData(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
		
		RequestDataCollector requestDataCollector = this.annotationExtractor.extractAnnotation(proceedingJoinPoint, RequestDataCollector.class);
		
		Object result = null;
		long startMilliSec = System.currentTimeMillis();
		Exception exception = null;
		try{
			result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
		}catch(Exception e){
			exception = e;
		}
		long excutionMilliSec = this.getExcutionMillSec(startMilliSec);
		
		RequestData requestData = new RequestData.Builder()
			.path(requestDataCollector.path())
			.parameters(proceedingJoinPoint.getArgs())
			.excutionTime(excutionMilliSec)
			.exception(exception)
			.build();
		
		requestData.doLog(this.logger);
		
		if(isExceptionNull(exception)) return result;
		throw exception;
	}
	
	private long getExcutionMillSec(long startMilliSec){
		return System.currentTimeMillis() - startMilliSec;
	}
	
	private boolean isExceptionNull(Exception e){
		return e == null ? true : false;
	}
	
	@Autowired
	public RequestDataCollectorAop(AnnotationExtractor<RequestDataCollector> annotationExtractor){
		this.logger = LoggerFactory.getLogger(RequestDataCollectorAop.class);
		this.annotationExtractor = annotationExtractor;
	}
	
}