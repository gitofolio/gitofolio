package com.gitofolio.api.service.user.svg.portfoliocard;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.gitofolio.api.aop.svg.annotation.SvgStringLineBreaker;
import com.gitofolio.api.service.user.svg.color.colors.Color;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.factory.Factory;
import com.gitofolio.api.service.factory.parameter.PortfolioCardSvgParameter;

@Service
public class PortfolioCardSvgFactory implements Factory<PortfolioCardSvgDTO, PortfolioCardSvgParameter>{
	
	private Factory<Color, String> colorFactory;
	
	@Override
	@SvgStringLineBreaker(idx=0, width=260)
	public PortfolioCardSvgDTO get(PortfolioCardSvgParameter parameter){
		Color color = this.colorFactory.get(parameter.getColorName());
		
		return new PortfolioCardSvgDTO.Builder()
			.name(parameter.getName())
			.portfolioUrl(parameter.getPortfolioUrl())
			.color(color)
			.base64EncodedImage(parameter.getEncodedImage())
			.watchedNum(String.valueOf(parameter.getWatchedNum()))
			.article(parameter.getArticle())
			.build();
	}
	
	@Autowired
	public PortfolioCardSvgFactory(@Qualifier("colorFactory") Factory<Color, String> colorFactory){
		this.colorFactory = colorFactory;
	}
	
}
