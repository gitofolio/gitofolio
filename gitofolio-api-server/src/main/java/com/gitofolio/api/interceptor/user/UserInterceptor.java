package com.gitofolio.api.interceptor.user;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gitofolio.api.service.user.UserStatService;
import com.gitofolio.api.service.user.UserStatisticsService;

public class UserInterceptor implements HandlerInterceptor{
	
	@Autowired
	private UserStatService userStatService;
	
	@Autowired
	private UserStatisticsService userStatisticsService;
	
	private String[] exceptUris = {"", "user", "portfolio"};
	
	@Override
	@Transactional
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception{
		String[] URIs = request.getRequestURI().split("/");
		String name = "";
		for(String uri : URIs){
			boolean isExcept = false;
			for(String exceptUri : exceptUris){
				if(uri.equals(exceptUri)){
					isExcept = true;
					break;
				} 
			}
			if(isExcept == true) continue;
			name = uri;
			break;
		}
		this.userStatService.addTotalVisitors(name);
		this.userStatisticsService.addVisitorStatistics(name);
	}
	
}