package com.gitofolio.api.service.proxy.userinfo;

import org.springframework.beans.factory.annotation.Qualifier;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;

import com.gitofolio.api.service.user.dtos.*;
import com.gitofolio.api.service.factory.mapper.UserMapper;
import com.gitofolio.api.service.user.*;
import com.gitofolio.api.domain.user.*;

@ExtendWith(MockitoExtension.class)
public class UserInfoCrudProxyTest{
	
	@Mock
	private UserInfoService userInfoService;
	
	@Mock
	@Qualifier("userInfoMapper")
	private UserMapper<UserInfo> userInfoMapper;
	
	@InjectMocks
	private UserInfoCrudProxy userInfoCrudProxy;
	
	@InjectMocks
	private UserInfoStringCrudProxy userInfoStringCrudProxy;
	
	@BeforeEach
	public void setUpCrudProxy(){
		this.userInfoCrudProxy.addProxy(userInfoStringCrudProxy);
	}
	
	@Test
	public void userInfoCrudProxy_create_Test(){
		// given
		UserDTO userDTO = this.getUserDTO();
		
		// when
		given(this.userInfoService.save(any(UserInfo.class))).willReturn(this.getUserInfo());
		given(this.userInfoMapper.doMap(any(UserInfo.class))).willReturn(userDTO);
		given(this.userInfoMapper.resolveMap(any(UserDTO.class))).willReturn(this.getUserInfo());
		
		UserDTO result = this.userInfoCrudProxy.create(userDTO);
		
		// then
		assertEquals(result.getName(), userDTO.getName());
	}
	
	@Test
	public void userInfoCrudProxy_update_Test(){
		// given
		UserDTO userDTO = this.getUserDTO();
		
		// when
		given(this.userInfoService.edit(any(UserInfo.class))).willReturn(this.getUserInfo());
		given(this.userInfoMapper.doMap(any(UserInfo.class))).willReturn(userDTO);
		given(this.userInfoMapper.resolveMap(any(UserDTO.class))).willReturn(this.getUserInfo());
		
		UserDTO result = this.userInfoCrudProxy.update(userDTO);
		
		// then
		assertEquals(result.getName(), userDTO.getName());
	}
	
	@Test
	public void userInfoCrudProxy_read_Test(){
		// given
		UserDTO userDTO = this.getUserDTO();
		
		// when
		given(this.userInfoService.get(any(String.class))).willReturn(this.getUserInfo());
		given(this.userInfoMapper.doMap(any(UserInfo.class))).willReturn(userDTO);
		
		UserDTO result = this.userInfoCrudProxy.read(userDTO.getName());
		
		// then
		assertEquals(result.getName(), userDTO.getName());
	}
	
	private UserDTO getUserDTO(){
		return new UserDTO.Builder()
			.userInfo(this.getUserInfo())
			.build();
	}
	
	private UserInfo getUserInfo(){
		UserInfo userInfo = new UserInfo();
		userInfo.setId(0L);
		userInfo.setName("name");
		userInfo.setProfileUrl("url.helloworld.com");
		return userInfo;
	}
	
}