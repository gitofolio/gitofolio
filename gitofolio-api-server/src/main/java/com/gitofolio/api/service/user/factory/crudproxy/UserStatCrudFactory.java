package com.gitofolio.api.service.user.factory.crudproxy;

import com.gitofolio.api.service.user.factory.CrudFactory;
import com.gitofolio.api.service.user.proxy.CrudProxy;
import com.gitofolio.api.service.user.dtos.UserDTO;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class UserStatCrudFactory implements CrudFactory<UserDTO>{
	
	private final CrudProxy<UserDTO> crudProxy;
	
	@Override
	public CrudProxy<UserDTO> get(){
		return this.crudProxy;
	}
	
	@Autowired
	public UserStatCrudFactory(@Qualifier("userStatCrudProxy") CrudProxy<UserDTO> userStatCrudProxy,
							  @Qualifier("userStatStringCrudProxy") CrudProxy<UserDTO> userStatStringCrudProxy){
		this.crudProxy = userStatCrudProxy;
		this.crudProxy.addProxy(userStatStringCrudProxy);
	}
	
}