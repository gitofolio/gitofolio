package com.gitofolio.api.interceptor;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.List;

import com.gitofolio.api.domain.user.*;
import com.gitofolio.api.service.user.dtos.*;
import com.gitofolio.api.service.user.factory.*;
import com.gitofolio.api.service.user.*;
import com.gitofolio.api.service.user.eraser.*;

@SpringBootTest
public class UserInterceptorTest{
	
	@Autowired
	private UserStatService userStatService;
	
	@Autowired
	private UserStatisticsService userStatisticsService;
	
	@Autowired
	@Qualifier("userInfoFactory")
	private UserFactory userInfoFactory;
	
	@Autowired
	@Qualifier("userStatFactory")
	private UserFactory userStatFactory;
	
	@Autowired
	@Qualifier("userStatisticsFactory")
	private UserFactory userStatisticsFactory;
	
	@Autowired
	@Qualifier("userEraser")
	private UserEraser userInfoEraser;
	
	@Transactional
	public void UserStat_자동_업데이트_테스트(){
		// given
		UserDTO setting = new UserDTO.Builder().name("devxb").build();
		this.userInfoFactory.saveUser(setting);
		
		// when
		this.userStatService.increaseTotalVisitors("devxb");
		this.userStatService.increaseTotalVisitors("devxb");
		this.userStatService.increaseTotalVisitors("devxb");
		
		this.userStatService.increaseTotalStars("devxb");
		this.userStatService.increaseTotalStars("devxb");
		
		// then
		UserDTO userDTO = this.userStatFactory.getUser("devxb");
		UserStatDTO userStatDTO = userDTO.getUserStat();
		assertEquals(userDTO.getName(), "devxb");
		assertEquals(userStatDTO.getTotalVisitors(), 3);
		assertEquals(userStatDTO.getTotalStars(), 2);
	}
	
	@Transactional
	public void UserStatistics_자동_업데이트_테스트(){
		// given
		UserDTO setting = new UserDTO.Builder().name("devxb").build();
		this.userInfoFactory.saveUser(setting);
		
		// when
		this.userStatisticsService.increaseVisitorStatistics("devxb");
		this.userStatisticsService.increaseVisitorStatistics("devxb");
		this.userStatisticsService.increaseVisitorStatistics("devxb");
		
		this.userStatisticsService.setRefferingSite("devxb", "https://naver.com");
		this.userStatisticsService.setRefferingSite("devxb", "https://github.com");
		this.userStatisticsService.setRefferingSite("devxb", "https://naver.com"); // 중복 되서 저장 안되야함
		
		// then
		UserDTO userDTO = this.userStatisticsFactory.getUser("devxb");
		UserStatisticsDTO userStatisticsDTO = userDTO.getUserStatistics();
		
		assertEquals(userDTO.getName(), "devxb");
		assertEquals(userStatisticsDTO.getVisitorStatistics().get(0).getVisitorCount(), 3);
		assertEquals(userStatisticsDTO.getVisitorStatistics().get(0).getVisitDate().toString(), LocalDate.now().toString());
		assertEquals(userStatisticsDTO.getRefferingSites().size(), 2);
		assertEquals(userStatisticsDTO.getRefferingSites().get(0).getRefferingSiteName(), "https://naver.com");
		assertEquals(userStatisticsDTO.getRefferingSites().get(1).getRefferingSiteName(), "https://github.com");
	}
	
	@BeforeEach
	public void pre_삭제(){
		this.userInfoEraser.delete("devxb");
	}
	
	@AfterEach
	public void post_삭제(){
		this.userInfoEraser.delete("devxb");
	}
	
}