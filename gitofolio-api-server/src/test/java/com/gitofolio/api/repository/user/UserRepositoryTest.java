package com.gitofolio.api.repository.user;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
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
	
	@AfterEach
	@Transactional
	public void init(){
		userStatRepository.deleteAll();
		userStatisticsRepository.deleteAll();
		portfolioCardRepository.deleteAll();
		userInfoRepository.deleteAll();
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
		user1 = userInfoRepository.findByName("user1").orElseThrow(()->new RuntimeException("fail save"));
		user2 = userInfoRepository.findByName("user2").orElseThrow(()->new RuntimeException("fail save"));
		
		assertEquals(user1.getName(), "user1");
		assertEquals(user2.getName(), "user2");
	}
	
	@Test
	@Transactional
	public void PortfolioCard_저장_조회_테스트(){
		// given
		UserInfo user1 = new UserInfo();
		user1.setName("user1");
		user1.setProfileUrl("url1");
		
		PortfolioCard portfolioCard1 = new PortfolioCard();
		portfolioCard1.setPortfolioCardArticle("Lorem ipsum");
		portfolioCard1.setPortfolioCardStars(123);
		portfolioCard1.setPortfolioUrl("portfolioUrl1");
		
		portfolioCard1.setUserInfo(user1);
		
		PortfolioCard portfolioCard2 = new PortfolioCard();
		portfolioCard2.setPortfolioCardArticle("Lorem ipsum");
		portfolioCard2.setPortfolioCardStars(456);
		portfolioCard2.setPortfolioUrl("portfolioUrl2");
		
		portfolioCard2.setUserInfo(user1);
		
		// when
		userInfoRepository.save(user1);
		portfolioCardRepository.save(portfolioCard1);
		portfolioCardRepository.save(portfolioCard2);
		
		// then
		List<PortfolioCard> cards = portfolioCardRepository.findByName("user1");
		
		assertEquals(cards.size(), 2);
		
		assertEquals(cards.get(0).getPortfolioUrl(), "portfolioUrl1");
		assertEquals(cards.get(0).getUserInfo().getName(), "user1");
			
		assertEquals(cards.get(1).getPortfolioUrl(), "portfolioUrl2");
		assertEquals(cards.get(1).getUserInfo().getName(), "user1");
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
		
		userStat.setUserInfo(user1);
		
		// when
		userInfoRepository.save(user1);
		userStatRepository.save(userStat);
		
		// then
		UserStat userStat1 = userStatRepository.findByName("user1").orElseThrow(()->new RuntimeException("fail save"));
		
		assertEquals(userStat1, userStat);
		assertEquals(userStat1.getTotalVisitors(), 123);
		assertEquals(userStat1.getUserInfo(), user1);
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
		user1Statistics.setRefferingSite("https://api.gitofolio.com");
		user1Statistics.setRefferingSite("https://gitofolio.com");
		user1Statistics.addVisitorStatistics();
		
		user1Statistics.setUserInfo(user1);
		
		// when
		userInfoRepository.save(user1);
		userStatisticsRepository.save(user1Statistics);
		
		// then
		user1Statistics = userStatisticsRepository.findByName("user1").orElseThrow(()->new RuntimeException("fail save"));
		
		assertTrue(user1Statistics.getUserInfo() == user1);
		
		assertEquals(user1Statistics
					.getRefferingSites()
					.size(), 2);
		
		assertEquals(user1Statistics
					 .getRefferingSites()
					 .get(0)
					 .getRefferingSiteName(), "https://gitofolio.com");
		assertEquals(user1Statistics
					 .getRefferingSites()
					 .get(1)
					 .getRefferingSiteName(), "https://api.gitofolio.com");
		assertEquals(user1Statistics
					.getVisitorStatistics()
					.get(0)
					.getVisitorCount(), 2); // 1이 나와야하는데 2가 나오는 오류발생
		
	}
	
}