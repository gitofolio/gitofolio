package com.gitofolio.api.service.user;

import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;

import com.gitofolio.api.domain.user.*;
import com.gitofolio.api.service.user.*;
import com.gitofolio.api.service.user.exception.*;
import com.gitofolio.api.repository.user.*;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserStatisticsServiceTest{
	
	@Mock
	private UserStatisticsRepository userStatisticsRepository;
	@Mock
	private UserInfoRepository userInfoRepository;
	
	@InjectMocks
	private UserStatisticsService userStatisticsService;
	
	@Test
	@Transactional
	public void UserStatisticsService_Get_Test(){
		// given
		UserStatistics userStatistics = this.getUserStatistics();
		
		// when
		given(this.userStatisticsRepository.findByName(any(String.class))).willReturn(Optional.ofNullable(userStatistics));
		
		UserStatistics result = this.userStatisticsService.get(userStatistics.getUserInfo().getName());
		
		// then
		assertEquals(result.getVisitorStatistics().size(), 1);
		assertEquals(result.getVisitorStatistics().get(0).getVisitorCount(), 1);
		assertEquals(result.getVisitorStatistics().get(0).getVisitDate().toString(), LocalDate.now().toString());
	}
	
	@Test
	@Transactional
	public void UserStatisticsService_Get_Fail_Test(){
		// given
		UserStatistics userStatistics = this.getUserStatistics();
		
		// when
		given(this.userStatisticsRepository.findByName(any(String.class))).willReturn(Optional.ofNullable(null));
		given(this.userInfoRepository.findByName(any(String.class))).willReturn(Optional.ofNullable(null));
		
		// then
		assertThrows(NonExistUserException.class, ()->this.userStatisticsService.get(userStatistics.getUserInfo().getName()));
	}

	private UserStatistics getUserStatistics(){
		UserStatistics userStatistics = new UserStatistics();
		userStatistics.setUserInfo(this.getUserInfo());
		userStatistics.setRefferingSite("reffering_site_1");
		userStatistics.addVisitorStatistics();
		return userStatistics;
	}
	
	private UserInfo getUserInfo(){
		UserInfo userInfo = new UserInfo();
		userInfo.setId(0L);
		userInfo.setName("name");
		userInfo.setProfileUrl("url.helloworld.com");
		return userInfo;
	}
	
}