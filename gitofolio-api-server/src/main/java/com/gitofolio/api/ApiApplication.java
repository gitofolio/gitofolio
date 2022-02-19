package com.gitofolio.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@EnableScheduling
@SpringBootApplication
public class ApiApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(ApiApplication.class, args);
		DispatcherServlet dispatcherServlet = (DispatcherServlet)applicationContext.getBean("dispatcherServlet");
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
	}

}
