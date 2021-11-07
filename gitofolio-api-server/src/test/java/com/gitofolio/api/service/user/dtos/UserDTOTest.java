package com.gitofolio.api.service.user.dtos;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions.*;

import com.gitofolio.api.domain.user.*;
import com.gitofolio.api.repository.user.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserDTOTest{
	
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
	public void UserDTO_매핑테스트(){
		// when
		UserInfo user1 = userInfoRepository.findByName("user1");
		UserDTO user1DTO = new UserDTO
			.Builder()
			.userInfo(user1)
			.build();
		
		// then
		assertEquals(user1DTO.getName(), "user1");
		assertEquals(user1DTO.getProfileUrl(), "url1");
	}
		
	@Test
	@Transactional
	public void UserDTO_종합_매핑테스트(){
		// when
		UserInfo user1 = userInfoRepository.findByName("user1");
		UserStat user1Stat = userStatRepository.findByName("user1");
		UserStatistics user1Statistics = userStatisticsRepository.findByName("user1");
		List<PortfolioCard> portfolioCards = portfolioCardRepository.findByName("user1");
		
		UserStatDTO user1StatDTO = new UserStatDTO
			.Builder()
			.userStat(user1Stat)
			.build();
		
		UserStatisticsDTO user1StatisticsDTO = new UserStatisticsDTO
			.Builder()
			.userStatistics(user1Statistics)
			.build();
		
		PortfolioCardDTO portfolioCard1DTO = new PortfolioCardDTO
			.Builder()
			.portfolioCard(portfolioCards.get(0))
			.build();
		
		PortfolioCardDTO portfolioCard2DTO = new PortfolioCardDTO
			.Builder()
			.portfolioCard(portfolioCards.get(1))
			.build();
		
		UserDTO user1DTO = new UserDTO
			.Builder()
			.userInfo(user1)
			.portfolioCardDTO(portfolioCard1DTO)
			.portfolioCardDTO(portfolioCard2DTO)
			.userStatisticsDTO(user1StatisticsDTO)
			.userStatDTO(user1StatDTO)
			.build();
		
		// then
		assertEquals(user1DTO.getName(), "user1");
		assertEquals(user1DTO.getProfileUrl(), "url1");
		assertEquals(user1DTO.getUserStatDTO().getTotalVisitors(), 123);
		assertEquals(user1DTO.getUserStatDTO().getTotalStars(), 123);
		assertEquals(user1DTO.getUserStatisticsDTO().getRefferingSiteDTOs().get(0).getRefferingSiteName(), "https://gitofolio.com");
		assertEquals(user1DTO.getPortfolioCardDTOs().get(0).getPortfolioUrl(), "portfolioUrl1");
		assertEquals(user1DTO.getPortfolioCardDTOs().get(1).getPortfolioUrl(), "portfolioUrl2");
	}
}