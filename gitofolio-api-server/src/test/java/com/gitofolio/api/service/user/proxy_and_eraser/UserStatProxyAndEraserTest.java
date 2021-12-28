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
public class UserStatProxyAndEraserTest{
	
	@Autowired
	@Qualifier("userStatProxy")
	private UserProxy userStatProxy;
	
	@Autowired
	@Qualifier("userInfoProxy")
	private UserProxy userInfoProxy;
	
	@Autowired
	@Qualifier("userInfoEraser")
	private UserEraser userInfoEraser;
	
	String name = "testName";
	String url = "testProfileUrl";
	
	@Test
	public void userStatProxy_Save_and_Get_Test(){
		// given
		UserDTO userDTO = new UserDTO.Builder()
			.id(0L)
			.name(this.name)
			.profileUrl(this.url)
			.build();
		
		// when
		userInfoProxy.saveUser(userDTO);
		
		// then
		UserDTO resultUserDTO = userStatProxy.getUser(this.name);
		UserStatDTO resultUserStatDTO = resultUserDTO.getUserStat();
		
		assertEquals(resultUserDTO.getName(), this.name);
		assertEquals(resultUserDTO.getProfileUrl(), this.url);
		assertEquals(resultUserStatDTO.getTotalVisitors(), 1);
		assertEquals(resultUserStatDTO.getTotalStars(), 0);
	}
	
	@AfterEach
	public void pre_UserStatEraser_Delete_Test(){
		try{
			this.userInfoEraser.delete(this.name);
		}catch(NonExistUserException NUE){}
		
		assertThrows(NonExistUserException.class, ()->userStatProxy.getUser(this.name));
	}
	
	@BeforeEach
	public void post_UserStatEraser_Delete_Test(){
		try{
			this.userInfoEraser.delete(this.name);
		}catch(NonExistUserException NUE){}
		
		assertThrows(NonExistUserException.class, ()->userStatProxy.getUser(this.name));
	}
	
}