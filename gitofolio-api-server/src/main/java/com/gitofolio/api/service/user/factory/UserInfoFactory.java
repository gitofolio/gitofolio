package com.gitofolio.api.service.user.factory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.UserMapper;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.parameter.ParameterHandler;
import com.gitofolio.api.service.user.factory.hateoas.Hateoas;

@Service
public class UserInfoFactory implements UserFactory{
	
	private UserMapper userInfoService;
	private Hateoas userInfoHateoas;
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name){
		return this.setHateoas(this.userInfoService.doMap(name));
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name, Object parameter){
		throw new IllegalStateException("GET : /user/{name}의 parameter요청은 허용되지 않았습니다.");
	}
	
	@Override
	@Transactional
	public UserDTO saveUser(UserDTO userDTO){
		return this.setHateoas(this.userInfoService.resolveMap(userDTO));
	}
	
	@Override
	public UserDTO setHateoas(UserDTO userDTO){
		userDTO.setLinks(this.userInfoHateoas.getLinks());
		return userDTO;
	}
	
	@Autowired
	public UserInfoFactory(@Qualifier("userInfoService") UserMapper userInfoService,
						  @Qualifier("userInfoHateoas") Hateoas hateoas){
		this.userInfoService = userInfoService;
		this.userInfoHateoas = hateoas;
	}
	
}