package com.gitofolio.api.service.user.factory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.UserMapper;
import com.gitofolio.api.service.user.dtos.UserDTO;

@Service
public class PortfolioCardFactory implements UserFactory{
	
	private UserMapper portfolioCardService;
	private UserFactory userInfoFactory;
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name){
		return this.portfolioCardService.doMap(name);
	}
	
	@Override
	@Transactional
	public UserDTO saveUser(UserDTO userDTO){
		this.userInfoFactory.saveUser(userDTO);
		return this.portfolioCardService.resolveMap(userDTO);
	}
	
	@Autowired
	public PortfolioCardFactory(@Qualifier("portfolioCardService") UserMapper portfolioCardService,
							   @Qualifier("userInfoFactory") UserFactory userInfoFactory){
		this.portfolioCardService = portfolioCardService;
		this.userInfoFactory = userInfoFactory;
	}
	
}