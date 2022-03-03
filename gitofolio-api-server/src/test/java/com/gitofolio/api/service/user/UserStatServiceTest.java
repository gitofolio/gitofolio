package com.gitofolio.api.service.user;


import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;

import com.gitofolio.api.domain.user.*;
import com.gitofolio.api.service.user.*;
import com.gitofolio.api.service.user.exception.*;
import com.gitofolio.api.repository.user.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserStatServiceTest{
	
	@Mock
	private UserStatRepository userStatRepository;
	@Mock
	private UserInfoRepository userInfoRepository;

	@InjectMocks
	private UserStatService userStatService;
	
	@Test
	@Transactional
	public void UserStatService_GET_Test(){
		// given
		UserInfo userInfo = this.getUserInfo();
		UserStat userStat = this.getUserStat(userInfo);
		
		// when
		given(this.userStatRepository.findByName(any(String.class))).willReturn(Optional.ofNullable(this.getUserStat(userInfo)));
		
		UserStat result = this.userStatService.get(userStat.getUserInfo().getName());
		
		// then
		assertEquals(result.getId(), userStat.getId());
	}

	@Test
	@Transactional
	public void UserStatService_Get_NonExistUser_Fail_Test(){
		// given
		UserInfo userInfo = this.getUserInfo();
		UserStat userStat = this.getUserStat(userInfo);
		
		// when
		given(this.userInfoRepository.findByName(any(String.class))).willReturn(Optional.ofNullable(null));
		given(this.userStatRepository.findByName(any(String.class))).willReturn(Optional.ofNullable(null));

		
		// then
		assertThrows(NonExistUserException.class, ()->this.userStatService.get(userStat.getUserInfo().getName()));
	}
	
	private UserStat getUserStat(UserInfo userInfo){
		UserStat userStat = new UserStat();
		userStat.setTotalVisitors(0);
		userStat.setUserInfo(userInfo);
		
		return userStat;
	}
	
	private UserInfo getUserInfo(){
		UserInfo userInfo = new UserInfo();
		userInfo.setId(0L);
		userInfo.setName("name");
		userInfo.setProfileUrl("url.helloworld.com");
		
		return userInfo;
	}
	
}