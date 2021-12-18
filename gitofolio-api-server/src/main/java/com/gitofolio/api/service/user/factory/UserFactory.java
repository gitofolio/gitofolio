package com.gitofolio.api.service.user.factory;

import org.springframework.stereotype.Service;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.parameter.ParameterHandler;
import com.gitofolio.api.service.user.factory.mapper.UserMapper;

@Service
public interface UserFactory{
	
	UserDTO getUser(String name);
	
	UserDTO getUser(String name, Object parameter);
	
	UserDTO saveUser(UserDTO userDTO);
	
	UserDTO editUser(UserDTO userDTO);
	
	UserDTO editUser(UserDTO userDTO, Object parameter);
	
}