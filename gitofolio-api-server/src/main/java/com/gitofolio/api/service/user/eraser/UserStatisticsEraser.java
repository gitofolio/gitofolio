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
		this.userStatisticsService.delete(name);
		return name;
	}
	
	@Override
	public String delete(String name, Object parameter){
		throw new IllegalStateException("DELETE : /user/dailiystatistics의 parameter요청은 허용되지 않았습니다.");
	}
	
	@Autowired
	public UserStatisticsEraser(UserStatisticsService userStatisticsService){
		this.userStatisticsService = userStatisticsService;
	}
	
}