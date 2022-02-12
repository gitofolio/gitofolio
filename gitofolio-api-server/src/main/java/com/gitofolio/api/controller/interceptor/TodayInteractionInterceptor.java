package com.gitofolio.api.controller.interceptor;

import javax.servlet.http.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.handler.*;

import com.gitofolio.api.service.common.TodayInteraction;

public class TodayInteractionInterceptor extends HandlerInterceptorAdapter{

	private TodayInteraction todayInteraction;

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response,
			Object handler,
			ModelAndView modelAndView) throws Exception {
		this.todayInteraction.increaseInteractCount();
	}
	
	@Autowired
	public void HandlerInterceptor(TodayInteraction todayInteraction){
		this.todayInteraction = todayInteraction;
	}

}
