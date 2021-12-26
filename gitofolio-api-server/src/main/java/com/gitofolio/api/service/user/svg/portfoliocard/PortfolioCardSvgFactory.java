package com.gitofolio.api.service.user.svg.portfoliocard;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.gitofolio.api.aop.svg.annotation.SvgStringLineBreaker;
import com.gitofolio.api.service.user.svg.color.colors.Color;
import com.gitofolio.api.service.user.svg.color.ColorFactory;
import com.gitofolio.api.service.user.dtos.UserDTO;

@Service
public class PortfolioCardSvgFactory{
	
	private ColorFactory colorFactory;
	
	@SvgStringLineBreaker(idx=3, width=260)
	public PortfolioCardSvgDTO getPortfolioCardSvgDTO(String name, String url, Integer starNum, String article, String colorName){
		Color color = this.colorFactory.getColor(colorName);
		
		return new PortfolioCardSvgDTO.Builder()
			.name(name)
			.color(color)
			.profileUrl(url)
			.starNum(String.valueOf(starNum))
			.article(article)
			.build();
	}
	
	@Autowired
	public PortfolioCardSvgFactory(ColorFactory colorFactory){
		this.colorFactory = colorFactory;
	}
	
}