package com.gitofolio.api.service.user.proxy;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import com.gitofolio.api.service.user.proxy.UserProxy;
import com.gitofolio.api.service.user.eraser.UserEraser;
import com.gitofolio.api.service.user.dtos.*;
import com.gitofolio.api.domain.user.*;
import com.gitofolio.api.service.user.exception.*;

import java.util.List;
import java.util.ArrayList;

@SpringBootTest
public class UserInfoProxyAndEraserTest{
	
	@Autowired
	@Qualifier("userInfoProxy")
	private UserProxy userInfoProxy;
	
	@Autowired
	@Qualifier("userInfoEraser")
	private UserEraser userInfoEraser;
	
	String name = "testName";
	String url = "testProfileUrl";
	
	@Test
	public void UserInfoProxy_Save_and_Get_Test(){
		// given
		UserDTO userDTO = new UserDTO.Builder()
			.id(0L)
			.name(name)
			.profileUrl(url)
			.build();
		
		// when
		userInfoProxy.saveUser(userDTO);
		
		// then
		UserDTO ret = userInfoProxy.getUser(name);
		assertEquals(ret.getName(), this.name);
		assertEquals(ret.getProfileUrl(), this.url);
	}
	
	@Test
	public void UserInfoProxy_Edit_Test(){
		// given
		UserDTO userDTO = new UserDTO.Builder()
			.id(0L)
			.name(name)
			.profileUrl(url)
			.build();
		
		UserDTO editUserDTO = new UserDTO.Builder()
			.id(0L)
			.name(name)
			.profileUrl(url+"edit")
			.build();
		
		// when
		this.userInfoProxy.saveUser(userDTO);
		this.userInfoProxy.editUser(editUserDTO);
		
		// then
		UserDTO result = this.userInfoProxy.getUser(this.name);
		assertEquals(result.getName(), this.name);
		assertEquals(result.getProfileUrl(), this.url+"edit");
	}
	
	@AfterEach
	public void pre_UserInfoEraser_Delete_Test(){
		try{
			this.userInfoEraser.delete(this.name);
		}catch(NonExistUserException NUE){}
		
		assertThrows(NonExistUserException.class, ()->userInfoProxy.getUser(this.name));
	}
	
	@BeforeEach
	public void post_UserInfoEraser_Delete_Test(){
		try{
			this.userInfoEraser.delete(this.name);
		}catch(NonExistUserException NUE){}
		
		assertThrows(NonExistUserException.class, ()->userInfoProxy.getUser(this.name));
	}
	
}