package com.gitofolio.api.repository.user;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import com.gitofolio.api.domain.user.*;

import java.util.Optional;
import java.util.NoSuchElementException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserStatisticsRepositoryTest{
	
	@Autowired
	private UserStatisticsRepository userStatisticsRepository;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Test
	public void UserStatistics_findByName_Test(){
		// given
		UserInfo userInfo = this.getUserInfo();
		UserStatistics userStatistics = this.getUserStatistics();
		userStatistics.setUserInfo(userInfo);
		
		// when
		this.saveUserInfo(userInfo);
		this.saveUserStatistics(userStatistics);
		
		UserStatistics result = this.userStatisticsRepository.findByName(userInfo.getName()).orElseGet(()->new UserStatistics());
		
		// then
		assertEquals(result.getUserInfo().getName(), userStatistics.getUserInfo().getName());
	}
	
	@Test
	@BeforeEach
	public void beforeCleanupDB(){
		// given
		UserInfo userInfo = this.getUserInfo();
		
		// when
		clearUserStatistics(userInfo.getName());
		this.userStatisticsRepository.deleteByName(userInfo.getName());
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
		clearUserStatistics(userInfo.getName());
		this.userStatisticsRepository.deleteByName(userInfo.getName());
		this.userInfoRepository.deleteByName(userInfo.getName());
		
		// then
		assertThrows(NoSuchElementException.class, ()->this.userInfoRepository.findByName(userInfo.getName()).get());
	}
	
	private void clearUserStatistics(String name){
		UserStatistics userStatistics = this.userStatisticsRepository.findByName(name).orElseGet(()->new UserStatistics());
		userStatistics.getVisitorStatistics().clear();
		userStatistics.getRefferingSites().clear();
		return;
	}
	
	private void saveUserStatistics(UserStatistics userStatistics){
		this.userStatisticsRepository.save(userStatistics);
	}
	
	private void saveUserInfo(UserInfo userInfo){
		this.userInfoRepository.saveAndFlush(userInfo);
	}
	
	private UserStatistics getUserStatistics(){
		UserStatistics userStatistics = new UserStatistics();
		userStatistics.setRefferingSite("reffering_site_1");
		userStatistics.addVisitorStatistics();
		return userStatistics;
	}
	
	private UserInfo getUserInfo(){
		UserInfo userInfo = new UserInfo();
		userInfo.setName("name");
		userInfo.setProfileUrl("url.helloworld.com");
		return userInfo;
	}
	
}