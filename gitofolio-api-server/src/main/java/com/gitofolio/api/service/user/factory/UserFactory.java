package com.gitofolio.api.service.user.factory;

import org.springframework.stereotype.Service;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.UserMapper;

@Service
public abstract class UserFactory{
	
	protected UserMapper userMapper;
	
	public UserDTO getUser(String name){
		return this.userMapper.doMap(name);
	}
	
	public UserDTO saveUser(UserDTO userDTO){
		return this.userMapper.resolveMap(userDTO);
	}
	
}