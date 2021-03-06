package com.gitofolio.api.service.user;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.dtos.*;
import com.gitofolio.api.service.user.exception.*;
import com.gitofolio.api.repository.user.*;
import com.gitofolio.api.domain.user.*;

import java.util.*;
import java.lang.reflect.Field;

@Service
public class PortfolioCardService{
	
	private PortfolioCardRepository portfolioCardRepository;
	private UserInfoRepository userInfoRepository;
	private int portfolioCardLimitCnt = 5;
	
	public List<PortfolioCard> get(String name){
		List<PortfolioCard> portfolioCards = this.portfolioCardRepository.findByName(name);
		
		if(portfolioCards.isEmpty()) {
			this.userInfoRepository.findByName(name).orElseThrow(()->new NonExistUserException("NonExistUserException", "존재하지 않는 유저 입니다 유저이름을 확인해 주세요.", "/portfoliocards/"+name));
			throw new NonExistUserException("NonExistUserException", "이 유저는 어떠한 포트폴리오 카드도 갖고있지 않습니다 요청 전 포트폴리오 카드를 생성해주세요", "/portfoliocards/"+name);
		}
		
		return portfolioCards;
	}
	
	public List<PortfolioCard> get(String name, Long id){
		PortfolioCard portfolioCard = this.portfolioCardRepository.findById(id)
			.orElseThrow(()->new IllegalParameterException("IllegalParameterException", "존재 하지 않는 포트폴리오 카드 id 입니다 포트폴리오 카드 id값을 확인해 주세요.", "/portfoliocards"));
		
		if(!portfolioCard.isPortfolioCardOwner(name)) throw new IllegalParameterException("IllegalParameterException", "이 포트폴로이 카드는 해당 유저의 소유가 아닙니다.", "/portfoliocards");
		
		return this.generateCardArrays(portfolioCard);
	}
	
	public List<PortfolioCard> get(Long id){
		PortfolioCard portfolioCard = this.portfolioCardRepository.findById(id)
			.orElseThrow(()->new IllegalParameterException("IllegalParameterException", "존재 하지 않는 포트폴리오 카드 id 입니다 포트폴리오 카드 id값을 확인해 주세요.", "/portfoliocard/"+id));
		
		portfolioCard.increasePortfolioCardWatched();
		
		return this.generateCardArrays(portfolioCard);
	}
	
	private List<PortfolioCard> generateCardArrays(PortfolioCard portfolioCard){
		return new ArrayList<PortfolioCard>(Arrays.asList(portfolioCard));
	}
	
	public List<PortfolioCard> save(List<PortfolioCard> portfolioCards){
		UserInfo userInfo = this.userInfoRepository.findByName(portfolioCards.get(0).getUserInfo().getName())
			.orElseThrow(()->new NonExistUserException("NonExistUserException", "존재 하지 않는 유저 입니다 유저이름을 확인해 주세요.", "/portfoliocards/"));
		
		if(this.isPortfolioCardCntReachedLimit(userInfo.getName())) throw new EditException("EditException", "포트폴리오카드 최대 생성 가능 갯수에 도달했습니다.");
		
		for(PortfolioCard portfolioCard : portfolioCards){
			portfolioCard.setUserInfo(userInfo);
			portfolioCardRepository.save(portfolioCard);
		}
		
		return get(portfolioCards.get(0).getUserInfo().getName());
	}
	
	private boolean isPortfolioCardCntReachedLimit(String name){
		List<PortfolioCard> portfolioCards = this.portfolioCardRepository.findByName(name);
		if(portfolioCards.size() >= this.portfolioCardLimitCnt) return true;
		return false;
	}
	
	public void delete(String name){
		this.userInfoRepository.findByName(name).orElseThrow(()->new NonExistUserException("NonExistUserException", "존재 하지 않는 유저 입니다 유저이름을 확인해 주세요.", "/portfoliocards/"+name));
		this.portfolioCardRepository.deleteByName(name);
		return;
	}
	
	public void delete(String name, Long parameter){
		this.userInfoRepository.findByName(name).orElseThrow(()->new NonExistUserException("NonExistUserException", "존재 하지 않는 유저 입니다 유저이름을 확인해 주세요.", "/portfoliocards/"+name));
		
		List<PortfolioCard> portfolioCards = this.portfolioCardRepository.findByName(name);
		this.portfolioCardRepository.deleteById(parameter);
		
		return;
	}
	
	public List<PortfolioCard> edit(List<PortfolioCard> portfolioCards){
		
		String name = portfolioCards.get(0).getUserInfo().getName();
		Long editId = portfolioCards.get(0).getId();
		
		List<PortfolioCard> exist = this.portfolioCardRepository.findByName(name);
		if(exist.isEmpty()) {
			this.userInfoRepository.findByName(name).orElseThrow(()->new NonExistUserException("NonExistUserException", "존재 하지 않는 유저 입니다 유저이름을 확인해 주세요.", "/portfoliocards"));
			throw new NonExistUserException("NonExistUserException", "이 유저는 어떠한 포트폴리오 카드도 갖고있지 않습니다. 요청 전 포트폴리오 카드를 생성해주세요", "/portfoliocards");
		}
		
		try{
			PortfolioCard oldPortfolioCard = this.findPortfolioCardBasedOnId(editId, exist);
			PortfolioCard newPortfolioCard = portfolioCards.get(0);
			doEdit(oldPortfolioCard, newPortfolioCard);
		}catch(IllegalAccessException IAE){
			throw new EditException("EditException", "카드를 수정하는데 실패했습니다. HTTP Body의 요청 JSON을 확인해주세요", "/portfoliocards/"+name);
		}catch(NullPointerException NPE){
			throw new IllegalParameterException("IllegalParameterException", "요청하신 번호에 해당하는 포토폴리오 카드를 찾을 수 없습니다.", "https://api.gitofolio.com/portfoliocards");
		}
		
		return this.get(name);
	}
	
	private void doEdit(PortfolioCard oldPortfolioCard, PortfolioCard newPortfolioCard) throws IllegalAccessException{
		Field[] oldFields = oldPortfolioCard.getClass().getDeclaredFields();
		Field[] newFields = newPortfolioCard.getClass().getDeclaredFields();
	
		for(Field oldField : oldFields){
			oldField.setAccessible(true);
			Object oldFieldValue = oldField.get(oldPortfolioCard);
			if(oldFieldValue == null || !isFieldEditAble(oldField)) continue;
			
			for(Field newField : newFields){
				newField.setAccessible(true);
				Object newFieldValue = newField.get(newPortfolioCard);
				if(newFieldValue == null || !isFieldEditAble(newField)) continue;
				
				if(oldField.getName().equals(newField.getName())) oldField.set(oldPortfolioCard, newFieldValue);
			}
		}
	}
	
	private PortfolioCard findPortfolioCardBasedOnId(Long id, List<PortfolioCard> cards) throws NullPointerException{
		PortfolioCard ans = null;
		for(PortfolioCard card : cards) if(card.getId().equals(id)) ans = card;
		if(ans.getId().equals(id)) return ans;
		return null; // never reached!
	}
	
	private boolean isFieldEditAble(Field field){
		if(field.getName().equals("id")
		   || field.getName().equals("userInfo") 
		   || field.getName().equals("portfolioCardWatched")) return false;
		
		return true;
	}
	
	@Autowired
	public PortfolioCardService(PortfolioCardRepository portfolioCardRepository
							   , UserInfoRepository userInfoRepository){
		this.portfolioCardRepository = portfolioCardRepository;
		this.userInfoRepository = userInfoRepository;
	}
	
}