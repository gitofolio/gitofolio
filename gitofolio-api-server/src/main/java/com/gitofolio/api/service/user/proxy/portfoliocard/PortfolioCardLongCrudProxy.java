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
public class PortfolioCardLongCrudProxy implements CrudProxy<UserDTO>{
	
	private CrudProxy<UserDTO> crudProxy = null;
	private final UserMapper<List<PortfolioCard>> portfolioCardMapper;
	private final PortfolioCardService portfolioCardService;
	
	@Override
	public UserDTO create(Object ...args){
		return this.crudProxy.create(args);
	}
	
	@Override
	public UserDTO read(Object ...args){
		if(args.length==1 && args[0].getClass().equals(Long.class)){
			return this.portfolioCardMapper.doMap(
				this.portfolioCardService.get((Long)args[0])
			);
		}
		return this.crudProxy.read(args);
	}
	
	@Override
	public UserDTO update(Object ...args){
		return this.crudProxy.update(args);
	}
	
	@Override
	public void delete(Object ...args){
		this.crudProxy.delete(args);
	}
	
	@Override
	public void addProxy(CrudProxy<UserDTO> crudProxy){
		if(this.crudProxy == null) this.crudProxy = crudProxy;
		else this.crudProxy.addProxy(crudProxy);
	}
	
	@Autowired
	public PortfolioCardLongCrudProxy(@Qualifier("portfolioCardMapper") UserMapper<List<PortfolioCard>> portfolioCardMapper,
									  PortfolioCardService portfolioCardService){
		this.portfolioCardMapper = portfolioCardMapper;
		this.portfolioCardService = portfolioCardService;
	}
	
}