package com.gitofolio.api.service.user.eraser;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.UserStatService;

@Service
public class UserStatEraser implements UserEraser{
	
	private UserStatService userStatService;
	
	@Override
	@Transactional
	public String deleteUser(String name){
		this.userStatService.deleteUserStat(name);
		return name;
	}
	
	@Autowired
	public UserStatEraser(UserStatService userStatService){
		this.userStatService = userStatService;
	}
	
}