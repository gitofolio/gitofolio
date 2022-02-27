package com.gitofolio.api.service.user;

import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;

import com.gitofolio.api.domain.user.*;
import com.gitofolio.api.service.user.*;
import com.gitofolio.api.service.user.exception.*;
import com.gitofolio.api.repository.user.*;

import java.util.List; 
import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PortfolioCardServiceTest{
	
	@Mock
	private PortfolioCardRepository portfolioCardRepository;
	@Mock
	private UserInfoRepository userInfoRepository;
	
	@InjectMocks
	private PortfolioCardService portfolioCardService;
	
	@Test
	@Transactional
	public void portfolioCardService_Get_Test(){
		// given
		List<PortfolioCard> cards = new ArrayList<PortfolioCard>();
		cards.add(this.getPortfolioCard(1));
		cards.add(this.getPortfolioCard(2));
		cards.add(this.getPortfolioCard(3));
		
		// when
		given(this.portfolioCardRepository.findByName(any(String.class))).willReturn(cards);
		
		List<PortfolioCard> result = this.portfolioCardService.get(cards.get(0).getUserInfo().getName());
		
		// then
		assertEquals(result.get(0).getUserInfo().getName(), cards.get(0).getUserInfo().getName());
		assertEquals(result.size(), 3);
	}
	
	// @Test
	// @Transactional
	// public void portfolioCardService_GET_STRING_AND_LONG_TEST(){
	// 	// given
	// 	List<PortfolioCard> cards = new ArrayList<PortfolioCard>();
	// 	cards.add(this.getPortfolioCard(1));
		
	// 	// when
	// 	given(this.portfolioCardRepository.findByNameAndId(any(String.class), any(Long.class))).willReturn(Optional.of(cards.get(0)));
		
	// 	List<PortfolioCard> result = this.portfolioCardService.get(cards.get(0).getUserInfo().getName(), 1L);
		
	// 	// then
	// 	assertEquals(result.get(0).getUserInfo().getName(), cards.get(0).getUserInfo().getName());
	// 	assertEquals(result.size(), 1);
	// }
	
	@Test
	@Transactional
	public void portfolioCard_NonExistUser_Fail_Test(){
		// given
		UserInfo userInfo = this.getUserInfo();
		
		// when
		given(this.portfolioCardRepository.findByName(any(String.class))).willReturn(new ArrayList<PortfolioCard>(0));
		given(this.userInfoRepository.findByName(any(String.class))).willReturn(Optional.ofNullable(null));
		
		// then
		assertThrows(NonExistUserException.class, ()->this.portfolioCardService.get(userInfo.getName()));
	}
	
	@Test
	@Transactional
	public void portfolioCardService_ExistButNoPortfolioCard_Fail_Test(){
		// given
		UserInfo userInfo = this.getUserInfo();
		
		// when
		given(this.portfolioCardRepository.findByName(any(String.class))).willReturn(new ArrayList<PortfolioCard>(0));
		given(this.userInfoRepository.findByName(any(String.class))).willReturn(Optional.ofNullable(userInfo));
		
		// then
		assertThrows(NonExistUserException.class, ()->this.portfolioCardService.get(userInfo.getName()));
	}
	
	@Test
	@Transactional
	public void portfolioCardService_Edit_Test(){
		// given
		List<PortfolioCard> cards = new ArrayList<PortfolioCard>();
		cards.add(this.getPortfolioCard(1));
		cards.add(this.getPortfolioCard(2));
		cards.add(this.getPortfolioCard(3));
		
		List<PortfolioCard> editCards = new ArrayList<PortfolioCard>();
		PortfolioCard editedCard = this.getPortfolioCard(1);
		editedCard.setPortfolioCardArticle("edit");
		editCards.add(editedCard);
		
		// when
		given(this.portfolioCardRepository.findByName(any(String.class))).willReturn(cards);
		
		List<PortfolioCard> result = this.portfolioCardService.edit(editCards);
			
		// then
		assertEquals(result.get(0).getUserInfo().getName(), cards.get(0).getUserInfo().getName());
		assertEquals(result.get(0).getPortfolioCardArticle(), "edit");
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
		userInfo.setId(0L);
		userInfo.setName("name");
		userInfo.setProfileUrl("url.helloworld.com");
		return userInfo;
	}
	
}