package com.gitofolio.api.service.user;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.dtos.UserStatisticsDTO;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.dtos.UserStatisticsDTO.RefferingSiteDTO;
import com.gitofolio.api.service.user.dtos.UserStatisticsDTO.VisitorStatisticsDTO;
import com.gitofolio.api.service.user.exception.NonExistUserException;
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
	public UserDTO doMap(String name){
		UserStatistics userStatistics = this.userStatisticsRepository.findByName(name).orElseThrow(()->new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/user/statistics/"+name));
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
	public UserDTO resolveMap(UserDTO userDTO){
		UserInfo userInfo = this.userInfoRepository.findByName(userDTO.getName()).orElseThrow(()->new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/user/statistics/"));
		
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
	
	public void deleteUserStatistics(String name){
		this.userStatisticsRepository.deleteByName(name);
		return;
	}
	
	// constructor
	@Autowired
	public UserStatisticsService(UserStatisticsRepository userStatisticsRepository
								, UserInfoRepository userInfoRepository){
		this.userStatisticsRepository = userStatisticsRepository;
		this.userInfoRepository = userInfoRepository;
	}
	
}