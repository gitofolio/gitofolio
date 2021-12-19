package com.gitofolio.api.service.user.factory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.gitofolio.api.service.user.factory.mapper.UserMapper;
import com.gitofolio.api.service.user.PortfolioCardService;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.domain.user.PortfolioCard;
import com.gitofolio.api.service.user.exception.IllegalParameterException;
import com.gitofolio.api.service.user.parameter.ParameterHandler;
import com.gitofolio.api.service.user.factory.hateoas.Hateoas;

@Service
public class PortfolioCardFactory implements UserFactory{
	
	private PortfolioCardService portfolioCardService;
	private UserMapper<List<PortfolioCard>> portfolioCardMapper;
	private ParameterHandler<String> parameterHandler;
	private Hateoas portfolioCardHateoas;
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name){
		return this.setHateoas(
			this.portfolioCardMapper.doMap(
				this.portfolioCardService.get(name)
			)
		);
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name, Object parameter){
		if(!parameter.getClass().equals(String.class)) 
			throw new IllegalParameterException("잘못된 파라미터 요청", "포트폴리오 카드요청 파라미터를 잘못 입력하셨습니다.", "https://api.gitofolio.com/portfoliocards");
		
		UserDTO userDTO = this.getUser(name);
		
		return this.setHateoas(parameterHandler.doHandle(userDTO, (String)parameter));
	}
	
	@Override
	@Transactional
	public UserDTO saveUser(UserDTO userDTO){
		return this.setHateoas(
			this.portfolioCardMapper.doMap(
				this.portfolioCardService.save(
					this.portfolioCardMapper.resolveMap(userDTO)
				)
			)
		);
	}
	
	@Override
	@Transactional
	public UserDTO editUser(UserDTO userDTO, Object parameter){
		throw new IllegalParameterException("잘못된 파라미터 요청", "포트폴리오 카드요청 파라미터를 잘못 입력하셨습니다.", "https://api.gitofolio.com/portfoliocards");
	}
	
	@Override
	@Transactional
	public UserDTO editUser(UserDTO userDTO){
		return this.setHateoas(
			this.portfolioCardMapper.doMap(
				this.portfolioCardService.edit(
					this.portfolioCardMapper.resolveMap(userDTO)
				)
			)
		);
	}
	
	private UserDTO setHateoas(UserDTO userDTO){
		userDTO.setLinks(this.portfolioCardHateoas.getLinks());
		return userDTO;
	}
	
	@Autowired
	public PortfolioCardFactory(PortfolioCardService portfolioCardService,
								@Qualifier("portfolioCardMapper") UserMapper<List<PortfolioCard>> portfolioCardMapper,
								@Qualifier("portfolioCardsGetParameterHandler") ParameterHandler<String> parameterHandler,
								@Qualifier("portfolioCardHateoas") Hateoas portfolioCardHateoas){
		this.portfolioCardService = portfolioCardService;
		this.parameterHandler = parameterHandler;
		this.portfolioCardHateoas = portfolioCardHateoas;
		this.portfolioCardMapper = portfolioCardMapper;
	}
	
}