package com.gitofolio.api.service.user.factory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.UserMapper;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.factory.hateoas.Hateoas;

@Service
public class UserStatisticsFactory implements UserFactory{
	
	private UserMapper userStatisticsService;
	private UserFactory userInfoFactory;
	private Hateoas userStatisticsHateoas;
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name){
		return this.setHateoas(this.userStatisticsService.doMap(name));
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name, Object parameter){
		throw new IllegalStateException("/user/statistics/{name}의 parameter요청은 허용되지 않았습니다.");
	}
	
	@Override
	@Transactional
	public UserDTO saveUser(UserDTO userDTO){
		this.userInfoFactory.saveUser(userDTO);
		return this.setHateoas(this.userStatisticsService.resolveMap(userDTO));
	}
	
	@Override
	public UserDTO setHateoas(UserDTO userDTO){
		userDTO.setLinks(this.userStatisticsHateoas.getLinks());
		return userDTO;
	}
	
	@Autowired
	public UserStatisticsFactory(@Qualifier("userStatisticsService") UserMapper userStatisticsService,
								@Qualifier("userInfoFactory") UserFactory userInfoFactory,
								@Qualifier("userStatisticsHateoas") Hateoas userStatisticsHateoas){
		this.userStatisticsService = userStatisticsService;
		this.userInfoFactory = userInfoFactory;
		this.userStatisticsHateoas = userStatisticsHateoas;
	}
	
}