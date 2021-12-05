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
public class PortfolioCardService{
	
	private PortfolioCardRepository portfolioCardRepository;
	
	private UserInfoRepository userInfoRepository;
	
	public List<PortfolioCard> get(String name){
		List<PortfolioCard> portfolioCards = this.portfolioCardRepository.findByName(name);
		
		if(portfolioCards.size() == 0) {
			this.userInfoRepository.findByName(name).orElseThrow(()->new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/portfoliocards/"+name));
			throw new NonExistUserException("이 유저는 어떠한 포트폴리오 카드도 갖고있지 않습니다.", "요청 전 포트폴리오 카드를 생성해주세요", "/portfoliocards/"+name);
		}
		
		return portfolioCards;
	}
	
	public List<PortfolioCard> save(List<PortfolioCard> portfolioCards){
		UserInfo userInfo = this.userInfoRepository.findByName(portfolioCards.get(0).getUserInfo().getName())
			.orElseThrow(()->new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/portfoliocards/"));
		
		for(PortfolioCard portfolioCard : portfolioCards){
			portfolioCard.setUserInfo(userInfo);
			portfolioCardRepository.save(portfolioCard);
		}
		
		return get(portfolioCards.get(0).getUserInfo().getName());
	}

	public void delete(String name){
		this.userInfoRepository.findByName(name).orElseThrow(()->new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/portfoliocards/"+name));
		this.portfolioCardRepository.deleteByName(name);
		return;
	}
	
	public void delete(String name, String parameter){
		this.userInfoRepository.findByName(name).orElseThrow(()->new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/portfoliocards/"+name));
		int delIdx = 0;
		try{
			delIdx = Integer.parseInt(parameter);
		}catch(Exception e){
			throw new IllegalParameterException("잘못된 파라미터 요청", "포트폴리오 카드 범위삭제 파라미터에 오류가 있습니다.", "https://api.gitofolio.com/portfoliocards/user?card="+parameter);
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