package com.gitofolio.api.service.user.factory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.UserMapper;
import com.gitofolio.api.service.user.dtos.UserDTO;

@Service
public class UserStatisticsFactory extends UserFactory{
	
	@Autowired
	public UserStatisticsFactory(@Qualifier("userStatisticsService") UserMapper userStatisticsService){
		this.userMapper = userStatisticsService;
	}
	
}