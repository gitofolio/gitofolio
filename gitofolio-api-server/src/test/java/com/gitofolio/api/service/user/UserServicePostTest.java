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
public class UserServicePostTest{
	
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
	
	@AfterEach
	@Transactional
	public void delete(){
		userStatRepository.deleteAll();
		userStatisticsRepository.deleteAll();
		portfolioCardRepository.deleteAll();
		userInfoRepository.deleteAll();
	}
	
	@Test
	public void UserInfoService_resolveMap_테스트(){
		// given
		UserDTO userDTO = new UserDTO.Builder()
			.name("user1")
			.profileUrl("url1")
			.build();
		
		// when
		this.userInfoService.resolveMap(userDTO);
		
		// then
		UserDTO user1DTO = this.userInfoService.doMap("user1");
		
		assertEquals(user1DTO.getName(), "user1");
		assertEquals(user1DTO.getProfileUrl(), "url1");
		
		assertNull(user1DTO.getPortfolioCardDTOs());
		assertNull(user1DTO.getUserStatDTO());
		assertNull(user1DTO.getUserStatisticsDTO());
	}
	
	@Test
	public void UserStatService_resolveMap_테스트(){
		//given
		UserStatDTO userStatDTO = new UserStatDTO.Builder()
			.totalVisitors(123)
			.totalStars(123)
			.build();
		
		UserDTO userDTO = new UserDTO.Builder()
			.name("user1")
			.profileUrl("url1")
			.userStatDTO(userStatDTO)
			.build();
		
		// when
		this.userStatService.resolveMap(userDTO);
		
		// then
		UserDTO user1DTO = this.userStatService.doMap("user1");
		
		assertEquals(user1DTO.getName(), "user1");
		assertEquals(user1DTO.getProfileUrl(), "url1");
		
		assertNull(user1DTO.getPortfolioCardDTOs());
		
		assertEquals(user1DTO.getUserStatDTO().getTotalVisitors(), 123);
		assertEquals(user1DTO.getUserStatDTO().getTotalStars(), 123);
		
		assertNull(user1DTO.getUserStatisticsDTO());
	}
	
	@Test
	@Transactional
	public void UserStatisticsService_resolveMap_테스트(){
		// given
		UserStatistics userStatistics = new UserStatistics();
		userStatistics.setRefferingSite("https://gitofolio.com");
		userStatistics.addVisitorStatistics();
		
		UserStatisticsDTO userStatisticsDTO = new UserStatisticsDTO.Builder()
			.userStatistics(userStatistics)
			.build();
		
		UserDTO userDTO = new UserDTO.Builder()
			.name("user1")
			.profileUrl("url1")
			.userStatisticsDTO(userStatisticsDTO)
			.build();
		
		// when
		this.userStatisticsService.resolveMap(userDTO);
		
		// then
		UserDTO user1DTO = this.userStatisticsService.doMap("user1");
		
		assertEquals(user1DTO.getName(), "user1");
		assertEquals(user1DTO.getProfileUrl(), "url1");
		
		assertNull(user1DTO.getPortfolioCardDTOs());
		assertNull(user1DTO.getUserStatDTO());
			
		assertEquals(user1DTO.getUserStatisticsDTO().getVisitorStatisticsDTOs().get(0).getVisitorCount(), 1);
		assertEquals(user1DTO.getUserStatisticsDTO().getRefferingSiteDTOs().get(0).getRefferingSiteName(), "https://gitofolio.com");
	}
	
	@Test
	public void UserRepositoryService_resolveMap_테스트(){
		// given
		PortfolioCard portfolioCard1 = new PortfolioCard();
		portfolioCard1.setPortfolioCardArticle("Lorem ipsum");
		portfolioCard1.setPortfolioCardStars(123);
		portfolioCard1.setPortfolioUrl("portfolioUrl1");
		
		PortfolioCard portfolioCard2 = new PortfolioCard();
		portfolioCard2.setPortfolioCardArticle("Lorem ipsum");
		portfolioCard2.setPortfolioCardStars(123);
		portfolioCard2.setPortfolioUrl("portfolioUrl2");
		
		PortfolioCardDTO portfolioCardDTO1 = new PortfolioCardDTO.Builder()
			.portfolioCard(portfolioCard1)
			.build();
		
		PortfolioCardDTO portfolioCardDTO2 = new PortfolioCardDTO.Builder()
			.portfolioCard(portfolioCard2)
			.build();
		
		UserDTO userDTO = new UserDTO.Builder()
			.name("user1")
			.profileUrl("url1")
			.portfolioCardDTO(portfolioCardDTO1)
			.portfolioCardDTO(portfolioCardDTO2)
			.build();
		
		// when
		this.portfolioCardService.resolveMap(userDTO);
		
		// then
		UserDTO user1DTO = this.portfolioCardService.doMap("user1");
		
		assertEquals(user1DTO.getName(), "user1");
		assertEquals(user1DTO.getProfileUrl(), "url1");
		
		assertEquals(user1DTO.getPortfolioCardDTOs().size(), 2);
		assertEquals(user1DTO.getPortfolioCardDTOs().get(0).getPortfolioUrl(), "portfolioUrl1");
		assertEquals(user1DTO.getPortfolioCardDTOs().get(1).getPortfolioUrl(), "portfolioUrl2");
		
		assertNull(user1DTO.getUserStatDTO());
		assertNull(user1DTO.getUserStatisticsDTO());
	}
	
}