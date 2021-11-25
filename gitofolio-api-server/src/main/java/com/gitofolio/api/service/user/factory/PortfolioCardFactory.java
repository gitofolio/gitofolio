package com.gitofolio.api.service.user.factory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.UserMapper;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.exception.IllegalParameterException;
import com.gitofolio.api.service.user.parameter.ParameterHandler;
import com.gitofolio.api.service.user.factory.hateoas.Hateoas;

@Service
public class PortfolioCardFactory implements UserFactory{
	
	private UserMapper portfolioCardService;
	private ParameterHandler<String> parameterHandler;
	private Hateoas portfolioCardHateoas;
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name){
		return this.setHateoas(this.portfolioCardService.doMap(name));
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name, Object parameter){
		if(!parameter.getClass().equals(String.class)) 
			throw new IllegalParameterException("잘못된 파라미터 요청", "포트폴리오 카드요청 파라미터를 잘못 입력하셨습니다.", "https://api.gitofolio.com/user/portfoliocards");
		UserDTO userDTO = this.portfolioCardService.doMap(name);
		return this.setHateoas(parameterHandler.doHandle(userDTO, (String)parameter));
	}
	
	@Override
	@Transactional
	public UserDTO saveUser(UserDTO userDTO){
		return this.setHateoas(this.portfolioCardService.resolveMap(userDTO));
	}
	
	@Override
	public UserDTO setHateoas(UserDTO userDTO){
		userDTO.setLinks(this.portfolioCardHateoas.getLinks());
		return userDTO;
	}
	
	@Autowired
	public PortfolioCardFactory(@Qualifier("portfolioCardService") UserMapper portfolioCardService,
							   @Qualifier("portfolioCardsGetParameterHandler") ParameterHandler<String> parameterHandler,
							   @Qualifier("portfolioCardHateoas") Hateoas portfolioCardHateoas){
		this.portfolioCardService = portfolioCardService;
		this.parameterHandler = parameterHandler;
		this.portfolioCardHateoas = portfolioCardHateoas;
	}
	
}