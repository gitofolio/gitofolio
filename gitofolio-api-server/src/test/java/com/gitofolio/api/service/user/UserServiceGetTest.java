package com.gitofolio.api.service.user;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import com.gitofolio.api.service.user.dtos.*;
import com.gitofolio.api.domain.user.*;
import com.gitofolio.api.repository.user.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class UserServiceGetTest{
	
	@Autowired 
	@Qualifier("userInfoService") 
	private UserMapper userInfoService;
	
	@Autowired 
	@Qualifier("userStatService") 
	private UserMapper userStatService;
	
	@Autowired 
	@Qualifier("userStatisticsService") 
	private UserMapper userStatisticsService;
	
	@Autowired 
	@Qualifier("portfolioCardService") 
	private UserMapper portfolioCardService;
	
	
	@Autowired 
	private UserInfoRepository userInfoRepository;
	
	@Autowired 
	private UserStatRepository userStatRepository;
	
	@Autowired 
	private UserStatisticsRepository userStatisticsRepository;
	
	@Autowired 
	private PortfolioCardRepository portfolioCardRepository;
	
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
	
	@Test
	@Transactional
	public void UserDTO_GET_테스트(){
		// when
		UserDTO user1DTO = userInfoService.doMap("user1");
		
		// then
		assertEquals(user1DTO.getName(), "user1");
		assertEquals(user1DTO.getProfileUrl(), "url1");
		
		assertNull(user1DTO.getPortfolioCards());
		assertNull(user1DTO.getUserStat());
		assertNull(user1DTO.getUserStatistics());
	}
	
	@Test
	@Transactional
	public void UserStatDTO_GET_테스트(){
		// when
		UserDTO user1DTO = userStatService.doMap("user1");
		
		// then
		assertEquals(user1DTO.getName(), "user1");
		assertEquals(user1DTO.getProfileUrl(), "url1");
		
		assertNull(user1DTO.getPortfolioCards());
		
		assertEquals(user1DTO.getUserStat().getTotalVisitors(), 123);
		assertEquals(user1DTO.getUserStat().getTotalStars(), 123);
		
		assertNull(user1DTO.getUserStatistics());
	}
	
	@Test
	@Transactional
	public void UserStatisticsDTO_GET_테스트(){
		// when
		UserDTO user1DTO = userStatisticsService.doMap("user1");
		
		// then
		assertEquals(user1DTO.getName(), "user1");
		assertEquals(user1DTO.getProfileUrl(), "url1");
		
		assertNull(user1DTO.getPortfolioCards());
		assertNull(user1DTO.getUserStat());
			
		assertEquals(user1DTO.getUserStatistics().getVisitorStatistics().get(0).getVisitorCount(), 1);
		assertEquals(user1DTO.getUserStatistics().getRefferingSites().get(0).getRefferingSiteName(), "https://gitofolio.com");
	}
	
	@Test
	public void PortfolioCardDTO_GET_테스트(){
		// when
		UserDTO user1DTO = portfolioCardService.doMap("user1");
		
		// then
		assertEquals(user1DTO.getName(), "user1");
		assertEquals(user1DTO.getProfileUrl(), "url1");
		
		assertEquals(user1DTO.getPortfolioCards().size(), 2);
		assertEquals(user1DTO.getPortfolioCards().get(0).getPortfolioUrl(), "portfolioUrl1");
		assertEquals(user1DTO.getPortfolioCards().get(1).getPortfolioUrl(), "portfolioUrl2");
		
		assertNull(user1DTO.getUserStat());
		assertNull(user1DTO.getUserStatistics());
		
	}
	
}