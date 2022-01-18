package com.gitofolio.api.service.factory.mapper;

import com.gitofolio.api.domain.user.UserInfo;
import com.gitofolio.api.domain.user.UserStat;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.dtos.UserStatDTO;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserStatMapper implements UserMapper<UserStat>{
	
	@Override
	public UserDTO doMap(UserStat userStat){
		
		UserStatDTO userStatDTO = new UserStatDTO.Builder()
			.userStat(userStat)
			.build();
		
		return new UserDTO.Builder()
			.userInfo(userStat.getUserInfo())
			.userStatDTO(userStatDTO)
			.build();
	}
	
	@Override
	public UserStat resolveMap(UserDTO userDTO){
		UserStat userStat = new UserStat();
		userStat.setTotalVisitors(1);
		userStat.setTotalStars(userDTO.getUserStat().getTotalStars());
		
		UserInfo userInfo = new UserInfo();
		userInfo.setName(userDTO.getName());
		userInfo.setProfileUrl(userDTO.getProfileUrl());
		userStat.setUserInfo(userInfo);
		
		return userStat;
	}
	
}