package com.gitofolio.api.service.user.parameter;

import com.gitofolio.api.service.user.dtos.UserDTO;

import org.springframework.stereotype.Service;

@Service
public interface ParameterHandler<T>{
	
	UserDTO doHandle(UserDTO userDTO, T parameter);
	
}