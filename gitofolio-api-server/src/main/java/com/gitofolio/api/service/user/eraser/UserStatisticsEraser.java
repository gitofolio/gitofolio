package com.gitofolio.api.service.user.eraser;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.UserStatisticsService;

@Service
public class UserStatisticsEraser implements UserEraser{
	
	private UserStatisticsService userStatisticsService;
	
	@Override
	@Transactional
	public String delete(String name){
		this.userStatisticsService.deleteUserStatistics(name);
		return name;
	}
	
	@Autowired
	public UserStatisticsEraser(UserStatisticsService userStatisticsService){
		this.userStatisticsService = userStatisticsService;
	}
	
}