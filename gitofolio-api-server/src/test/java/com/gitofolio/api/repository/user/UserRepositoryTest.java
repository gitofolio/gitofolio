package com.gitofolio.api.repository.user;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions.*;

import com.gitofolio.api.domain.user.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserRepositoryTest{
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Autowired
	private UserStatRepository userStatRepository;
	
	@Autowired
	private UserStatisticsRepository userStatisticsRepository;
	
	@Autowired
	private PortfolioCardRepository portfolioCardRepository;
	
	@BeforeEach
	public void init(){
		userInfoRepository.deleteAll();
		userStatRepository.deleteAll();
		userStatisticsRepository.deleteAll();
		portfolioCardRepository.deleteAll();
	}
	                                                                                                   
	@Test
	@Transactional
	public void userInfo_저장_조회_테스트() throws Exception{
		// given
		UserInfo user1 = new UserInfo();
		user1.setName("user1");
		user1.setProfileUrl("url1");
		
		UserInfo user2 = new UserInfo();
		user2.setName("user2");
		user2.setProfileUrl("url2");
		
		// when
		userInfoRepository.save(user1);
		userInfoRepository.save(user2);
		
		// then
		user1 = userInfoRepository.findByName("user1");
		user2 = userInfoRepository.findByName("user2");
		
		assertEquals(user1.getName(), "user1");
		assertEquals(user2.getName(), "user2");
	}
	
	@Test
	@Transactional
	public void PortfolioCard_저장_조회_테스트() throws Exception{
		// given
		UserInfo user1 = new UserInfo();
		user1.setName("user1");
		user1.setProfileUrl("url1");
		
		PortfolioCard portfolioCard1 = new PortfolioCard();
		portfolioCard1.setPortfolioCardArticle("Lorem ipsum");
		portfolioCard1.setPortfolioCardStars(123);
		portfolioCard1.setPortfolioUrl("portfolioUrl1");
		
		user1.setPortfolioCards(portfolioCard1);
		
		PortfolioCard portfolioCard2 = new PortfolioCard();
		portfolioCard2.setPortfolioCardArticle("Lorem ipsum");
		portfolioCard2.setPortfolioCardStars(456);
		portfolioCard2.setPortfolioUrl("portfolioUrl2");
		
		user1.setPortfolioCards(portfolioCard2);
		
		// when
		userInfoRepository.save(user1);
		
		// then
		List<PortfolioCard> cards = portfolioCardRepository.findAll();
		
		assertEquals(cards.size(), 2);
		
		assertEquals(cards.get(0).getPortfolioUrl(), "portfolioUrl1");
		assertTrue(cards.get(0).getUserInfo().getPortfolioCards().contains(cards.get(0)));
		
		assertEquals(cards.get(1).getPortfolioUrl(), "portfolioUrl2");
		assertTrue(cards.get(1).getUserInfo().getPortfolioCards().contains(cards.get(1)));
	}
	
	@Test
	@Transactional
	public void UserStat_저장_조회_테스트(){
		// given
		UserInfo user1 = new UserInfo();
		user1.setName("user1");
		user1.setProfileUrl("url1");
		
		UserStat userStat = new UserStat();
		userStat.setTotalVisitors(123);
		userStat.setTotalStars(123);
		
		user1.setUserStat(userStat);
		
		// when
		userInfoRepository.save(user1);
		
		// then
		user1 = userInfoRepository.findByName("user1");
		
		assertEquals(user1.getUserStat(), userStat);
		assertEquals(user1.getUserStat().getTotalVisitors(), 123);
		assertEquals(user1.getUserStat().getUserInfo(), user1);
	}
	
	@Test
	@Transactional
	public void UserStatistics_저장_조회_테스트(){
		// given
		UserInfo user1 = new UserInfo();
		user1.setName("user1");
		user1.setProfileUrl("url1");
		
		UserStatistics user1Statistics = new UserStatistics();
		user1Statistics.setRefferingSite("https://gitofolio.com");
		user1Statistics.addVisitorStatistics();
		
		user1.setUserStatistics(user1Statistics);
		
		// when
		userInfoRepository.save(user1);
		
		// then
		user1 = userInfoRepository.findByName("user1");
		user1Statistics = user1.getUserStatistics();
		
		assertTrue(user1Statistics.getUserInfo() == user1);
		assertEquals(user1Statistics
					 .getRefferingSites()
					 .get(0)
					 .getRefferingSiteName(), "https://gitofolio.com");
		assertEquals(user1Statistics
					.getVisitorStatistics()
					.get(0)
					.getVisitorCount(), 1);
		
	}
	
}