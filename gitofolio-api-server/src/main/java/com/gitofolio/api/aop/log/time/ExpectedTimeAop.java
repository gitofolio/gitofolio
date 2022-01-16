package com.gitofolio.api.aop.log.time;

import com.gitofolio.api.aop.AnnotationExtractor;
import com.gitofolio.api.aop.log.time.annotation.ExpectedTime;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.reflect.Method;
import java.lang.annotation.Annotation;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Aspect
@Component
public class ExpectedTimeAop{
	
	private final Logger logger;
	private final AnnotationExtractor<ExpectedTime> annotationExtractor;
	
	@Around("@annotation(com.gitofolio.api.aop.log.time.annotation.ExpectedTime)")
	public Object checkTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
		
		ExpectedTime expectedTime = this.annotationExtractor.extractAnnotation(proceedingJoinPoint, ExpectedTime.class);
		
		long expectedMilliSec = expectedTime.milliSec();
		long startMilliSec = System.currentTimeMillis();
		
		Object result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
		
		long endMilliSec = System.currentTimeMillis();
		
		String methodName = proceedingJoinPoint.getSignature().getName();
		Class targetClass = proceedingJoinPoint.getTarget().getClass();
		
		doLog(targetClass.toString()+"."+methodName, endMilliSec-startMilliSec, expectedMilliSec);
		
		return result;
	}
	
	private void doLog(String triggerInfo, long excutionMilliSec, long expectedMillisec){
		if(excutionMilliSec > expectedMillisec){
			this.logger.warn(triggerInfo + " Timeout / Expected time : " + expectedMillisec + ", Excution time : " + excutionMilliSec);
		}else{
			this.logger.info(triggerInfo + " Expected time : " + expectedMillisec + ", Excution time : " + excutionMilliSec);
		}
	}
	
	@Autowired
	public ExpectedTimeAop(@Qualifier("annotationExtractor") AnnotationExtractor<ExpectedTime> annotationExtractor){
		this.annotationExtractor = annotationExtractor;
		this.logger = LoggerFactory.getLogger(ExpectedTimeAop.class);
	}
	
}