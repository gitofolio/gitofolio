package com.gitofolio.api.service.user.factory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.gitofolio.api.service.user.UserMapper;
import com.gitofolio.api.service.user.dtos.UserDTO;

@Service
public class PortfolioCardFactory extends UserFactory{
	
	@Autowired
	public PortfolioCardFactory(@Qualifier("portfolioCardService") UserMapper portfolioCardService){
		this.userMapper = portfolioCardService;
	}
	
}