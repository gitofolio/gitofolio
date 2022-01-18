package com.gitofolio.api.service.factory.crudproxy;

import com.gitofolio.api.service.factory.CrudFactory;
import com.gitofolio.api.service.proxy.CrudProxy;
import com.gitofolio.api.service.user.dtos.UserDTO;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class UserInfoCrudFactory implements CrudFactory<UserDTO>{
	
	private final CrudProxy<UserDTO> crudProxy;
	
	@Override
	public CrudProxy<UserDTO> get(){
		return this.crudProxy;
	}
	
	@Autowired
	public UserInfoCrudFactory(@Qualifier("userInfoCrudProxy") CrudProxy<UserDTO> userInfoCrudProxy,
							  @Qualifier("userInfoStringCrudProxy") CrudProxy<UserDTO> userInfoStringCrudProxy){
		this.crudProxy = userInfoCrudProxy;
		this.crudProxy.addProxy(userInfoStringCrudProxy);
	}
	
}