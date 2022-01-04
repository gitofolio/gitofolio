package com.gitofolio.api.service.user.factory.crudproxy;

import com.gitofolio.api.service.user.factory.CrudFactory;
import com.gitofolio.api.service.user.proxy.CrudProxy;
import com.gitofolio.api.service.user.dtos.UserDTO;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class PortfolioCardCrudFactory implements CrudFactory<UserDTO>{
	
	private CrudProxy<UserDTO> crudProxy;
	
	@Override
	public CrudProxy<UserDTO> get(){
		return this.crudProxy;
	}
	
	@Autowired
	public PortfolioCardCrudFactory(@Qualifier("portfolioCardCrudProxy") CrudProxy<UserDTO> portfolioCardCrudProxy,
								   @Qualifier("portfolioCardStringCrudProxy") CrudProxy<UserDTO> portfolioCardStringCrudProxy,
								   @Qualifier("portfolioCardLongCrudProxy") CrudProxy<UserDTO> portfolioCardLongCrudProxy,
								   @Qualifier("portfolioCardStringLongCrudProxy") CrudProxy<UserDTO> portfolioCardStringLongCrudProxy){
		this.crudProxy = portfolioCardCrudProxy;
		this.crudProxy.addProxy(portfolioCardStringCrudProxy);
		this.crudProxy.addProxy(portfolioCardLongCrudProxy);
		this.crudProxy.addProxy(portfolioCardStringLongCrudProxy);
	}
	
}