package com.gitofolio.api.service.factory.crudproxy;

import com.gitofolio.api.service.factory.CrudFactory;
import com.gitofolio.api.service.proxy.CrudProxy;
import com.gitofolio.api.service.user.dtos.UserDTO;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class UserStatisticsCrudFactory implements CrudFactory<UserDTO>{
	
	private final CrudProxy<UserDTO> crudProxy;
	
	@Override
	public CrudProxy<UserDTO> get(){
		return this.crudProxy;
	}
	
	@Autowired
	public UserStatisticsCrudFactory(@Qualifier("userStatisticsCrudProxy") CrudProxy<UserDTO> userStatisticsCrudProxy,
								  @Qualifier("userStatisticsStringCrudProxy") CrudProxy<UserDTO> userStatisticsStringCrudProxy){
		this.crudProxy = userStatisticsCrudProxy;
		this.crudProxy.addProxy(userStatisticsStringCrudProxy);
	}
	
}