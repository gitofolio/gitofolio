package com.gitofolio.api.service.user;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.exception.NonExistUserException;
import com.gitofolio.api.service.user.exception.DuplicationUserException;
import com.gitofolio.api.repository.user.UserInfoRepository;
import com.gitofolio.api.domain.user.UserInfo;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserInfoService implements UserMapper{
	
	private UserInfoRepository userInfoRepository;
	
	@Override
	public UserDTO doMap(String name){
		UserInfo user = this.userInfoRepository.findByName(name).orElseThrow(()->new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/user/"+name));
		return new UserDTO.Builder()
			.userInfo(user)
			.build();
	}
	
	@Override
	public UserDTO resolveMap(UserDTO userDTO){
		UserInfo userInfo = this.userInfoRepository.findByName(userDTO.getName()).orElseGet(()->new UserInfo());
		if(userInfo.getName() == null){
			userInfo.setName(userDTO.getName());
			userInfo.setProfileUrl(userDTO.getProfileUrl());
			userInfoRepository.save(userInfo);
		}
		else if(userInfo.getName() != null) throw new DuplicationUserException("유저 이름이 중복되었습니다.", "다른 유저이름을 사용해 저장하세요", "/user/"+userInfo.getName());
			
		return this.doMap(userDTO.getName());
	}
	
	public void deleteUserInfo(String name){
		this.userInfoRepository.deleteByName(name);
		return;
	}
	
	// constructor
	@Autowired
	public UserInfoService(UserInfoRepository userInfoRepository){
		this.userInfoRepository = userInfoRepository;
	}
	
}