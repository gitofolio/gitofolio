package com.gitofolio.api.service.proxy.userstat;

import org.springframework.beans.factory.annotation.Qualifier;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;
  
import com.gitofolio.api.service.factory.mapper.*;
import com.gitofolio.api.service.user.dtos.*;
import com.gitofolio.api.service.user.*;
import com.gitofolio.api.domain.user.*;

@ExtendWith(MockitoExtension.class)
public class UserStatCrudProxyTest{
	
	@Mock
	@Qualifier("userStatMapper")
	private UserMapper<UserStat> userStatMapper;
	
	@Mock
	@Qualifier("userStatService")
	private UserStatService userStatService;
	
	@InjectMocks
	private UserStatCrudProxy userStatCrudProxy;
	
	@InjectMocks
	private UserStatStringCrudProxy userStatStringCrudProxy;
	
	@BeforeEach
	public void setUpCrudProxy(){
		this.userStatCrudProxy.addProxy(this.userStatStringCrudProxy);
	}
	
	@Test
	public void UserStatCrudProxy_update_Test(){
		// given
		UserDTO userDTO = this.getUserDTO();
		
		// when
		given(this.userStatService.edit(any(UserStat.class))).willReturn(this.getUserStat());
		given(this.userStatMapper.doMap(any(UserStat.class))).willReturn(this.getUserDTO());
		given(this.userStatMapper.resolveMap(any(UserDTO.class))).willReturn(this.getUserStat());
		
		UserDTO result = this.userStatCrudProxy.update(userDTO);
		
		// then
		assertEquals(result.getName(), userDTO.getName());
	}
	
	@Test
	public void UserStatCrudProxy_read_Test(){
		// given
		UserDTO userDTO = this.getUserDTO();
		
		// when
		given(this.userStatService.get(any(String.class))).willReturn(this.getUserStat());
		given(this.userStatMapper.doMap(any(UserStat.class))).willReturn(this.getUserDTO());
		
		UserDTO result = this.userStatCrudProxy.read(userDTO.getName());
		
		// then
		assertEquals(result.getName(), userDTO.getName());
	}
	
	private UserDTO getUserDTO(){
		return new UserDTO.Builder()
			.userInfo(this.getUserInfo())
			.userStatDTO(this.getUserStatDTO())
			.build();
	}
	
	private UserStatDTO getUserStatDTO(){
		return new UserStatDTO.Builder()
			.userStat(this.getUserStat())
			.build();
	}
	
	private UserStat getUserStat(){
		UserStat userStat = new UserStat();
		userStat.setTotalVisitors(0);
		userStat.setUserInfo(this.getUserInfo());
		return userStat;
	}
	
	private UserInfo getUserInfo(){
		UserInfo userInfo = new UserInfo();
		userInfo.setId(0L);
		userInfo.setName("name");
		userInfo.setProfileUrl("url.helloworld.com");
		return userInfo;
	}
	
}