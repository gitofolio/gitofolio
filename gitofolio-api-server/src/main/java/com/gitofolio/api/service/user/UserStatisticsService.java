package com.gitofolio.api.service.user;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.dtos.UserStatisticsDTO.RefferingSiteDTO;
import com.gitofolio.api.service.user.dtos.UserStatisticsDTO.VisitorStatisticsDTO;
import com.gitofolio.api.service.user.exception.NonExistUserException;
import com.gitofolio.api.repository.user.UserStatisticsRepository;
import com.gitofolio.api.repository.user.UserInfoRepository;
import com.gitofolio.api.domain.user.UserStatistics;
import com.gitofolio.api.domain.user.RefferingSite;
import com.gitofolio.api.domain.user.VisitorStatistics;
import com.gitofolio.api.domain.user.UserInfo;
import com.gitofolio.api.domain.user.UserStatistics;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserStatisticsService{
	
	private UserStatisticsRepository userStatisticsRepository;
	private UserInfoRepository userInfoRepository;
	
	public UserStatistics get(String name){
		UserStatistics userStatistics = this.userStatisticsRepository.findByName(name).orElseGet(()->new UserStatistics());
		
		if(userStatistics.getUserInfo() == null){
			UserInfo userInfo = this.userInfoRepository.findByName(name)
				.orElseThrow(()->new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/user/dailystat/"));
			
			userStatistics.setUserInfo(userInfo);
			
			userStatisticsRepository.save(userStatistics);	
		}

		return userStatistics;
	}
	
	public void delete(String name){
		Optional<UserStatistics> optionalUserStatistics = this.userStatisticsRepository.findByName(name);
		if(optionalUserStatistics.isEmpty()) return;
		UserStatistics userStatistics = optionalUserStatistics.get();
		userStatistics.getVisitorStatistics().clear();
		userStatistics.getRefferingSites().clear();
		this.userStatisticsRepository.deleteByName(name);
		return;
	}
	
	public void increaseVisitorStatistics(String name){
		UserStatistics userStatistics = this.userStatisticsRepository.findByName(name).orElseGet(()->new UserStatistics());
		if(userStatistics.getUserInfo() == null){
			UserInfo userInfo = this.userInfoRepository.findByName(name)
				.orElseThrow(()->new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/user/dailystat/"));
			
			userStatistics.setUserInfo(userInfo); 
			
			userStatisticsRepository.save(userStatistics);	
		}
		
		userStatistics.addVisitorStatistics();
	}
	
	public void setRefferingSite(String name, String refferingSiteName){
		UserStatistics userStatistics = this.userStatisticsRepository.findByName(name).orElseGet(()->new UserStatistics());
		if(userStatistics.getUserInfo() == null){
			UserInfo userInfo = this.userInfoRepository.findByName(name)
				.orElseThrow(()->new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/user/dailystat/"));
			
			userStatistics.setUserInfo(userInfo);	
		}
		userStatistics.setRefferingSite(refferingSiteName);
		userStatisticsRepository.save(userStatistics);	
	}
	
	// constructor
	@Autowired
	public UserStatisticsService(UserStatisticsRepository userStatisticsRepository
								, UserInfoRepository userInfoRepository){
		this.userStatisticsRepository = userStatisticsRepository;
		this.userInfoRepository = userInfoRepository;
	}
	
}