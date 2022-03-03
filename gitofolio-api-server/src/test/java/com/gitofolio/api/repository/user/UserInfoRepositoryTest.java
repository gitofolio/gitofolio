package com.gitofolio.api.repository.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import com.gitofolio.api.domain.user.*;

import java.util.Optional;
import java.util.NoSuchElementException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserInfoRepositoryTest{
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Test
	public void UserInfoRepository_findByName_test(){
		// given
		UserInfo userInfo = this.getUserInfo();
		
		// when
		saveUserInfo(userInfo);
		UserInfo result = this.userInfoRepository.findByName(userInfo.getName()).orElseGet(()->new UserInfo());
		
		// then
		assertEquals(result.getName(), userInfo.getName());
	}
	
	@Test
	@BeforeEach
	public void beforeCleanupDB(){
		// given
		UserInfo userInfo = this.getUserInfo();
		
		// when
		this.userInfoRepository.deleteByName(userInfo.getName());
		
		// then
		assertThrows(NoSuchElementException.class, ()->this.userInfoRepository.findByName(userInfo.getName()).get());
	}
	
	@Test
	@AfterEach
	public void afterCleanupDB(){
		// given
		UserInfo userInfo = this.getUserInfo();
		
		// when
		this.userInfoRepository.deleteByName(userInfo.getName());
		
		// then
		assertThrows(NoSuchElementException.class, ()->this.userInfoRepository.findByName(userInfo.getName()).get());
	}
	
	private void saveUserInfo(UserInfo userInfo){
		this.userInfoRepository.save(userInfo);
	}
	
	private UserInfo getUserInfo(){
		UserInfo userInfo = new UserInfo();
		userInfo.setName("name");
		userInfo.setProfileUrl("url.helloworld.com");
		return userInfo;
	}
	
}