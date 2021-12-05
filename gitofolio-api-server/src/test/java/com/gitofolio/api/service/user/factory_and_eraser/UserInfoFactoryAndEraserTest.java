package com.gitofolio.api.service.user.factory;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import com.gitofolio.api.service.user.factory.UserFactory;
import com.gitofolio.api.service.user.eraser.UserEraser;
import com.gitofolio.api.service.user.dtos.*;
import com.gitofolio.api.domain.user.*;
import com.gitofolio.api.service.user.exception.*;

import java.util.List;
import java.util.ArrayList;

@SpringBootTest
public class UserInfoFactoryAndEraserTest{
	
	@Autowired
	@Qualifier("userInfoFactory")
	private UserFactory userInfoFactory;
	
	@Autowired
	@Qualifier("userInfoEraser")
	private UserEraser userInfoEraser;
	
	String name = "testName";
	String url = "testProfileUrl";
	
	@Test
	public void UserInfoFactory_Save_and_Get_Test(){
		// given
		UserDTO userDTO = new UserDTO.Builder()
			.name(name)
			.profileUrl(url)
			.build();
		
		// when
		userInfoFactory.saveUser(userDTO);
		
		// then
		UserDTO ret = userInfoFactory.getUser(name);
		assertEquals(ret.getName(), this.name);
		assertEquals(ret.getProfileUrl(), this.url);
	}
	
	@AfterEach
	public void pre_UserInfoEraser_Delete_Test(){
		try{
			this.userInfoEraser.delete(this.name);
		}catch(NonExistUserException NUE){}
		
		assertThrows(NonExistUserException.class, ()->userInfoFactory.getUser(this.name));
	}
	
	@BeforeEach
	public void post_UserInfoEraser_Delete_Test(){
		try{
			this.userInfoEraser.delete(this.name);
		}catch(NonExistUserException NUE){}
		
		assertThrows(NonExistUserException.class, ()->userInfoFactory.getUser(this.name));
	}
	
}