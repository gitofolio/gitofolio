package com.gitofolio.api.service.user;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.dtos.PortfolioCardDTO;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.exception.NonExistUserException;
import com.gitofolio.api.service.user.exception.IllegalParameterException;
import com.gitofolio.api.repository.user.PortfolioCardRepository;
import com.gitofolio.api.repository.user.UserInfoRepository;
import com.gitofolio.api.domain.user.PortfolioCard;
import com.gitofolio.api.domain.user.UserInfo;

import java.util.List;
import java.util.ArrayList;

@Service
public class PortfolioCardService implements UserMapper{
	
	private PortfolioCardRepository portfolioCardRepository;
	
	private UserInfoRepository userInfoRepository;
	
	@Override
	public UserDTO doMap(String name){
		List<PortfolioCard> portfolioCards = this.portfolioCardRepository.findByName(name);
		
		if(portfolioCards.size() == 0) {
			this.userInfoRepository.findByName(name).orElseThrow(()->new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/portfoliocards/"+name));
			return new UserDTO.Builder().build();
		}
		
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
	public UserDTO resolveMap(UserDTO userDTO){
		UserInfo userInfo = this.userInfoRepository.findByName(userDTO.getName()).orElseThrow(()->new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/portfoliocards/"));
		
		List<PortfolioCardDTO> portfolioCardDTOs = userDTO.getPortfolioCards();
		for(PortfolioCardDTO portfolioCardDTO : portfolioCardDTOs){
			PortfolioCard portfolioCard = new PortfolioCard();
			portfolioCard.setPortfolioCardArticle(portfolioCardDTO.getPortfolioCardArticle());
			portfolioCard.setPortfolioCardStars(portfolioCardDTO.getPortfolioCardStars());
			portfolioCard.setPortfolioUrl(portfolioCardDTO.getPortfolioUrl());
			portfolioCard.setUserInfo(userInfo);
			
			portfolioCardRepository.save(portfolioCard);
		}
		
		return this.doMap(userDTO.getName());
	}

	public void deletePortfolioCard(String name){
		this.portfolioCardRepository.deleteByName(name);
		return;
	}
	
	public void deletePortfolioCard(String name, String parameter){
		int delIdx = 0;
		try{
			delIdx = Integer.parseInt(parameter);
		}catch(Exception e){
			throw new IllegalParameterException("잘못된 파라미터 요청", "포트폴리오 카드 범위삭제 파라미터에 오류가 있습니다.", "https://api.gitofolio.com/user/portfoliocards?card="+parameter);
		}
		
		List<PortfolioCard> portfolioCards = this.portfolioCardRepository.findByName(name);
		this.portfolioCardRepository.deleteById(portfolioCards.get(delIdx-1).getId());
				   
		return;
	}
	
	@Autowired
	public PortfolioCardService(PortfolioCardRepository portfolioCardRepository
							   , UserInfoRepository userInfoRepository){
		this.portfolioCardRepository = portfolioCardRepository;
		this.userInfoRepository = userInfoRepository;
	}
	
}