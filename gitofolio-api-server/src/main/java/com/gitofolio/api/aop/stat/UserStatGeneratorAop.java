package com.gitofolio.api.aop.stat;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.JoinPoint;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.gitofolio.api.service.user.UserStatService;
import com.gitofolio.api.service.user.UserStatisticsService;
import com.gitofolio.api.aop.stat.annotation.UserStatGenerator;

import java.lang.reflect.*;
import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Component
public class UserStatGeneratorAop{
	
	@Autowired
	private UserStatService userStatService;
	
	@Autowired
	private UserStatisticsService userStatisticsService;
	
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@AfterReturning("@annotation(com.gitofolio.api.aop.stat.annotation.UserStatGenerator)")
	public void generateUserStat(JoinPoint joinPoint){
		String methodName = joinPoint.getSignature().getName();
		Class target = joinPoint.getTarget().getClass();
		Method[] methods = target.getDeclaredMethods();
		
		UserStatGenerator userStatGenerator = null;
		for(Method method : methods){
			if(method.getName().equals(methodName)){
				userStatGenerator = method.getAnnotation(UserStatGenerator.class);
				break;
			}
		}
		
		int idx = userStatGenerator.idx();
		Object[] params = joinPoint.getArgs();
		
		if(params[idx].getClass().equals(Long.class)){
			this.increaseVisitCount((Long)params[idx]);
			this.setReffererSite((Long)params[idx]);
		}
		if(params[idx].getClass().equals(String.class)){
			this.increaseVisitCount((String)params[idx]);
			this.setReffererSite((String)params[idx]);
		}
	}
	
	private void increaseVisitCount(Long id){
		this.userStatService.increaseTotalVisitors(id);
		this.userStatisticsService.increaseVisitorStatistics(id);
	}
	
	private void setReffererSite(Long id){
		String refferingSiteName = this.httpServletRequest.getHeader("Referer");
		if(refferingSiteName == null) return;
		this.userStatisticsService.setRefferingSite(id, refferingSiteName);
	}
	
	private void increaseVisitCount(String name){
		this.userStatService.increaseTotalVisitors(name);
		this.userStatisticsService.increaseVisitorStatistics(name);
	}
	
	private void setReffererSite(String name){
		String refferingSiteName = this.httpServletRequest.getHeader("Referer");
		if(refferingSiteName == null) return;
		this.userStatisticsService.setRefferingSite(name, refferingSiteName);
	}
	
}