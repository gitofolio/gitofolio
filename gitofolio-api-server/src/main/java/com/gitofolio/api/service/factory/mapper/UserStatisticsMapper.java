package com.gitofolio.api.service.factory.mapper;

import com.gitofolio.api.domain.user.UserInfo;
import com.gitofolio.api.domain.user.UserStatistics;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.dtos.UserStatisticsDTO;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserStatisticsMapper implements UserMapper<UserStatistics>{
	
	@Override
	public UserDTO doMap(UserStatistics userStatistics){
		return this.setUserStatisticsDTO(
			this.setUserDTO(userStatistics), userStatistics
		);
	}
	
	@Override
	public UserStatistics resolveMap(UserDTO userDTO){
		throw new IllegalStateException("userStatisticsMapper의 resolveMap은 허용되지 않았습니다.");
	}
	
	private UserDTO setUserDTO(UserStatistics userStatistics){
		UserInfo userInfo = userStatistics.getUserInfo();
		
		UserDTO userDTO = new UserDTO.Builder()
			.id(userInfo.getId())
			.name(userInfo.getName())
			.profileUrl(userInfo.getProfileUrl())
			.build();
		
		return userDTO;
	}
	
	private UserDTO setUserStatisticsDTO(UserDTO userDTO, UserStatistics userStatistics){
		
		UserStatisticsDTO userStatisticsDTO = new UserStatisticsDTO.Builder()
			.userStatistics(userStatistics)
			.build();
		
		userDTO.setUserStatistics(userStatisticsDTO);
		return userDTO;
	}
	
}