package com.gitofolio.api.service.proxy.userinfo;

import com.gitofolio.api.service.factory.mapper.UserMapper;
import com.gitofolio.api.service.proxy.CrudProxy;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.UserInfoService;
import com.gitofolio.api.domain.user.UserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserInfoStringCrudProxy implements CrudProxy<UserDTO>{
	
	private CrudProxy<UserDTO> crudProxy = null;
	private final UserInfoService userInfoService;
	private final UserMapper<UserInfo> userInfoMapper;
	
	@Override
	public UserDTO create(Object ...args){
		return this.crudProxy.create(args);
	}
	
	@Override
	public UserDTO read(Object ...args){
		if(args.length==1 && args[0].getClass().equals(String.class)){
			return this.userInfoMapper.doMap(
				userInfoService.get((String)args[0])
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
		if(args.length==1 && args[0].getClass().equals(String.class)) userInfoService.delete((String)args[0]);
		else this.crudProxy.delete(args);
	}
	
	@Override
	public void addProxy(CrudProxy<UserDTO> crudProxy){
		if(this.crudProxy == null) this.crudProxy = crudProxy;
		else this.crudProxy.addProxy(crudProxy);
	}
	
	@Autowired
	public UserInfoStringCrudProxy(UserInfoService userInfoService,
								  @Qualifier("userInfoMapper") UserMapper<UserInfo> userInfoMapper){
		this.userInfoService = userInfoService;
		this.userInfoMapper = userInfoMapper;
	}
	
}