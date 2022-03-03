package com.gitofolio.api.service.proxy.userstatistics;

import org.springframework.beans.factory.annotation.Qualifier;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import static org.mockito.BDDMockito.*;

import com.gitofolio.api.domain.user.*;
import com.gitofolio.api.service.user.*;
import com.gitofolio.api.service.user.dtos.*;
import com.gitofolio.api.service.factory.mapper.*;

@ExtendWith(MockitoExtension.class)
public class UserStatisticsCrudProxyTest{
	
	@Mock
	@Qualifier("userStatisticsMapper")
	private UserMapper<UserStatistics> userStatisticsMapper;
	
	@Mock
	private UserStatisticsService userStatisticsService;
	
	@InjectMocks
	private UserStatisticsCrudProxy userStatisticsCrudProxy;
	
	@InjectMocks
	private UserStatisticsStringCrudProxy userStatisticsStringCrudProxy;
	
	@BeforeEach
	public void setUpCrudProxy(){
		this.userStatisticsCrudProxy.addProxy(this.userStatisticsStringCrudProxy);
	}
	
	@Test
	public void UserStatisticsCrudProxy_read_Test(){
		// given
		UserDTO userDTO = this.getUserDTO();
		
		// when
		given(this.userStatisticsService.get(any(String.class))).willReturn(this.getUserStatistics());
		given(this.userStatisticsMapper.doMap(any(UserStatistics.class))).willReturn(userDTO);
		
		UserDTO result = this.userStatisticsCrudProxy.read(userDTO.getName());
		
		// then
		assertEquals(result.getName(), userDTO.getName());
	}
	
	private UserDTO getUserDTO(){
		return new UserDTO.Builder()
			.userInfo(this.getUserInfo())
			.userStatisticsDTO(this.getUserStatisticsDTO())
			.build();
	}
	
	private UserStatisticsDTO getUserStatisticsDTO(){
		return new UserStatisticsDTO.Builder()
			.userStatistics(this.getUserStatistics())
			.build();
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