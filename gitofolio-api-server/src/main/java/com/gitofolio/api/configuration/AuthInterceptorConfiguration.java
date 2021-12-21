package com.gitofolio.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Configuration;

import com.gitofolio.api.interceptor.auth.AuthenticateInterceptor;

@Configuration
public class AuthInterceptorConfiguration implements WebMvcConfigurer{
	
	@Override
	public void addInterceptors(InterceptorRegistry registry){
		registry.addInterceptor(authenticateInterceptor()).addPathPatterns("*", "/*", "/**", "**");
	}
	
	@Bean
	public AuthenticateInterceptor authenticateInterceptor(){
		return new AuthenticateInterceptor();
	}
	
}