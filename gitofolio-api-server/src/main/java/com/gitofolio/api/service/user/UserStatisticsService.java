package com.gitofolio.api.service.user;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.dtos.UserStatisticsDTO;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.dtos.UserStatisticsDTO.RefferingSiteDTO;
import com.gitofolio.api.service.user.dtos.UserStatisticsDTO.VisitorStatisticsDTO;
import com.gitofolio.api.repository.user.UserStatisticsRepository;
import com.gitofolio.api.repository.user.UserInfoRepository;
import com.gitofolio.api.domain.user.UserStatistics;
import com.gitofolio.api.domain.user.RefferingSite;
import com.gitofolio.api.domain.user.VisitorStatistics;
import com.gitofolio.api.domain.user.UserInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserStatisticsService implements UserMapper{
	
	private UserStatisticsRepository userStatisticsRepository;
	private UserInfoRepository userInfoRepository;
	
	@Override
	@Transactional(readOnly=true)
	public UserDTO doMap(String name){
		UserStatistics userStatistics = this.userStatisticsRepository.findByName(name).orElseThrow(()->new RuntimeException("임시 오류 메시지"));
		UserStatisticsDTO userStatisticsDTO = new UserStatisticsDTO.Builder()
			.userStatistics(userStatistics)
			.build();
		
		UserDTO userDTO = new UserDTO.Builder()
			.userInfo(userStatistics.getUserInfo())
			.userStatisticsDTO(userStatisticsDTO)
			.build();

		return userDTO;
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
		
		UserStatistics userStatistics = this.userStatisticsRepository.findByName(userInfo.getName()).orElseGet(()->new UserStatistics());
		if(userStatistics.getUserInfo() == null){
			userStatistics.setUserInfo(userInfo);
			userStatisticsRepository.save(userStatistics);	
		}
		
		UserStatisticsDTO userStatisticsDTO = userDTO.getUserStatistics();
			
			List<RefferingSiteDTO> refferingSiteDTOs = userStatisticsDTO.getRefferingSites();
			List<VisitorStatisticsDTO> visitorStatisticsDTOs = userStatisticsDTO.getVisitorStatistics();
			
			for(RefferingSiteDTO refferingSiteDTO : refferingSiteDTOs){
				userStatistics.setRefferingSite(
					refferingSiteDTO.getRefferingSiteName()
					, refferingSiteDTO.getRefferingDate()
				);
			}
			
			VisitorStatistics visitorStatistics = null;
			for(VisitorStatisticsDTO visitorStatisticsDTO : visitorStatisticsDTOs){
				if(visitorStatisticsDTO.getVisitDate().toString().equals(LocalDate.now().toString())){
					visitorStatistics = new VisitorStatistics(visitorStatisticsDTO.getVisitDate(), visitorStatisticsDTO.getVisitorCount());
					break;
				}
			}
			if(visitorStatistics == null) visitorStatistics = new VisitorStatistics(LocalDate.now(), 1);
		
			userStatistics.setVisitorStatistics(visitorStatistics.getVisitorCount());
		
		userStatistics.setUserInfo(userInfo);
		
		return this.doMap(userDTO.getName());
	}
	
	// constructor
	@Autowired
	public UserStatisticsService(UserStatisticsRepository userStatisticsRepository
								, UserInfoRepository userInfoRepository){
		this.userStatisticsRepository = userStatisticsRepository;
		this.userInfoRepository = userInfoRepository;
	}
	
}