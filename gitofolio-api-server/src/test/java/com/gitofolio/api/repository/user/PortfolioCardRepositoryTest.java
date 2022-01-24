package com.gitofolio.api.repository.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import com.gitofolio.api.domain.user.*;
import com.gitofolio.api.repository.user.*;

import java.util.Optional;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.ArrayList;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PortfolioCardRepositoryTest{
	
	@Autowired
	private PortfolioCardRepository portfolioCardRepository;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Test
	public void PortfolioCardRepository_findByName_Test(){
		// given
		UserInfo userInfo = this.getUserInfo();
		
		List<PortfolioCard> cards = new ArrayList<PortfolioCard>();
		cards.add(this.getPortfolioCard(1));
		cards.add(this.getPortfolioCard(2));
		cards.add(this.getPortfolioCard(3));
		
		// when
		this.saveUserInfo(userInfo);
		this.savePortfolioCards(cards);
		
		List<PortfolioCard> result = this.portfolioCardRepository.findByName(userInfo.getName());
		
		// then
		assertEquals(result.size(), 3);
		assertEquals(result.get(0).getPortfolioCardArticle(), cards.get(0).getPortfolioCardArticle());
	}
	
	@Test
	public void PortfolioCardRepository_save_Long_Article_Test(){
		// given
		UserInfo userInfo = this.getUserInfo();
		
		List<PortfolioCard> cards = new ArrayList<PortfolioCard>();
		cards.add(this.getPortfolioCard(1));
		cards.get(0).setPortfolioCardArticle(this.makeLongText(2000));
		
		String expectedLongText = this.makeLongText(1000);
		
		// when
		this.saveUserInfo(userInfo);
		this.savePortfolioCards(cards);
		
		List<PortfolioCard> result = this.portfolioCardRepository.findByName(userInfo.getName());
		
		// then
		assertEquals(result.get(0).getPortfolioCardArticle().length(), expectedLongText.length());
	}
	
	@Test
	@BeforeEach
	public void beforeCleanupDB(){
		// given
		UserInfo userInfo = this.getUserInfo();
		
		// when
		this.portfolioCardRepository.deleteByName(userInfo.getName());
		this.userInfoRepository.deleteByName(userInfo.getName());
		
		// then
		assertThrows(NoSuchElementException.class, ()->this.userInfoRepository.findByName(userInfo.getName()).get());
	}
	
	@Test
	@AfterEach
	public void afterCleanupDB(){
		// given
		UserInfo userInfo = this.getUserInfo();
		
		// when
		this.portfolioCardRepository.deleteByName(userInfo.getName());
		this.userInfoRepository.deleteByName(userInfo.getName());
		
		// then
		assertThrows(NoSuchElementException.class, ()->this.userInfoRepository.findByName(userInfo.getName()).get());
	}
	
	private void savePortfolioCards(List<PortfolioCard> cards){
		for(PortfolioCard card : cards) this.portfolioCardRepository.save(card);
	}
	
	private void saveUserInfo(UserInfo userInfo){
		this.userInfoRepository.save(userInfo);
	}
	
	private PortfolioCard getPortfolioCard(int cardCnt){
		PortfolioCard portfolioCard = new PortfolioCard();
		portfolioCard.setId(Long.valueOf(cardCnt));
		portfolioCard.setPortfolioCardArticle("article"+cardCnt);
		portfolioCard.setPortfolioCardWatched(cardCnt);
		portfolioCard.setPortfolioUrl("portfolioUrl"+cardCnt);
		
		portfolioCard.setUserInfo(this.getUserInfo());
		return portfolioCard;
	}
	
	private UserInfo getUserInfo(){
		UserInfo userInfo = new UserInfo();
		userInfo.setId(789L);
		userInfo.setName("name");
		userInfo.setProfileUrl("url.helloworld.com");
		return userInfo;
	}
	
	private String makeLongText(int length){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < length; i++) sb.append("a");
		return sb.toString();
	}
	
}