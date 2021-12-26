package com.gitofolio.api.service.user;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import com.gitofolio.api.service.user.EncodedProfileImageService;
import com.gitofolio.api.service.user.UserInfoService;
import com.gitofolio.api.service.user.exception.NonExistUserException;
import com.gitofolio.api.domain.user.EncodedProfileImage;
import com.gitofolio.api.domain.user.UserInfo;

@SpringBootTest
public class EncodedProfileImageServiceTest{
	
	@Autowired
	private EncodedProfileImageService encodedProfileImageService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	private String name = "devxb";
	
	@Test
	@Transactional
	public void 이미지인코딩_SAVE_AND_GET_TEST(){
		// given
		UserInfo userInfo = new UserInfo();
		userInfo.setId(62425964L);
		userInfo.setName("devxb");
		userInfo.setProfileUrl("https://avatars.githubusercontent.com/u/62425964?v=4");
		
		// when
		this.userInfoService.save(userInfo);
		EncodedProfileImage encodedProfileImage = this.encodedProfileImageService.save(userInfo);
		
		// then
		EncodedProfileImage result = this.encodedProfileImageService.get(userInfo);
		
		assertEquals(result.getProfileUrl(), userInfo.getProfileUrl());
	}
	
	@AfterEach
	@Transactional
	public void pre_UserInfoService_delete_Test(){
		// given
		String userName = this.name;
		
		// when 
		try{
			this.encodedProfileImageService.delete(userName);
		}catch(NonExistUserException NEU){}
		
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
			this.encodedProfileImageService.delete(userName);
		}catch(NonExistUserException NEU){}
		
		try{
			this.userInfoService.delete(userName);
		}catch(NonExistUserException NEU){}
		
		// then
		assertThrows(NonExistUserException.class, ()->this.userInfoService.get(userName));
	}
	
}