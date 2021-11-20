package com.gitofolio.api.service.user.eraser;

import org.springframework.stereotype.Service;

@Service
public interface UserEraser{
	
	String delete(String name);  
	
	String delete(String name, Object parameter);
	
}