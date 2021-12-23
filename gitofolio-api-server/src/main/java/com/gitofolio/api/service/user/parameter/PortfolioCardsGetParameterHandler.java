package com.gitofolio.api.service.user.parameter;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.dtos.PortfolioCardDTO;
import com.gitofolio.api.service.user.exception.IllegalParameterException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

import static java.lang.Math.min;

@Service
public class PortfolioCardsGetParameterHandler<T extends String> implements ParameterHandler<T>{
	
	@Override
	public UserDTO doHandle(UserDTO userDTO, T parameter){
		int from = 0;
		int to = 0;
		try{
			String[] params = parameter.split(",");
			from = Integer.parseInt(params[0]);
			to = Integer.parseInt(params[1]);
		}catch(Exception e){
			throw new IllegalParameterException("잘못된 파라미터 요청", "포트폴리오 카드요청 파라미터를 잘못 입력하셨습니다.", "https://api.gitofolio.com/portfoliocards/user/cards="+parameter);
		}
		List<PortfolioCardDTO> portfolioCards = userDTO.getPortfolioCards();
		List<PortfolioCardDTO> removedPortfolioCards = new ArrayList<PortfolioCardDTO>(portfolioCards.size());
		
		userDTO.setPortfolioCards(removedPortfolioCards);
		
		for(int i = 1; i <= portfolioCards.size(); i++) 
			if(i >= from && i <= to) removedPortfolioCards.add(portfolioCards.get(i-1));
		
		return userDTO;
	}
	
}