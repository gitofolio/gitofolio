package com.gitofolio.api.service.user.factory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.UserMapper;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.exception.IllegalParameterException;
import com.gitofolio.api.service.user.parameter.ParameterHandler;

@Service
public class PortfolioCardFactory implements UserFactory{
	
	private UserMapper portfolioCardService;
	private UserFactory userInfoFactory;
	private ParameterHandler<String> parameterHandler;
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name){
		return this.portfolioCardService.doMap(name);
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name, Object parameter){
		if(!parameter.getClass().equals(String.class)) 
			throw new IllegalParameterException("잘못된 파라미터 요청", "포트폴리오 카드요청 파라미터를 잘못 입력하셨습니다.", "https://api.gitofolio.com/user/portfoliocards");
		UserDTO userDTO = this.portfolioCardService.doMap(name);
		return parameterHandler.doHandle(userDTO, (String)parameter);
	}
	
	@Override
	@Transactional
	public UserDTO saveUser(UserDTO userDTO){
		this.userInfoFactory.saveUser(userDTO);
		return this.portfolioCardService.resolveMap(userDTO);
	}
	
	@Autowired
	public PortfolioCardFactory(@Qualifier("portfolioCardService") UserMapper portfolioCardService,
							   @Qualifier("userInfoFactory") UserFactory userInfoFactory,
							   @Qualifier("portfolioCardsParameterHandler") ParameterHandler<String> parameterHandler){
		this.portfolioCardService = portfolioCardService;
		this.userInfoFactory = userInfoFactory;
		this.parameterHandler = parameterHandler;
	}
	
}