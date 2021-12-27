package com.gitofolio.api.service.user.factory;

import com.gitofolio.api.service.user.factory.mapper.UserMapper;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.EncodedProfileImageService;
import com.gitofolio.api.domain.user.UserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

@Service
public class EncodedProfileImageFactory{
	
	private UserMapper<UserInfo> userInfoMapper;
	private EncodedProfileImageService encodedProfileImageService;
	
	public String get(UserDTO userDTO){
		return this.encodedProfileImageService.get(
			this.userInfoMapper.resolveMap(userDTO)
		).getEncodedProfileUrl();
	}
	
	@Async
	public void save(UserDTO userDTO){
		this.encodedProfileImageService.save(
			this.userInfoMapper.resolveMap(userDTO)
		);
	}
	
	@Autowired
	public EncodedProfileImageFactory(@Qualifier("userInfoMapper") UserMapper<UserInfo> userInfoMapper,
									 EncodedProfileImageService encodedProfileImageService){
		this.userInfoMapper = userInfoMapper;
		this.encodedProfileImageService = encodedProfileImageService;
	}
	
}