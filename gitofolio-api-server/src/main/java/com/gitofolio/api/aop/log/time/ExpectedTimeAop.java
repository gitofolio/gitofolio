package com.gitofolio.api.aop.log.time;

import com.gitofolio.api.aop.log.time.annotation.ExpectedTime;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;

import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.annotation.Annotation;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Aspect
@Component
public class ExpectedTimeAop{
	
	private final Logger logger = LoggerFactory.getLogger(ExpectedTimeAop.class);
	
	@Around("@annotation(com.gitofolio.api.aop.log.time.annotation.ExpectedTime)")
	public Object checkTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
		System.out.println("start");
		String methodName = proceedingJoinPoint.getSignature().getName();
		Object[] args = proceedingJoinPoint.getArgs();
		Class targetClass = proceedingJoinPoint.getTarget().getClass();
		System.out.println(methodName + ", " + args + ", " + targetClass.toString());
		
		Method methods[] = targetClass.getMethods();
		ExpectedTime expectedTime = null;
		for(Method method : methods){
			if(method.getName().equals(methodName)){
				expectedTime = method.getAnnotation(ExpectedTime.class);
				break;
			}
		}
		long expectMilliSec = expectedTime.milliSec();
		long startMilliSec = System.currentTimeMillis();
		
		Object result = proceedingJoinPoint.proceed(args);
		
		long endMilliSec = System.currentTimeMillis();
		
		if(endMilliSec - startMilliSec > expectMilliSec) logger.warn(targetClass.toString() + "." + methodName + " Timeout / Expected time : " + expectMilliSec + ", Excution time : " + (endMilliSec - startMilliSec));
		else logger.info(targetClass.toString() + "." + methodName + " Expected time : " + expectMilliSec + ", Excution time : " + (endMilliSec - startMilliSec));
		
		return result;
	}
	
}