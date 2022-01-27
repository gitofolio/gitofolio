package com.gitofolio.api.repository.auth;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import com.gitofolio.api.domain.auth.*;
import com.gitofolio.api.domain.user.UserInfo;
import com.gitofolio.api.repository.user.*;

import java.util.Optional;
import java.util.NoSuchElementException;
import java.time.LocalDate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PersonalAccessTokenRepositoryTest{
	
	@Autowired
	private PersonalAccessTokenRepository personalAccessTokenRepository;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Test
	public void PersonalAccessTokenRepository_findByTokenValue_Test(){
		// given
		PersonalAccessToken token = this.getToken();
		UserInfo userInfo = this.getUserInfo();
		
		// when
		this.saveUser(token.getUserInfo());
		this.saveToken(token);
		PersonalAccessToken result = this.personalAccessTokenRepository.findByTokenValue(token.getTokenValue()).get();
		
		// then
		assertEquals(result.getTokenValue(), token.getTokenValue());
		assertEquals(result.getUserInfo().getId(), userInfo.getId());
	}
	
	@Test
	public void PersonalAccessTokenRepository_deleteUnusedToken_Test(){
		// given
		PersonalAccessToken token = this.getToken();
		
		// when
		this.saveUser(token.getUserInfo());
		this.saveToken(token);
		this.personalAccessTokenRepository.deleteUnusedToken(LocalDate.now().plusDays(1));
		
		// then
		assertThrows(NoSuchElementException.class, ()->this.personalAccessTokenRepository.findByTokenValue(token.getTokenValue()).get());
	}
	
	private void saveUser(UserInfo user){
		this.userInfoRepository.saveAndFlush(user);
	}
	
	private void saveToken(PersonalAccessToken token){
		this.personalAccessTokenRepository.saveAndFlush(token);
	}
	
	private PersonalAccessToken getToken(){
		PersonalAccessToken personalAccessToken = new PersonalAccessToken();
		personalAccessToken.setUserInfo(getUserInfo());
		return personalAccessToken;
	}
	
	private UserInfo getUserInfo(){
		UserInfo userInfo = new UserInfo();
		userInfo.setId(1L);
		userInfo.setName("test");
		userInfo.setProfileUrl("hello.world");
		return userInfo;
	}
	
}