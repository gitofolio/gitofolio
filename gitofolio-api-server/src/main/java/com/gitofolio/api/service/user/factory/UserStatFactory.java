package com.gitofolio.api.service.user.factory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.gitofolio.api.service.user.UserMapper;

@Service
public class UserStatFactory extends UserFactory{
	
	@Autowired
	public UserStatFactory(@Qualifier("userStatService") UserMapper userStatService){
		this.userMapper = userStatService;
	}
	
}