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
public class UserStatisticsProxyAndEraserTest{
	
	@Autowired
	@Qualifier("userInfoProxy")
	private UserProxy userInfoProxy;
	
	@Autowired
	@Qualifier("userStatisticsProxy")
	private UserProxy userStatisticsProxy;
	
	@Autowired
	@Qualifier("userInfoEraser")
	private UserEraser userInfoEraser;
	
	String name = "testName";
	String url = "testProfileUrl";
	
	@Test
	public void UserStatisticsProxy_Save_and_Get_Test(){
		// given
		UserDTO userDTO = new UserDTO.Builder()
			.id(0L)
			.name(this.name)
			.profileUrl(this.url)
			.build();
		
		// when
		userInfoProxy.saveUser(userDTO);
		
		// then
		UserDTO retUserDTO = this.userStatisticsProxy.getUser(this.name); 
		UserStatisticsDTO retUserStatisticsDTO = retUserDTO.getUserStatistics();
		assertEquals(retUserDTO.getName(), this.name);
		assertEquals(retUserDTO.getProfileUrl(), this.url);
		assertEquals(retUserStatisticsDTO.getRefferingSites().size(), 0);
		assertEquals(retUserStatisticsDTO.getVisitorStatistics().size(), 0);
	}
	
	@AfterEach
	public void pre_UserStatisticsEraser_Delete_Test(){
		try{
			this.userInfoEraser.delete(this.name);
		}catch(NonExistUserException NUE){}
		
		assertThrows(NonExistUserException.class, ()->userStatisticsProxy.getUser(this.name));
	}
	
	@BeforeEach
	public void post_UserStatisticsEraser_Delete_Test(){
		try{
			this.userInfoEraser.delete(this.name);
		}catch(NonExistUserException NUE){}
		
		assertThrows(NonExistUserException.class, ()->userStatisticsProxy.getUser(this.name));
	}
	
}