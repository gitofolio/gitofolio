package com.gitofolio.api.service.user;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.gitofolio.api.domain.user.*;
import com.gitofolio.api.repository.user.*;
import com.gitofolio.api.service.user.dtos.*;
import com.gitofolio.api.service.user.eraser.*;
import com.gitofolio.api.service.user.factory.*;

@SpringBootTest
public class UserEraserTest{
	
	@Autowired
	@Qualifier("userInfoEraser")
	private UserEraser userInfoEraser;
	
	@Autowired
	@Qualifier("userStatEraser")
	private UserEraser userStatEraser;
	
	@Autowired
	@Qualifier("userStatisticsEraser")
	private UserEraser userStatisticsEraser;
	
	@Autowired
	@Qualifier("portfolioCardEraser")
	private UserEraser portfolioCardEraser;
	
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
	@Qualifier("portfolioCardFactory")
	private UserFactory portfolioCardFactory;
	
	@Autowired 
	private UserInfoRepository userInfoRepository;
	
	@Autowired 
	private UserStatRepository userStatRepository;
	
	@Autowired 
	private UserStatisticsRepository userStatisticsRepository;
	
	@Autowired 
	private PortfolioCardRepository portfolioCardRepository;
	
	
	@Test
	public void UserStat_삭제_테스트(){
		// when
		String result = this.userStatEraser.delete("user1");
		
		// then
		assertEquals(result, "user1");
		assertThrows(RuntimeException.class, ()->this.userStatFactory.getUser("user1"));
	}
	
	@Test
	public void UserStatistics_삭제_테스트(){
		// when
		String result = this.userStatisticsEraser.delete("user1");
		
		// then
		assertEquals(result, "user1");
		assertThrows(RuntimeException.class, ()->this.userStatisticsFactory.getUser("user1"));
	}
	
	@Test
	public void PortfolioCard_삭제_테스트(){
		// when
		String result = this.portfolioCardEraser.delete("user1");
		
		// then
		assertEquals(result, "user1");
		assertNull(this.portfolioCardFactory.getUser("user1").getPortfolioCards());
	}
	
	@Test
	public void UserInfo_삭제_테스트(){
		// when
		String result = this.userInfoEraser.delete("user1");
		
		// then
		assertEquals(result, "user1");
		assertThrows(RuntimeException.class, ()->this.userInfoFactory.getUser("user1"));
	}
	
	
	
	@BeforeEach
	@Transactional
	public void init(){
		saveUser();
	}
	
	@AfterEach
	@Transactional
	public void delete(){
		userStatRepository.deleteAll();
		userStatisticsRepository.deleteAll();
		portfolioCardRepository.deleteAll();
		userInfoRepository.deleteAll();
	}
	
	public void saveUser(){
		// given
		UserInfo user1 = new UserInfo();
		user1.setName("user1");
		user1.setProfileUrl("url1");
		
		userInfoRepository.save(user1);
		
		PortfolioCard portfolioCard1 = new PortfolioCard();
		portfolioCard1.setPortfolioCardArticle("Lorem ipsum");
		portfolioCard1.setPortfolioCardStars(123);
		portfolioCard1.setPortfolioUrl("portfolioUrl1");
		
		portfolioCard1.setUserInfo(user1);
		
		PortfolioCard portfolioCard2 = new PortfolioCard();
		portfolioCard2.setPortfolioCardArticle("Lorem ipsum");
		portfolioCard2.setPortfolioCardStars(123);
		portfolioCard2.setPortfolioUrl("portfolioUrl2");
		
		portfolioCard2.setUserInfo(user1);
		
		portfolioCardRepository.save(portfolioCard1);
		portfolioCardRepository.save(portfolioCard2);
		
		UserStat userStat = new UserStat();
		userStat.setTotalVisitors(123);
		userStat.setTotalStars(123);
		
		userStat.setUserInfo(user1);
		
		userStatRepository.save(userStat);
		
		UserStatistics user1Statistics = new UserStatistics();
		user1Statistics.setRefferingSite("https://gitofolio.com");
		user1Statistics.addVisitorStatistics();
		
		user1Statistics.setUserInfo(user1);
		
		userStatisticsRepository.save(user1Statistics);
	}
	
	
}