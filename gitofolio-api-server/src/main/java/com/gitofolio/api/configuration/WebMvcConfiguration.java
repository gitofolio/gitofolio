package com.gitofolio.api.configuration;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.filter.*;

import com.gitofolio.api.controller.interceptor.*;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer{
	
	@Override
	public void addInterceptors(InterceptorRegistry interceptorRegistry){
		interceptorRegistry.addInterceptor(this.todayInteractionInterceptor())
			.addPathPatterns("/**");
	}

	@Bean
	public HandlerInterceptor todayInteractionInterceptor(){
		return new TodayInteractionInterceptor();
	}
	
	@Bean
	public ShallowEtagHeaderFilter shallowEtagHeaderFilter(){
		return new ShallowEtagHeaderFilter();
	}

}
