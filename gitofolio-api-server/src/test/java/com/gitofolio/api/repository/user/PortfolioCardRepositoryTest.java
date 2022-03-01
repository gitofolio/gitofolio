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
		UserInfo userInfo = this.getUserInfo(1L, "name");
		
		List<PortfolioCard> cards = new ArrayList<PortfolioCard>();
		cards.add(this.getPortfolioCard(1, userInfo));
		cards.add(this.getPortfolioCard(2, userInfo));
		cards.add(this.getPortfolioCard(3, userInfo));
		
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
		UserInfo userInfo = this.getUserInfo(1L, "name");
		
		List<PortfolioCard> cards = new ArrayList<PortfolioCard>();
		cards.add(this.getPortfolioCard(1, userInfo));
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
	public void ColumnA_was_DELETE_BUT_Is_ColumnB_was_still_survive(){
		// given
		UserInfo columnA = this.getUserInfo(1L, "columnA");
		List<PortfolioCard> aCards = new ArrayList<PortfolioCard>();
		aCards.add(this.getPortfolioCard(1, columnA));
		aCards.add(this.getPortfolioCard(2, columnA));
		aCards.add(this.getPortfolioCard(3, columnA));
		
		UserInfo columnB = this.getUserInfo(2L, "columnB");
		List<PortfolioCard> bCards = new ArrayList<PortfolioCard>();
		bCards.add(this.getPortfolioCard(4, columnB));
		bCards.add(this.getPortfolioCard(5, columnB));
		bCards.add(this.getPortfolioCard(6, columnB));
		
		// when
		this.saveUserInfo(columnA);
		this.savePortfolioCards(aCards);
		this.saveUserInfo(columnB);
		this.savePortfolioCards(bCards);
		
		this.portfolioCardRepository.deleteByName(columnA.getName());
		
		List<PortfolioCard> resultA = this.portfolioCardRepository.findByName(columnA.getName());
		List<PortfolioCard> resultB = this.portfolioCardRepository.findByName(columnB.getName());
		
		// then
		assertTrue(resultA.isEmpty());
		assertEquals(resultB.size(), bCards.size());
	}
	
	private void deleteByName(String name){
		this.portfolioCardRepository.deleteByName(name);
	}
	
	@Test
	@BeforeEach
	public void beforeCleanupDB(){
		// when
		this.portfolioCardRepository.deleteAll();
		this.userInfoRepository.deleteAll();
		
		// then
		assertTrue(this.userInfoRepository.findAll().isEmpty());
	}
	
	@Test
	@AfterEach
	public void afterCleanupDB(){
		// when
		this.portfolioCardRepository.deleteAll();
		this.userInfoRepository.deleteAll();
		
		// then
		assertTrue(this.userInfoRepository.findAll().isEmpty());
	}
	
	private void savePortfolioCards(List<PortfolioCard> cards){
		for(PortfolioCard card : cards) this.portfolioCardRepository.save(card);
		this.portfolioCardRepository.flush();
	}
	
	private void saveUserInfo(UserInfo userInfo){
		this.userInfoRepository.saveAndFlush(userInfo);
	}
	
	private PortfolioCard getPortfolioCard(int cardCnt, UserInfo userInfo){
		PortfolioCard portfolioCard = new PortfolioCard();
		portfolioCard.setId(Long.valueOf(cardCnt));
		portfolioCard.setPortfolioCardArticle("article"+cardCnt);
		portfolioCard.setPortfolioCardWatched(cardCnt);
		portfolioCard.setPortfolioUrl("portfolioUrl"+cardCnt);
		
		portfolioCard.setUserInfo(userInfo);
		return portfolioCard;
	}
	
	private UserInfo getUserInfo(Long id, String name){
		UserInfo userInfo = new UserInfo();
		userInfo.setId(id);
		userInfo.setName(name);
		userInfo.setProfileUrl("url.helloworld.com");
		return userInfo;
	}
	
	private String makeLongText(int length){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < length; i++) sb.append("a");
		return sb.toString();
	}
	
}