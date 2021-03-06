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
		UserInfo userInfo = this.getUserInfo();
		List<PortfolioCard> cards = new ArrayList<PortfolioCard>();
		cards.add(this.getPortfolioCard(1, userInfo));
		cards.add(this.getPortfolioCard(2, userInfo));
		cards.add(this.getPortfolioCard(3, userInfo));
		
		// when
		given(this.portfolioCardRepository.findByName(any(String.class))).willReturn(cards);
		
		List<PortfolioCard> result = this.portfolioCardService.get(cards.get(0).getUserInfo().getName());
		
		// then
		assertEquals(result.get(0).getUserInfo().getName(), cards.get(0).getUserInfo().getName());
		assertEquals(result.size(), 3);
	}
	
	@Test
	@Transactional
	public void portfolioCardService_portfolioCard_reached_limit_TEST(){
		// given
		UserInfo userInfo = this.getUserInfo();
		List<PortfolioCard> cards = new ArrayList<PortfolioCard>();
		cards.add(this.getPortfolioCard(1, userInfo));
		cards.add(this.getPortfolioCard(2, userInfo));
		cards.add(this.getPortfolioCard(3, userInfo));
		cards.add(this.getPortfolioCard(4, userInfo));
		cards.add(this.getPortfolioCard(5, userInfo));
		
		// when
		given(this.portfolioCardRepository.findByName(any(String.class))).willReturn(cards);
		given(this.userInfoRepository.findByName(any(String.class))).willReturn(Optional.ofNullable(userInfo));
		
		// then
		assertThrows(EditException.class, ()->this.portfolioCardService.save(cards));
	}
	
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
		UserInfo userInfo = this.getUserInfo();
		List<PortfolioCard> cards = new ArrayList<PortfolioCard>();
		cards.add(this.getPortfolioCard(1, userInfo));
		cards.add(this.getPortfolioCard(2, userInfo));
		cards.add(this.getPortfolioCard(3, userInfo));
		
		List<PortfolioCard> editCards = new ArrayList<PortfolioCard>();
		PortfolioCard editedCard = this.getPortfolioCard(1, userInfo);
		editedCard.setPortfolioCardArticle("edit");
		editCards.add(editedCard);
		
		// when
		given(this.portfolioCardRepository.findByName(any(String.class))).willReturn(cards);
		
		List<PortfolioCard> result = this.portfolioCardService.edit(editCards);
			
		// then
		assertEquals(result.get(0).getUserInfo().getName(), cards.get(0).getUserInfo().getName());
		assertEquals(result.get(0).getPortfolioCardArticle(), "edit");
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
	
	private UserInfo getUserInfo(){
		UserInfo userInfo = new UserInfo();
		userInfo.setId(0L);
		userInfo.setName("name");
		userInfo.setProfileUrl("url.helloworld.com");
		return userInfo;
	}
	
}