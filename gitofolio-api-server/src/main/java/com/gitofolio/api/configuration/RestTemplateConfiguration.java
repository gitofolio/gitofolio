package com.gitofolio.api.configuration;

import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.*; 
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.apache.http.client.*;
import org.apache.http.impl.client.*;

import java.net.SocketException;

@Configuration
public class RestTemplateConfiguration{
	
	@Bean
	public RestTemplate restTemplate(){
		
		HttpClient httpClient = HttpClients.custom()
			.setMaxConnTotal(20)
			.setMaxConnPerRoute(4)
			.setRetryHandler(this.retryHandler())
			.build();
		
		HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		
		httpComponentsClientHttpRequestFactory.setConnectTimeout(5000);
		httpComponentsClientHttpRequestFactory.setReadTimeout(5000);
		
		RestTemplate restTemplate = new RestTemplate(httpComponentsClientHttpRequestFactory);
		
		return restTemplate;
	}
	
	@Bean
	public HttpRequestRetryHandler retryHandler(){
		return (exception, executionCount, context) -> {
            if (executionCount > 3) {
                return false;
            }
            if (exception instanceof SocketException && exception.getMessage().equals("Connection reset")) {
                return true;
			}
            return false;
        };
	} 
	
}