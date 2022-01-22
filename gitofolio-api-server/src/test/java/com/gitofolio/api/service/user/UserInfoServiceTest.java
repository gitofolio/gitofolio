package com.gitofolio.api.service.user;

import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;

import com.gitofolio.api.domain.user.UserInfo;
import com.gitofolio.api.service.user.*;
import com.gitofolio.api.service.user.exception.*;
import com.gitofolio.api.repository.user.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserInfoServiceTest{
	
	@Mock
	private UserInfoRepository userInfoRepository;
	@Mock
	private UserStatRepository userStatRepository;
	@Mock
	private UserStatisticsService userStatisticsService;
	@Mock
	private PortfolioCardRepository portfolioCardRepository;
	@Mock
	private EncodedProfileImageRepository encodedProfileImageRepository;
	
	@InjectMocks
	private UserInfoService userInfoService;
	
	@Test
	@Transactional
	public void UserInfoService_GET_Test(){
		// given
		UserInfo userInfo = this.getUser();
		// when
		given(this.userInfoRepository.findByName(any(String.class))).willReturn(Optional.ofNullable(userInfo));
		UserInfo result = this.userInfoService.get(userInfo.getName());
		
		// then
		assertEquals(result.getName(), userInfo.getName());
		assertEquals(result.getProfileUrl(), userInfo.getProfileUrl());
	}
	
	@Test
	@Transactional
	public void UserInfoService_GET_NonExistUser_Fail_Test(){
		// given
		UserInfo userInfo = this.getUser();
		
		// when
		given(this.userInfoRepository.findByName(any(String.class))).willReturn(Optional.ofNullable(null));
		
		// then
		assertThrows(NonExistUserException.class, ()->this.userInfoService.get(userInfo.getName()));
	}
	
	@Test
	@Transactional
	public void UserInfo_EDIT_Test(){
		// given
		UserInfo userInfo = this.getUser();
		UserInfo editInfo = this.getUser();
		editInfo.setName("edited!");
		
		// when
		given(this.userInfoRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(userInfo));
		given(this.userInfoRepository.findByName(any(String.class))).willReturn(Optional.ofNullable(editInfo));
		UserInfo result = this.userInfoService.edit(editInfo);
		
		// then 
		assertEquals(result.getName(), editInfo.getName());
		assertEquals(result.getProfileUrl(), editInfo.getProfileUrl());
	}
	
	private UserInfo getUser(){
		UserInfo userInfo = new UserInfo();
		userInfo.setId(0L);
		userInfo.setName("name");
		userInfo.setProfileUrl("url.helloworld.com");
		return userInfo;
	}
	
}