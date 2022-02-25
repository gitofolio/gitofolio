package com.gitofolio.api.service.common;

import org.springframework.context.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeansException;

@Component
public class ApplicationContextProvider implements ApplicationContextAware{
	
	private static ApplicationContext applicationContext;
	
	public static ApplicationContext getApplicationContext(){
		return applicationContext;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException{
		this.applicationContext = applicationContext;
	}
	
}