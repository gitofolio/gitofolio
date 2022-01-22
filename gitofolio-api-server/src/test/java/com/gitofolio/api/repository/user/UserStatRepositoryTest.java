package com.gitofolio.api.repository.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import com.gitofolio.api.domain.user.*;

import java.util.Optional;
import java.util.NoSuchElementException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserStatRepositoryTest{
	
	@Autowired
	private UserStatRepository userStatRepository;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Test
	public void userStatRepository_findByName_Test(){
		// given
		UserInfo userInfo = this.getUserInfo();
		UserStat userStat = this.getUserStat();
		
		// when
		this.saveUserInfo(userInfo);
		this.saveUserStat(userStat);
		
		UserStat result = this.userStatRepository.findByName(userInfo.getName()).orElseGet(()->new UserStat());
		
		// then
		assertEquals(result.getUserInfo().getName(), userStat.getUserInfo().getName());
	}
	
	@Test
	public void userStatRepository_findById_Test(){
		// given
		UserInfo userInfo = this.getUserInfo();
		UserStat userStat = this.getUserStat();
		
		// when
		this.saveUserInfo(userInfo);
		this.saveUserStat(userStat);
		
		UserStat result = this.userStatRepository.findById(userInfo.getId()).orElseGet(()->new UserStat());
		
		// then
		assertEquals(result.getUserInfo().getName(), userStat.getUserInfo().getName());
	}
	
	@Test
	@BeforeEach
	public void beforeCleanupDB(){
		// given
		UserInfo userInfo = this.getUserInfo();
		
		// when
		this.userStatRepository.deleteByName(userInfo.getName());
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
		this.userStatRepository.deleteByName(userInfo.getName());
		this.userInfoRepository.deleteByName(userInfo.getName());
		
		// then
		assertThrows(NoSuchElementException.class, ()->this.userInfoRepository.findByName(userInfo.getName()).get());
	}
	
	private void saveUserStat(UserStat userStat){
		this.userStatRepository.save(userStat);
	}
	
	private void saveUserInfo(UserInfo userInfo){
		this.userInfoRepository.saveAndFlush(userInfo);
	}
	
	private UserStat getUserStat(){
		UserStat userStat = new UserStat();
		userStat.setTotalStars(0);
		userStat.setTotalVisitors(0);
		userStat.setUserInfo(this.getUserInfo());
		return userStat;
	}
	
	private UserInfo getUserInfo(){
		UserInfo userInfo = new UserInfo();
		userInfo.setId(789L);
		userInfo.setName("name");
		userInfo.setProfileUrl("url.helloworld.com");
		return userInfo;
	}
	
}