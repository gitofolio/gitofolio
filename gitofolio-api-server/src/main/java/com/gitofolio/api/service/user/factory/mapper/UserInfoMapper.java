package com.gitofolio.api.service.user.factory.mapper;

import com.gitofolio.api.domain.user.UserInfo;
import com.gitofolio.api.service.user.dtos.UserDTO;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserInfoMapper implements UserMapper<UserInfo>{
	
	@Override
	public UserDTO doMap(UserInfo userInfo){
		return new UserDTO.Builder()
			.userInfo(userInfo)
			.build();
	}
	
	@Override
	public UserInfo resolveMap(UserDTO userDTO){
		UserInfo userInfo = new UserInfo();
		userInfo.setName(userDTO.getName());
		userInfo.setProfileUrl(userDTO.getProfileUrl());
		return userInfo;
	}
	
}