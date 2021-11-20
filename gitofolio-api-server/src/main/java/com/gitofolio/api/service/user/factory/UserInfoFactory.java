package com.gitofolio.api.service.user.factory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.UserMapper;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.parameter.ParameterHandler;

@Service
public class UserInfoFactory implements UserFactory{
	
	protected UserMapper userInfoService;
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name){
		return this.userInfoService.doMap(name);
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name, Object parameter){
		throw new IllegalStateException("GET : /user/{name}의 parameter요청은 허용되지 않았습니다.");
	}
	
	@Override
	@Transactional
	public UserDTO saveUser(UserDTO userDTO){
		return this.userInfoService.resolveMap(userDTO);
	}
	
	@Autowired
	public UserInfoFactory(@Qualifier("userInfoService") UserMapper userInfoService){
		this.userInfoService = userInfoService;
	}
	
}