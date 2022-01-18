package com.gitofolio.api.service.proxy;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.gitofolio.api.service.factory.CrudFactory;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.exception.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserInfoCrudProxyTest{

	private CrudProxy<UserDTO> crudProxy;
	
	@Test
	public void userInfoCrudProxy_read_Test(){
		// when
		UserDTO user = crudProxy.read("name");
		UserDTO originUser = this.getUser();
		
		// then
		assertEquals(user.getName(), originUser.getName());
		assertEquals(user.getProfileUrl(), originUser.getProfileUrl());
	}
	
	@Test
	public void userInfoCrudProxy_create_and_delete_Test(){
		// given
		UserDTO originUser = this.getUser();
		originUser.setName("new User");
		
		// when
		this.crudProxy.create(originUser);
		UserDTO user = crudProxy.read("new User");
		// then
		assertEquals(originUser.getName(), user.getName());
		
		// when
		this.crudProxy.delete("new User");
		
		// then
		assertThrows(NonExistUserException.class, ()->this.crudProxy.read("new User"));
	}
	
	@BeforeEach
	public void preInit(){
		UserDTO user = this.getUser();
		try{
			this.crudProxy.delete(user.getName());
		} catch(NonExistUserException NEUE){}
		try{
			this.crudProxy.create(user);
		}catch(DuplicationUserException DUE){}
		UserDTO result = this.crudProxy.read(user.getName());
		assertEquals(user.getName(), result.getName());
	}
	
	@AfterEach
	public void postInit(){
		UserDTO user = this.getUser();
		try{
			this.crudProxy.delete(user.getName());
		} catch(NonExistUserException NEUE){}
		try{
		}catch(DuplicationUserException DUE){DUE.printStackTrace();}
	}
	
	private UserDTO getUser(){
		UserDTO user = new UserDTO.Builder()
			.id(0L)
			.name("name")
			.profileUrl("https://example.profileUrl.com?1123u8413478")
			.build();
		return user;
	}
	
	@Autowired
	public UserInfoCrudProxyTest(@Qualifier("userInfoCrudFactory") CrudFactory<UserDTO> userInfoCrudFactory){
		this.crudProxy = userInfoCrudFactory.get();
	}
	
}