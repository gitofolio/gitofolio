package com.gitofolio.api.service.user;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import com.gitofolio.api.domain.user.*;
import com.gitofolio.api.repository.user.*;
import com.gitofolio.api.service.user.dtos.*;
import com.gitofolio.api.service.user.factory.*;

@SpringBootTest
public class UserFactoryTest{
	
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
	public void UserInfoFactory_테스트(){
		// when
		UserDTO user1DTO = userInfoFactory.getUser("user1");
		
		// then
		assertEquals(user1DTO.getName(), "user1");
		assertEquals(user1DTO.getProfileUrl(), "url1");
		
		assertNull(user1DTO.getPortfolioCardDTOs());
		assertNull(user1DTO.getUserStatDTO());
		assertNull(user1DTO.getUserStatisticsDTO());
	}
	
	@Test
	public void UserStatFactory_테스트(){
		// when
		UserDTO user1DTO = userStatFactory.getUser("user1");
		
		// then
		assertEquals(user1DTO.getName(), "user1");
		assertEquals(user1DTO.getProfileUrl(), "url1");
		
		assertNull(user1DTO.getPortfolioCardDTOs());
		
		assertEquals(user1DTO.getUserStatDTO().getTotalVisitors(), 123);
		assertEquals(user1DTO.getUserStatDTO().getTotalStars(), 123);
		
		assertNull(user1DTO.getUserStatisticsDTO());
	}
	
	@Test
	@Transactional
	public void UserStatisticsFactory_테스트(){
		// when
		UserDTO user1DTO = userStatisticsFactory.getUser("user1");
		
		// then
		assertEquals(user1DTO.getName(), "user1");
		assertEquals(user1DTO.getProfileUrl(), "url1");
		
		assertNull(user1DTO.getPortfolioCardDTOs());
		assertNull(user1DTO.getUserStatDTO());
		
		assertEquals(user1DTO.getUserStatisticsDTO().getVisitorStatisticsDTOs().get(0).getVisitorCount(), 1);
		assertEquals(user1DTO.getUserStatisticsDTO().getRefferingSiteDTOs().get(0).getRefferingSiteName(), "https://gitofolio.com");
	}
	
	@Test
	public void PortfolioCardFactory_테스트(){
		// when
		UserDTO user1DTO = portfolioCardFactory.getUser("user1");
		
		// then
		assertEquals(user1DTO.getName(), "user1");
		assertEquals(user1DTO.getProfileUrl(), "url1");
		
		assertEquals(user1DTO.getPortfolioCardDTOs().size(), 2);
		assertEquals(user1DTO.getPortfolioCardDTOs().get(0).getPortfolioUrl(), "portfolioUrl1");
		assertEquals(user1DTO.getPortfolioCardDTOs().get(1).getPortfolioUrl(), "portfolioUrl2");
		
		assertNull(user1DTO.getUserStatDTO());
		assertNull(user1DTO.getUserStatisticsDTO());
	}
	
	@Test
	public void UserInfoFactory_save_테스트(){
		// given
		UserDTO userDTO = this.userInfoFactory.getUser("user1");
		
		// when
		this.delete();
		this.userInfoFactory.saveUser(userDTO);
		
		// then
		UserDTO user1DTO = this.userInfoFactory.getUser("user1");
		
		assertEquals(user1DTO.getName(), "user1");
		assertEquals(user1DTO.getProfileUrl(), "url1");
		
		assertNull(user1DTO.getPortfolioCardDTOs());
		assertNull(user1DTO.getUserStatDTO());
		assertNull(user1DTO.getUserStatisticsDTO());
	}
	
	@Test
	public void UserStatFactory_save_테스트(){
		// given
		UserDTO userDTO = this.userStatFactory.getUser("user1");
		
		// when
		this.delete();
		this.userStatFactory.saveUser(userDTO);
		
		// then
		UserDTO user1DTO = this.userStatFactory.getUser("user1");
		
		assertEquals(user1DTO.getName(), "user1");
		assertEquals(user1DTO.getProfileUrl(), "url1");
		
		assertNull(user1DTO.getPortfolioCardDTOs());
		
		assertEquals(user1DTO.getUserStatDTO().getTotalVisitors(), 123);
		assertEquals(user1DTO.getUserStatDTO().getTotalStars(), 123);
		
		assertNull(user1DTO.getUserStatisticsDTO());
	}
	
	@Test
	@Transactional
	public void UserStatisticsFactory_save_테스트(){
		// given
		UserDTO userDTO = this.userStatisticsFactory.getUser("user1");
		
		// when
		this.delete();
		this.userStatisticsFactory.saveUser(userDTO);
		
		// then
		UserDTO user1DTO = this.userStatisticsFactory.getUser("user1");
		
		assertEquals(user1DTO.getName(), "user1");
		assertEquals(user1DTO.getProfileUrl(), "url1");
		
		assertNull(user1DTO.getPortfolioCardDTOs());
		assertNull(user1DTO.getUserStatDTO());
		
		assertEquals(user1DTO.getUserStatisticsDTO().getVisitorStatisticsDTOs().get(0).getVisitorCount(), 1);
		assertEquals(user1DTO.getUserStatisticsDTO().getRefferingSiteDTOs().get(0).getRefferingSiteName(), "https://gitofolio.com");
	}
	
	@Test
	public void UserPortfolioCard_save_테스트(){
		// given
		UserDTO userDTO = this.portfolioCardFactory.getUser("user1");
		
		// when
		this.delete();
		this.portfolioCardFactory.saveUser(userDTO);
		
		// then
		UserDTO user1DTO = this.portfolioCardFactory.getUser("user1");
		
		assertEquals(user1DTO.getName(), "user1");
		assertEquals(user1DTO.getProfileUrl(), "url1");
		
		assertEquals(user1DTO.getPortfolioCardDTOs().size(), 2);
		assertEquals(user1DTO.getPortfolioCardDTOs().get(0).getPortfolioUrl(), "portfolioUrl1");
		assertEquals(user1DTO.getPortfolioCardDTOs().get(1).getPortfolioUrl(), "portfolioUrl2");
		
		assertNull(user1DTO.getUserStatDTO());
		assertNull(user1DTO.getUserStatisticsDTO());
	}
	
	// @Test
	public void UserInfoFactory_중복_save_테스트(){
		// given
		UserDTO userDTO = this.userInfoFactory.getUser("user1");
		
		// when
		this.delete();
		this.userInfoFactory.saveUser(userDTO);
		this.userInfoFactory.saveUser(userDTO);
		this.userInfoFactory.saveUser(userDTO);
		this.userInfoFactory.saveUser(userDTO);
		this.userInfoFactory.saveUser(userDTO);
		this.userInfoFactory.saveUser(userDTO);
		this.userInfoFactory.saveUser(userDTO);
		this.userInfoFactory.saveUser(userDTO);
		this.userInfoFactory.saveUser(userDTO);
		this.userInfoFactory.saveUser(userDTO);
		
		// then
		UserDTO user1DTO = this.userInfoFactory.getUser("user1");
		
		assertEquals(user1DTO.getName(), "user1");
		assertEquals(user1DTO.getProfileUrl(), "url1");
		
		assertNull(user1DTO.getPortfolioCardDTOs());
		assertNull(user1DTO.getUserStatDTO());
		assertNull(user1DTO.getUserStatisticsDTO());
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