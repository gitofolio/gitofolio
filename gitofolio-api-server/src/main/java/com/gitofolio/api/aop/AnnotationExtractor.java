package com.gitofolio.api.aop;

import org.springframework.stereotype.Component;

import org.aspectj.lang.JoinPoint;

import java.lang.reflect.Method;
import java.lang.annotation.Annotation;

@Component
public class AnnotationExtractor<T extends Annotation>{
	
	public T extractAnnotation(JoinPoint joinPoint, Class<T> annotationType){
		String methodName = joinPoint.getSignature().getName();
		Class targetClass = joinPoint.getTarget().getClass();
		
		T ans = null;
		Method[] methods = targetClass.getMethods();
		for(Method method : methods){
			if(method.getName().equals(methodName)){
				ans = method.getAnnotation(annotationType);
				break;
			}
		}
		return ans;
	}
	
}