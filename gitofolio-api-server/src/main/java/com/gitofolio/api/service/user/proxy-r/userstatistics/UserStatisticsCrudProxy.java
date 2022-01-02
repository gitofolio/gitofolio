package com.gitofolio.api.service.user.proxy.userstatisics;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.proxy.CrudProxy;
import com.gitofolio.api.service.user.factory.mapper.UserMapper;
import com.gitofolio.api.service.user.UserStatisticsService;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.domain.user.UserStatistics;

@Service
public class UserStatisticsCrudProxy implements CrudProxy<UserDTO>{
	
	private final CrudProxy<UserDTO> crudProxy;
	private final UserMapper<UserStatistics> userStatisticsMapper;
	private final UserStatisticsService userStatisticsService;
	
	@Override
	@Transactional
	public UserDTO create(Object ...args){
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
		return this.crudProxy.update(args);
	}
	
	@Override
	@Transactional
	public void delete(Object ...args){
		this.crudProxy.delete(args);
	}
	
	@Autowired
	public UserStatisticsCrudProxy(@Qualifier("userStatisticsStringCrudProxy") CrudProxy<UserDTO> crudProxy,
								  @Qualifier("userStatisticsMapper") UserMapper<UserStatistics> userStatisticsMapper,
								  UserStatisticsService userStatisticsService){
		this.crudProxy = crudProxy;
		this.userStatisticsMapper = userStatisticsMapper;
		this.userStatisticsService = userStatisticsService;
	}
	
}