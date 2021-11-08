package com.gitofolio.api.service.user;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.dtos.UserStatDTO;
import com.gitofolio.api.repository.user.UserStatRepository;
import com.gitofolio.api.repository.user.UserInfoRepository;
import com.gitofolio.api.domain.user.UserStat;
import com.gitofolio.api.domain.user.UserInfo;

@Service
public class UserStatService implements UserMapper{
	
	private UserStatRepository userStatRepository;
	private UserInfoRepository userInfoRepository;
	
	@Override
	@Transactional(readOnly=true)
	public UserDTO doMap(String name){
		UserStat userStat = this.userStatRepository.findByName(name).orElseThrow(()->new RuntimeException("임시 오류 메시지"));
		
		UserStatDTO userStatDTO = new UserStatDTO.Builder()
			.userStat(userStat)
			.build();
		
		return new UserDTO.Builder()
			.userInfo(userStat.getUserInfo())
			.userStatDTO(userStatDTO)
			.build();
	}
	
	@Override
	@Transactional
	public UserDTO resolveMap(UserDTO userDTO){
		UserInfo userInfo = this.userInfoRepository.findByName(userDTO.getName()).orElseGet(()->new UserInfo());
		if(userInfo.getName() == null){
			userInfo.setName(userDTO.getName());
			userInfo.setProfileUrl(userDTO.getProfileUrl());
			userInfoRepository.save(userInfo);
		}
		
		UserStatDTO userStatDTO = userDTO.getUserStat();
		
		UserStat userStat = this.userStatRepository.findByName(userInfo.getName()).orElseGet(()->new UserStat());
		if(userStat.getUserInfo() == null){
			userStat.setTotalVisitors(userStatDTO.getTotalVisitors());
			userStat.setTotalStars(userStatDTO.getTotalStars());
			userStat.setUserInfo(userInfo);
			
			userStatRepository.save(userStat);
		}
		
		return this.doMap(userDTO.getName());
	}
	
	@Autowired
	public UserStatService(UserStatRepository userStatRepository
						  , UserInfoRepository userInfoRepository){
		this.userStatRepository = userStatRepository;
		this.userInfoRepository = userInfoRepository;
	}
	
}