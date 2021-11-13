package com.gitofolio.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Configuration;
	
import com.gitofolio.api.interceptor.user.UserInterceptor;

@Configuration
public class UserInterceptorConfiguration implements WebMvcConfigurer{
	
	@Override
	public void addInterceptors(InterceptorRegistry registry){
		registry.addInterceptor(userInterceptor()).addPathPatterns("/user/*");
	}
	
	@Bean
	public UserInterceptor userInterceptor(){
		return new UserInterceptor();
	}
	
}