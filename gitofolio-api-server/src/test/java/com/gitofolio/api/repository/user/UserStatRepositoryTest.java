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
		UserInfo userInfo = this.getUserInfo(1L, "name");
		UserStat userStat = this.getUserStat(userInfo);
		
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
		UserInfo userInfo = this.getUserInfo(1L, "name");
		UserStat userStat = this.getUserStat(userInfo);
		
		// when
		this.saveUserInfo(userInfo);
		this.saveUserStat(userStat);
		
		UserStat result = this.userStatRepository.findById(userInfo.getId()).orElseGet(()->new UserStat());
		
		// then
		assertEquals(result.getUserInfo().getName(), userStat.getUserInfo().getName());
	}
	
	@Test
	public void ColumnA_was_DELETE_BUT_Is_ColumnB_was_still_survive(){
		// given
		UserInfo columnA = this.getUserInfo(1L, "columnA");
		UserStat aStat = this.getUserStat(columnA);
		
		UserInfo columnB = this.getUserInfo(2L, "columnB");
		UserStat bStat = this.getUserStat(columnB);
		
		// when
		this.saveUserInfo(columnA);
		this.saveUserStat(aStat);
		this.saveUserInfo(columnB);
		this.saveUserStat(bStat);
		
		this.userStatRepository.deleteByName(columnA.getName());
		
		UserStat result = this.userStatRepository.findByName(columnB.getName()).get();
		
		// then
		assertEquals(result.getUserInfo().getName(), columnB.getName());
		assertThrows(NoSuchElementException.class, ()->this.userStatRepository.findByName(columnA.getName()).orElseThrow(()->new NoSuchElementException("")));
	}
	
	@Test
	@BeforeEach
	public void beforeCleanupDB(){
		// when
		this.userStatRepository.deleteAll();
		this.userInfoRepository.deleteAll();
		
		// then
		assertTrue(this.userInfoRepository.findAll().isEmpty());
	}
	
	@Test
	@AfterEach
	public void afterCleanupDB(){
		// when
		this.userStatRepository.deleteAll();
		this.userInfoRepository.deleteAll();
		
		// then
		assertTrue(this.userInfoRepository.findAll().isEmpty());
	}
	
	private void saveUserStat(UserStat userStat){
		this.userStatRepository.save(userStat);
	}
	
	private void saveUserInfo(UserInfo userInfo){
		this.userInfoRepository.saveAndFlush(userInfo);
	}
	
	private UserStat getUserStat(UserInfo userInfo){
		UserStat userStat = new UserStat();
		userStat.setTotalVisitors(0);
		userStat.setUserInfo(userInfo);
		return userStat;
	}
	
	private UserInfo getUserInfo(Long id, String name){
		UserInfo userInfo = new UserInfo();
		userInfo.setId(id);
		userInfo.setName(name);
		userInfo.setProfileUrl("url.helloworld.com");
		return userInfo;
	}
	
}