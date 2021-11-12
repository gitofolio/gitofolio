package com.gitofolio.api.service.user.eraser;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.PortfolioCardService;

@Service
public class PortfolioCardEraser implements UserEraser{
	
	private PortfolioCardService portfolioCardService;
	
	@Override
	@Transactional
	public String delete(String name){
		this.portfolioCardService.deletePortfolioCard(name);
		return name;
	}
	
	@Autowired
	public PortfolioCardEraser(PortfolioCardService portfolioCardService){
		this.portfolioCardService = portfolioCardService;
	}
	
}