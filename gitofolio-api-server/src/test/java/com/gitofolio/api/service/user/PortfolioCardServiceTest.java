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

import java.util.List; 
import java.util.ArrayList;

@SpringBootTest
public class PortfolioCardServiceTest{
	
	@Autowired
	private PortfolioCardService portfolioCardService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	String name = "testName";
	
	@Test
	@Transactional
	public void portfolioCardService_Get_and_Save_Test(){
		// given
		UserInfo userInfo = new UserInfo();
		userInfo.setName(this.name);
		userInfo.setProfileUrl("url.helloworld.com");
		
		PortfolioCard portfolioCard1 = new PortfolioCard();
		portfolioCard1.setPortfolioCardArticle("article1");
		portfolioCard1.setPortfolioCardStars(1);
		portfolioCard1.setPortfolioUrl("portfolioUrl1");
		
		portfolioCard1.setUserInfo(userInfo);
		
		PortfolioCard portfolioCard2 = new PortfolioCard();
		portfolioCard2.setPortfolioCardArticle("article2");
		portfolioCard2.setPortfolioCardStars(2);
		portfolioCard2.setPortfolioUrl("portfolioUrl2");
		
		portfolioCard2.setUserInfo(userInfo);
		
		PortfolioCard portfolioCard3 = new PortfolioCard();
		portfolioCard3.setPortfolioCardArticle("article3");
		portfolioCard3.setPortfolioCardStars(3);
		portfolioCard3.setPortfolioUrl("portfolioUrl3");
		
		portfolioCard3.setUserInfo(userInfo);
		
		List<PortfolioCard> cards = new ArrayList<PortfolioCard>();
		cards.add(portfolioCard1);
		cards.add(portfolioCard2);
		cards.add(portfolioCard3);
		
		// when
		this.userInfoService.save(userInfo);
		this.portfolioCardService.save(cards);
		
		// then
		List<PortfolioCard> ret = this.portfolioCardService.get(this.name);
		UserInfo retUser = this.userInfoService.get(this.name);
		assertEquals(retUser.getName(), this.name);
		assertEquals(ret.size(), 3);
		assertEquals(ret.get(0).getPortfolioUrl(), "portfolioUrl1");
	}
	
	@Test
	@Transactional
	public void portfolioCard_NonExistUser_Fail_Test(){
		// given
		String name = this.name;
		
		// then
		assertThrows(NonExistUserException.class, ()->this.portfolioCardService.get(name));
	}
	
	@Test
	@Transactional
	public void portfolioCardService_ExistButNoPortfolioCard_Fail_Test(){
		// given
		UserInfo userInfo = new UserInfo();
		userInfo.setName(this.name);
		userInfo.setProfileUrl("url.helloworld.com");
		
		this.userInfoService.save(userInfo);
		// when
		
		// then
		assertThrows(NonExistUserException.class, ()->this.portfolioCardService.get(this.name));
	}
	
	@Test
	@Transactional
	public void portfolioCardService_Put_Test(){
		// given
		UserInfo userInfo = new UserInfo();
		userInfo.setName(this.name);
		userInfo.setProfileUrl("url.helloworld.com");
		
		PortfolioCard portfolioCard1 = new PortfolioCard();
		portfolioCard1.setPortfolioCardArticle("article1");
		portfolioCard1.setPortfolioCardStars(1);
		portfolioCard1.setPortfolioUrl("portfolioUrl1");
		
		portfolioCard1.setUserInfo(userInfo);
		
		PortfolioCard portfolioCard2 = new PortfolioCard();
		portfolioCard2.setPortfolioCardArticle("article2");
		portfolioCard2.setPortfolioCardStars(2);
		portfolioCard2.setPortfolioUrl("portfolioUrl2");
		
		portfolioCard2.setUserInfo(userInfo);
		
		PortfolioCard portfolioCard3 = new PortfolioCard();
		portfolioCard3.setPortfolioCardArticle("article3");
		portfolioCard3.setPortfolioCardStars(3);
		portfolioCard3.setPortfolioUrl("portfolioUrl3");
		
		portfolioCard3.setUserInfo(userInfo);
		
		List<PortfolioCard> cards = new ArrayList<PortfolioCard>();
		cards.add(portfolioCard1);
		cards.add(portfolioCard2);
		cards.add(portfolioCard3);
		
		PortfolioCard editCard = new PortfolioCard();
		editCard.setPortfolioCardArticle("edit");
		editCard.setPortfolioCardStars(0);
		editCard.setPortfolioUrl("edit");
		
		editCard.setUserInfo(userInfo);
		
		List<PortfolioCard> editCards = new ArrayList<PortfolioCard>();
		editCards.add(editCard);
		
		// when
		this.userInfoService.save(userInfo);
		this.portfolioCardService.save(cards);
		this.portfolioCardService.edit(1, editCards);
			
		// then
		List<PortfolioCard> results = this.portfolioCardService.get(this.name);
		
		assertEquals(results.get(0).getUserInfo().getName(), this.name);
		assertEquals(results.size(), 3);
		assertEquals(results.get(0).getPortfolioUrl(), "edit");
		assertEquals(results.get(0).getPortfolioCardStars(), 1);
		assertEquals(results.get(0).getPortfolioCardArticle(), "edit");
	}
	
	@AfterEach
	@Transactional
	public void pre_UserStatisticsService_delete_Test(){
		// given
		String userName = this.name;
		
		// when 
		try{
			this.portfolioCardService.delete(userName);
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
			this.portfolioCardService.delete(userName);
			this.userInfoService.delete(userName);
		}catch(NonExistUserException NEU){}
		
		// then
		assertThrows(NonExistUserException.class, ()->this.userInfoService.get(userName));
	}
}