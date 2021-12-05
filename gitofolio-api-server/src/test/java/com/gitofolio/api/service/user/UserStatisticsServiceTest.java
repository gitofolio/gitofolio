package com.gitofolio.api.service.user;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import com.gitofolio.api.domain.user.*;
import com.gitofolio.api.service.user.*;
import com.gitofolio.api.service.user.exception.*;

import java.time.LocalDate;

@SpringBootTest
public class UserStatisticsServiceTest{
	
	@Autowired
	private UserStatisticsService userStatisticsService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	String name = "testName";
	
	@Test
	@Transactional
	public void UserStatisticsService_Get_and_Save_Test(){
		// given
		UserInfo userInfo = new UserInfo();
		userInfo.setName(this.name);
		userInfo.setProfileUrl("url.helloworld.com");
		
		this.userInfoService.save(userInfo);
		
		// when
		this.userStatisticsService.increaseVisitorStatistics(this.name);
		
		// then
		UserStatistics retStat = this.userStatisticsService.get(this.name);
		UserInfo retInfo = retStat.getUserInfo();
		
		assertEquals(retInfo.getName(), this.name);
		assertEquals(retStat.getVisitorStatistics().size(), 1);
		assertEquals(retStat.getVisitorStatistics().get(0).getVisitorCount(), 1);
		assertEquals(retStat.getVisitorStatistics().get(0).getVisitDate().toString(), LocalDate.now().toString());
	}
	
	@Test
	@Transactional
	public void UserStatisticsService_Get_Fail_Test(){
		// given
		String userName = this.name + "1"; 
		
		// then
		assertThrows(NonExistUserException.class, ()->this.userStatisticsService.get(userName));
	}
	
	@Test
	@Transactional
	public void UserStatisticsService_Logic_Fail_Test(){
		// given
		String userName = this.name + "1"; 
		
		// then
		assertThrows(NonExistUserException.class, ()->this.userStatisticsService.increaseVisitorStatistics(this.name));
		assertThrows(NonExistUserException.class, ()->this.userStatisticsService.setRefferingSite(this.name, "REFER.helloworld.com"));
	}
	
	@AfterEach
	@Transactional
	public void pre_UserStatisticsService_delete_Test(){
		// given
		String userName = this.name;
		
		// when 
		try{
			this.userStatisticsService.delete(userName);
			this.userInfoService.delete(userName);
		}catch(NonExistUserException NEU){}
		
		// then
		assertThrows(NonExistUserException.class, ()->this.userInfoService.get(userName));
	}
	
	@BeforeEach
	@Transactional
	public void post_UserStatisticsService_delete_Test(){
		// given
		String userName = this.name;
		
		// when 
		try{
			this.userStatisticsService.delete(userName);
			this.userInfoService.delete(userName);
		}catch(NonExistUserException NEU){}
		
		// then
		assertThrows(NonExistUserException.class, ()->this.userInfoService.get(userName));
	}
}