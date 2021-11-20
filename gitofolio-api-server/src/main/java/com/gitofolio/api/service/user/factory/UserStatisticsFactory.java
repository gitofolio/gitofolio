package com.gitofolio.api.service.user.factory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.UserMapper;
import com.gitofolio.api.service.user.dtos.UserDTO;

@Service
public class UserStatisticsFactory implements UserFactory{
	
	private UserMapper userStatisticsService;
	private UserFactory userInfoFactory;
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name){
		return this.userStatisticsService.doMap(name);
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
		return this.userStatisticsService.resolveMap(userDTO);
	}
	
	@Autowired
	public UserStatisticsFactory(@Qualifier("userStatisticsService") UserMapper userStatisticsService,
								@Qualifier("userInfoFactory") UserFactory userInfoFactory){
		this.userStatisticsService = userStatisticsService;
		this.userInfoFactory = userInfoFactory;
	}
	
}