package com.gitofolio.api.configuration;

import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestTemplateConfiguration{
	
	@Bean
	public RestTemplate restTemplate(){
		
		RestTemplate restTemplate = new RestTemplate();
		
		return restTemplate;
	}
	
}