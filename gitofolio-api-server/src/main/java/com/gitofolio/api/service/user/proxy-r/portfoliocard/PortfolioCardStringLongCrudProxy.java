package com.gitofolio.api.service.user.proxy.portfoliocard;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.factory.mapper.UserMapper;
import com.gitofolio.api.service.user.proxy.CrudProxy;
import com.gitofolio.api.service.user.PortfolioCardService;
import com.gitofolio.api.domain.user.PortfolioCard;

import java.util.List;

@Service
public class PortfolioCardStringLongCrudProxy implements CrudProxy<UserDTO>{
	
	private final CrudProxy<UserDTO> crudProxy = null;
	private final UserMapper<List<PortfolioCard>> portfolioCardMapper;
	private final PortfolioCardService portfolioCardService;
	
	@Override
	public UserDTO create(Object ...args){
		return this.crudProxy.create(args);
	}
	
	@Override
	public UserDTO read(Object ...args){
		return this.crudProxy.read(args);
	}
	
	@Override
	public UserDTO update(Object ...args){
		return this.crudProxy.update(args);
	}
	
	@Override
	public void delete(Object ...args){
		if(args.length==2 && (args[0].getClass().equals(String.class) && args[1].getClass().equals(Long.class))) this.portfolioCardService.delete((String)args[0], (Long)args[1]);
		this.crudProxy.delete(args);
	}
	
	@Autowired 
	public PortfolioCardStringLongCrudProxy(@Qualifier("portfolioCardMapper") UserMapper<List<PortfolioCard>> portfolioCardMapper,
											PortfolioCardService portfolioCardService){
		this.portfolioCardMapper = portfolioCardMapper;
		this.portfolioCardService = portfolioCardService;
	}
	
}