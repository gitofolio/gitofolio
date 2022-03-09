package com.gitofolio.api.controller.interceptor;

import javax.servlet.http.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.handler.*;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.domain.common.TodayInteraction;
import com.gitofolio.api.service.common.TodayInteractionService;

public class TodayInteractionInterceptor extends HandlerInterceptorAdapter{

	private TodayInteractionService todayInteractionService;
	
	@Override
	@Transactional
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response,
			Object handler,
			ModelAndView modelAndView) throws Exception {
		this.todayInteractionService.increaseInteractCount();
	}
	
	@Autowired
	public void HandlerInterceptor(TodayInteractionService todayInteractionService){
		this.todayInteractionService = todayInteractionService;
	}

}
