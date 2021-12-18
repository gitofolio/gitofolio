package com.gitofolio.api.service.user.factory.mapper;

import com.gitofolio.api.service.user.dtos.UserDTO;

import org.springframework.stereotype.Service;

@Service
public interface UserMapper<T extends Object>{
	
	UserDTO doMap(T parameter);
	
	T resolveMap(UserDTO userDTO);
	
}