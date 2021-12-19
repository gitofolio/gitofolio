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
public class UserStatFactoryAndEraserTest{
	
	@Autowired
	@Qualifier("userStatFactory")
	private UserFactory userStatFactory;
	
	@Autowired
	@Qualifier("userInfoFactory")
	private UserFactory userInfoFactory;
	
	@Autowired
	@Qualifier("userInfoEraser")
	private UserEraser userInfoEraser;
	
	String name = "testName";
	String url = "testProfileUrl";
	
	@Test
	public void userStatFactory_Save_and_Get_Test(){
		// given
		UserDTO userDTO = new UserDTO.Builder()
			.id(0L)
			.name(this.name)
			.profileUrl(this.url)
			.build();
		
		// when
		userInfoFactory.saveUser(userDTO);
		
		// then
		UserDTO resultUserDTO = userStatFactory.getUser(this.name);
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
		
		assertThrows(NonExistUserException.class, ()->userStatFactory.getUser(this.name));
	}
	
	@BeforeEach
	public void post_UserStatEraser_Delete_Test(){
		try{
			this.userInfoEraser.delete(this.name);
		}catch(NonExistUserException NUE){}
		
		assertThrows(NonExistUserException.class, ()->userStatFactory.getUser(this.name));
	}
	
}