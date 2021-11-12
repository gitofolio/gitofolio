package com.gitofolio.api.service.user.eraser;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.UserInfoService;

@Service
public class UserInfoEraser implements UserEraser{
	
	private UserInfoService userInfoService;
	private UserEraser userStatEraser;
	private UserEraser userStatisticsEraser;
	private UserEraser portfolioCardEraser;
	
	@Override
	@Transactional
	public String delete(String name){
		this.userInfoService.deleteUserInfo(
			this.portfolioCardEraser.delete(
				this.userStatisticsEraser.delete(
					this.userStatEraser.delete(name))));
		return name;
	}
	
	@Autowired
	public UserInfoEraser(UserInfoService userInfoService,
						 @Qualifier("userStatEraser") UserEraser userStatEraser,
						 @Qualifier("userStatisticsEraser") UserEraser userStatisticsEraser,
						 @Qualifier("portfolioCardEraser") UserEraser portfolioCardEraser){
		this.userInfoService = userInfoService;
		this.userStatEraser = userStatEraser;
		this.userStatisticsEraser = userStatisticsEraser;
		this.portfolioCardEraser = portfolioCardEraser;
	}
	
}