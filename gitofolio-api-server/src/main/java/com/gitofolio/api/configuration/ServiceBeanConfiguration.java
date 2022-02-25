package com.gitofolio.api.configuration;

import org.springframework.context.annotation.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.annotation.*;

@Configuration
public class ServiceBeanConfiguration{
	
	@Bean(name="prettyObjectMapper")
	public ObjectMapper getObjectMapper(){
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return objectMapper;
	}
	
}