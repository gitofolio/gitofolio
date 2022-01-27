package com.gitofolio.api.service.proxy.personalaccesstoken;

import org.springframework.beans.factory.annotation.Qualifier;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;

import com.gitofolio.api.service.auth.PersonalAccessTokenService;
import com.gitofolio.api.service.factory.mapper.UserMapper;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.domain.user.UserInfo;
import com.gitofolio.api.domain.auth.PersonalAccessToken;

@ExtendWith(MockitoExtension.class)
public class PersonalAccessTokenCrudProxyTest{
	
	private final Long tokenKey = 777L;
	
	@Mock
	private PersonalAccessTokenService personalAccessTokenService;
	
	@Mock
	@Qualifier("userInfoMapper")
	private UserMapper<UserInfo> userInfoMapper;
	
	@InjectMocks
	private PersonalAccessTokenCrudProxy personalAccessTokenCrudProxy;
	
	@InjectMocks
	private PersonalAccessTokenLongCrudProxy personalAccessTokenLongCrudProxy;
	
	@InjectMocks
	private PersonalAccessTokenUserDTOCrudProxy personalAccessTokenUserDTOCrudProxy;
	
	@InjectMocks
	private PersonalAccessTokenStringCrudProxy personalAccessTokenStringCrudProxy;
	
	@BeforeEach
	public void setUpCrudProxy(){
		this.personalAccessTokenCrudProxy.addProxy(personalAccessTokenLongCrudProxy);
		this.personalAccessTokenCrudProxy.addProxy(personalAccessTokenUserDTOCrudProxy);
		this.personalAccessTokenCrudProxy.addProxy(personalAccessTokenStringCrudProxy);
	}
	
	@Test
	public void personalAccessTokenCrudProxy_Long_read_Test(){
		// given
		PersonalAccessToken token = this.getToken();
		UserDTO userDTO = this.getUserDTO();
		
		// when
		given(this.personalAccessTokenService.get(any(Long.class))).willReturn(token);
		
		PersonalAccessToken result = this.personalAccessTokenCrudProxy.read(this.tokenKey);
		
		// then
		assertEquals(result.getTokenValue(), token.getTokenValue());
		assertEquals(result.getUserInfo().getId(), token.getUserInfo().getId());
	}
	
	@Test
	public void personalAccessTokenCrudProxy_String_read_Test(){
		// given
		PersonalAccessToken token = this.getToken();
		UserDTO userDTO = this.getUserDTO();
		
		// when
		given(this.personalAccessTokenService.get(any(String.class))).willReturn(token);
		
		PersonalAccessToken result = this.personalAccessTokenCrudProxy.read(token.getTokenValue());
		
		// then
		assertEquals(result.getTokenValue(), token.getTokenValue());
		assertEquals(result.getUserInfo().getId(), token.getUserInfo().getId());
	}
	
	@Test
	public void personalAccessTokenCrudProxy_UserDTO_create_Test(){
		// given
		PersonalAccessToken token = this.getToken();
		UserDTO userDTO = this.getUserDTO();
		
		// when
		given(this.personalAccessTokenService.save(any(UserInfo.class))).willReturn(token);
		given(this.userInfoMapper.resolveMap(any(UserDTO.class))).willReturn(this.getUserInfo());
		
		PersonalAccessToken result = this.personalAccessTokenCrudProxy.create(userDTO);
			
		// then
		assertEquals(result.getTokenValue(), token.getTokenValue());
		assertEquals(result.getUserInfo().getId(), token.getUserInfo().getId());
	}
	
	private UserDTO getUserDTO(){
		return new UserDTO.Builder()
			.userInfo(this.getUserInfo())
			.build();
	}
	
	private UserInfo getUserInfo(){
		UserInfo userInfo = new UserInfo();
		userInfo.setId(1L);
		userInfo.setProfileUrl("testUrl");
		userInfo.setName("test");
		return userInfo;
	}
	
	private PersonalAccessToken getToken(){
		PersonalAccessToken token = new PersonalAccessToken();
		token.setTokenKey(this.tokenKey);
		token.setUserInfo(this.getUserInfo());
		return token;
	}
	
}