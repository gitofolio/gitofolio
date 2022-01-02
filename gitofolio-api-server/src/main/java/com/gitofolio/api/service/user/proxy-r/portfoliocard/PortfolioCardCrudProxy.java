package com.gitofolio.api.service.user.proxy.portfoliocard;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.factory.mapper.UserMapper;
import com.gitofolio.api.service.user.proxy.CrudProxy;
import com.gitofolio.api.service.user.PortfolioCardService;
import com.gitofolio.api.domain.user.PortfolioCard;

import java.util.List;

@Service
public class PortfolioCardCrudProxy implements CrudProxy<UserDTO>{
	
	private final CrudProxy<UserDTO> crudProxy;
	private final UserMapper<List<PortfolioCard>> portfolioCardMapper;
	private final PortfolioCardService portfolioCardService;
	
	@Override
	@Transactional
	public UserDTO create(Object ...args){
		if(args.length==1 && args.getClass().equals(UserDTO.class)){
			return this.portfolioCardMapper.doMap(
				this.portfolioCardService.save(
					this.portfolioCardMapper.resolveMap((UserDTO)args[0])
				)
			);
		}
		return this.crudProxy.create(args);
	}
	
	@Override
	@Transactional(readOnly=true)
	public UserDTO read(Object ...args){
		return this.crudProxy.read(args);
	}
	
	@Override
	@Transactional
	public UserDTO update(Object ...args){
		if(args.length==1 && args.getClass().equals(UserDTO.class)){
			return this.portfolioCardMapper.doMap(
				this.portfolioCardService.edit(
					this.portfolioCardMapper.resolveMap((UserDTO)args[0])
				)
			);
		}
		return this.crudProxy.update(args);
	}
	
	@Override
	@Transactional
	public void delete(Object ...args){
		this.crudProxy.delete(args);
	}
	
	@Autowired
	public PortfolioCardCrudProxy(@Qualifier("portfolioCardStringCrudProxy") CrudProxy<UserDTO> crudProxy,
								 @Qualifier("portfolioCardMapper") UserMapper<List<PortfolioCard>> portfolioCardMapper,
								 PortfolioCardService portfolioCardService){
		this.crudProxy = crudProxy;
		this.portfolioCardMapper = portfolioCardMapper;
		this.portfolioCardService = portfolioCardService;
	}
	
}