package com.gitofolio.api.service.user.eraser;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.PortfolioCardService;
import com.gitofolio.api.service.user.exception.IllegalParameterException;

@Service
public class PortfolioCardEraser implements UserEraser{
	
	private PortfolioCardService portfolioCardService;
	
	@Override
	@Transactional
	public String delete(String name){
		this.portfolioCardService.delete(name);
		return name;
	}
	
	@Override
	@Transactional
	public String delete(String name, Object parameter){
		if(!parameter.getClass().equals(String.class)) 
			throw new IllegalParameterException("잘못된 파라미터 요청", "포트폴리오 카드 삭제 파라미터를 잘못 입력하셨습니다.", "https://api.gitofolio.com/portfoliocards/user");
		try{
			int checkParameter = Integer.parseInt((String)parameter);
		} catch(Exception e){
			throw new IllegalParameterException("잘못된 파라미터 요청", "포트폴리오 카드요청 파라미터를 잘못 입력하셨습니다.", "https://api.gitofolio.com/portfoliocards/user?card="+parameter);
		}
		this.portfolioCardService.delete(name, (String)parameter);
		return name;
	}
	
	@Autowired
	public PortfolioCardEraser(PortfolioCardService portfolioCardService){
		this.portfolioCardService = portfolioCardService;
	}
	
}