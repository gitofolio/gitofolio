package com.gitofolio.api.service.user;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import com.gitofolio.api.domain.user.UserInfo;
import com.gitofolio.api.service.user.*;
import com.gitofolio.api.service.user.exception.*;

@SpringBootTest
public class UserInfoServiceTest{
	
	@Autowired
	private UserInfoService userInfoService;
	
	String name = "testName";
	
	@Test
	@Transactional
	public void UserInfoService_Save_and_Get_Test(){
		// given
		UserInfo userInfo = new UserInfo();
		userInfo.setId(0L);
		userInfo.setName(name);
		userInfo.setProfileUrl("url.helloworld.com");
		
		// when
		this.userInfoService.save(userInfo);
		
		// then
		UserInfo result = this.userInfoService.get(userInfo.getName());
		assertEquals(result.getName(), this.name);
		assertEquals(result.getProfileUrl(), userInfo.getProfileUrl());
	}
	
	@Test
	@Transactional
	public void UserInfoService_Get_NonExistUser_Fail_Test(){
		// given
		UserInfo userInfo = new UserInfo();
		userInfo.setId(0L);
		userInfo.setName(name);
		userInfo.setProfileUrl("url.helloworld.com");
		
		// when
		this.userInfoService.save(userInfo);
		
		// then
		assertThrows(NonExistUserException.class, ()->this.userInfoService.get(this.name+"1"));
	}
	
	@Test
	@Transactional
	public void UserInfo_Put_Test(){
		// given
		UserInfo userInfo = new UserInfo();
		userInfo.setId(0L);
		userInfo.setName(name);
		userInfo.setProfileUrl("url.helloworld.com");
		
		UserInfo editInfo = new UserInfo();
		editInfo.setId(0L);
		editInfo.setName(name);
		editInfo.setProfileUrl("edit.com");
		
		// when
		this.userInfoService.save(userInfo);
		this.userInfoService.edit(editInfo);
		
		// then 
		UserInfo result = this.userInfoService.get(userInfo.getName());
		assertEquals(result.getName(), this.name);
		assertEquals(result.getProfileUrl(), editInfo.getProfileUrl());
	}
	
	@AfterEach
	@Transactional
	public void pre_UserInfoService_delete_Test(){
		// given
		String userName = this.name;
		
		// when 
		try{
			this.userInfoService.delete(userName);
		}catch(NonExistUserException NEU){}
		
		// then
		assertThrows(NonExistUserException.class, ()->this.userInfoService.get(userName));
	}
	
	@BeforeEach
	@Transactional
	public void post_UserInfoService_delete_Test(){
		// given
		String userName = this.name;
		
		// when 
		try{
			this.userInfoService.delete(userName);
		}catch(NonExistUserException NEU){}
		
		// then
		assertThrows(NonExistUserException.class, ()->this.userInfoService.get(userName));
	}
}