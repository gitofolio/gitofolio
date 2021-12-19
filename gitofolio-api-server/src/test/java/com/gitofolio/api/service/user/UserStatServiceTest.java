package com.gitofolio.api.service.user;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import com.gitofolio.api.domain.user.*;
import com.gitofolio.api.service.user.*;
import com.gitofolio.api.service.user.exception.*;

@SpringBootTest
public class UserStatServiceTest{
	
	@Autowired
	private UserStatService userStatService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	String name = "testName";
	
	@Test
	@Transactional
	public void UserStatService_Get_and_Save_Test(){
		// given
		UserInfo userInfo = new UserInfo();
		userInfo.setId(0L);
		userInfo.setName(this.name);
		userInfo.setProfileUrl("url.helloworld.com");
		
		this.userInfoService.save(userInfo);
		
		// when
		this.userStatService.increaseTotalVisitors(this.name);
		this.userStatService.increaseTotalStars(this.name);
		
		// then
		UserStat resultUserStat = this.userStatService.get(this.name);
		
		UserInfo resultUserInfo = resultUserStat.getUserInfo();
		
		assertEquals(resultUserInfo.getName(), this.name);
		assertEquals(resultUserStat.getTotalStars(), 1);
		assertEquals(resultUserStat.getTotalVisitors(), 1);
	}

	@Test
	@Transactional
	public void UserStatService_Get_NonExistUser_Fail_Test(){
		// given
		String userName = this.name + "1";
		
		// then
		assertThrows(NonExistUserException.class, ()->this.userStatService.increaseTotalStars(userName));
		assertThrows(NonExistUserException.class, ()->this.userStatService.increaseTotalVisitors(userName));
	}
	
	@AfterEach
	@Transactional
	public void pre_UserStatService_delete_Test(){
		// given
		String userName = this.name;
		
		// when 
		try{
			this.userStatService.delete(userName);
			this.userInfoService.delete(userName);
		}catch(NonExistUserException NEU){}
		
		// then
		assertThrows(NonExistUserException.class, ()->this.userInfoService.get(userName));
	}
	
	@BeforeEach
	@Transactional
	public void post_UserStatService_delete_Test(){
		// given
		String userName = this.name;
		
		// when 
		try{
			this.userStatService.delete(userName);
			this.userInfoService.delete(userName);
		}catch(NonExistUserException NEU){}
		
		// then
		assertThrows(NonExistUserException.class, ()->this.userInfoService.get(userName));
	}
	
}