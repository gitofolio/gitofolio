package com.gitofolio.api.configuration;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.filter.*;
import org.springframework.boot.web.servlet.*;

import com.gitofolio.api.controller.interceptor.*;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer{
	
	@Override
	public void addCorsMappings(CorsRegistry corsRegistry){
		corsRegistry.addMapping("**")
			.allowedOrigins("*")
			.maxAge(3600);
	}
	
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
	public FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilter(){
		FilterRegistrationBean<ShallowEtagHeaderFilter> filterRegistrationBean = new FilterRegistrationBean<ShallowEtagHeaderFilter>(new ShallowEtagHeaderFilter());
        filterRegistrationBean.addUrlPatterns("/user/*", "/portfoliocards/*", "/restdocs", "/restdocs.html");
        filterRegistrationBean.setName("ETagFilter");
        return filterRegistrationBean;
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry){
		resourceHandlerRegistry.addResourceHandler("/restdocs.html","/*.ico")
			.addResourceLocations("classpath:/html/docs/");
	}
	
}
