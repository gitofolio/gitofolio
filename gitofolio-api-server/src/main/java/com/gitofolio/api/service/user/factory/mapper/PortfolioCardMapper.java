package com.gitofolio.api.service.user.factory.mapper;

import com.gitofolio.api.domain.user.UserInfo;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.dtos.PortfolioCardDTO;
import com.gitofolio.api.domain.user.PortfolioCard;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class PortfolioCardMapper implements UserMapper<List<PortfolioCard>>{
	
	@Override
	public UserDTO doMap(List<PortfolioCard> portfolioCards){
		
		List<PortfolioCardDTO> cards = new ArrayList<PortfolioCardDTO>(portfolioCards.size());
		for(PortfolioCard card : portfolioCards){
			cards.add(new PortfolioCardDTO.Builder()
				.portfolioCard(card)
				.build());
		}
		
		return new UserDTO.Builder()
			.userInfo(portfolioCards.get(0).getUserInfo())
			.portfolioCardDTOs(cards)
			.build();
	}
	
	@Override
	public List<PortfolioCard> resolveMap(UserDTO userDTO){
		UserInfo mockUserInfo = getMockUserInfo(userDTO);
		
		List<PortfolioCard> portfolioCards = new ArrayList<PortfolioCard>();
		List<PortfolioCardDTO> portfolioCardDTOs = userDTO.getPortfolioCards();
		for(PortfolioCardDTO cardDTO : portfolioCardDTOs){
			PortfolioCard portfolioCard = new PortfolioCard();
			
			try{
				portfolioCard.setId(cardDTO.getId());
			}catch(NullPointerException NPE){}
			
			portfolioCard.setPortfolioCardArticle(cardDTO.getPortfolioCardArticle());
			portfolioCard.setPortfolioCardStars(cardDTO.getPortfolioCardStars());
			portfolioCard.setPortfolioUrl(cardDTO.getPortfolioUrl());
			
			portfolioCard.setUserInfo(mockUserInfo);
			
			portfolioCards.add(portfolioCard);
		}
		
		return portfolioCards;
	}
	
	private UserInfo getMockUserInfo(UserDTO userDTO){
		UserInfo mockUserInfo = new UserInfo();
		mockUserInfo.setName(userDTO.getName());
		try{
			mockUserInfo.setId(userDTO.getId());
		}catch(NullPointerException NPE){}
		return mockUserInfo;
	}
	
}