package com.gitofolio.api.service.user.factory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.UserMapper;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.factory.hateoas.Hateoas;

@Service
public class UserStatFactory implements UserFactory{
	
	private UserMapper userStatService;
	private UserFactory userInfoFactory;
	private Hateoas userStatHateoas;
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name){
		return setHateoas(this.userStatService.doMap(name));
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name, Object parameter){
		throw new IllegalStateException("/user/stat/{name}의 parameter요청은 허용되지 않았습니다.");
	}
	
	@Override
	@Transactional
	public UserDTO saveUser(UserDTO userDTO){
		this.userInfoFactory.saveUser(userDTO);
		return setHateoas(this.userStatService.resolveMap(userDTO));
	}
	
	public UserDTO setHateoas(UserDTO userDTO){
		userDTO.setLinks(this.userStatHateoas.getLinks());
		return userDTO;
	}
	
	@Autowired
	public UserStatFactory(@Qualifier("userStatService") UserMapper userStatService,
						  @Qualifier("userInfoFactory") UserFactory userInfoFactory,
						  @Qualifier("userStatHateoas") Hateoas userStatHateoas){
		this.userStatService = userStatService;
		this.userInfoFactory = userInfoFactory;
		this.userStatHateoas = userStatHateoas;
	}
	
}