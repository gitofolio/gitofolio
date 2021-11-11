package com.gitofolio.api.service.user;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

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
	public UserDTO resolveMap(UserDTO userDTO){
		UserInfo userInfo = this.userInfoRepository.findByName(userDTO.getName()).orElseThrow(()->new RuntimeException("임시 오류 메시지"));
		
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
	
	public void deleteUserStat(String name){
		this.userStatRepository.deleteByName(name);
		return;
	}
	
	@Autowired
	public UserStatService(UserStatRepository userStatRepository
						  , UserInfoRepository userInfoRepository){
		this.userStatRepository = userStatRepository;
		this.userInfoRepository = userInfoRepository;
	}
	
}