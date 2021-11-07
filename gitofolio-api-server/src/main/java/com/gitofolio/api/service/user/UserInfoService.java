package com.gitofolio.api.service.user;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.repository.user.UserInfoRepository;
import com.gitofolio.api.domain.user.UserInfo;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserInfoService implements UserMapper{
	
	private UserInfoRepository userInfoRepository;
	
	@Override
	@Transactional(readOnly=true)
	public UserDTO doMap(String name){
		UserInfo user = userInfoRepository.findByName(name);
		return new UserDTO.Builder()
			.userInfo(user)
			.build();
	}
	
	@Override
	@Transactional
	public UserDTO resolveMap(UserDTO userDTO){
		UserInfo userInfo = new UserInfo();
		userInfo.setName(userDTO.getName());
		userInfo.setProfileUrl(userDTO.getProfileUrl());
		userInfoRepository.save(userInfo);
		
		return this.doMap(userDTO.getName());
	}
	
	// constructor
	@Autowired
	public UserInfoService(UserInfoRepository userInfoRepository){
		this.userInfoRepository = userInfoRepository;
	}
	
}