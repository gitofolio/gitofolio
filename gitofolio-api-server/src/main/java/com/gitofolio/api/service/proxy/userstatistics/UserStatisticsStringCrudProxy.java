package com.gitofolio.api.service.proxy.userstatistics;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.gitofolio.api.service.proxy.CrudProxy;
import com.gitofolio.api.service.factory.mapper.UserMapper;
import com.gitofolio.api.service.user.UserStatisticsService;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.domain.user.UserStatistics;

@Service
public class UserStatisticsStringCrudProxy implements CrudProxy<UserDTO>{
	
	private CrudProxy<UserDTO> crudProxy = null;
	private final UserMapper<UserStatistics> userStatisticsMapper;
	private final UserStatisticsService userStatisticsService;
	
	@Override
	public UserDTO create(Object ...args){
		return this.crudProxy.create(args);
	}
	
	@Override
	public UserDTO read(Object ...args){
		if(args.length==1 && args[0].getClass().equals(String.class)) return this.userStatisticsMapper.doMap(this.userStatisticsService.get((String)args[0]));
		return this.crudProxy.read(args);
	}
	
	@Override
	public UserDTO update(Object ...args){
		return this.crudProxy.update(args);
	}
	
	@Override
	public void delete(Object ...args){
		if(args.length==1 && args[0].getClass().equals(String.class)) this.userStatisticsService.delete((String)args[0]);
		else this.crudProxy.delete(args);
	}
	
	@Override
	public void addProxy(CrudProxy<UserDTO> crudProxy){
		if(this.crudProxy == null) this.crudProxy = crudProxy;
		else this.crudProxy.addProxy(crudProxy);
	}
	
	@Autowired
	public UserStatisticsStringCrudProxy(@Qualifier("userStatisticsMapper") UserMapper<UserStatistics> userStatisticsMapper,
										 UserStatisticsService userStatisticsService){
		this.userStatisticsMapper = userStatisticsMapper;
		this.userStatisticsService = userStatisticsService;
	}
	
}