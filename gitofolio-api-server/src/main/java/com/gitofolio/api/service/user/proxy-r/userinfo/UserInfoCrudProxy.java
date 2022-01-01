package com.gitofolio.api.service.user.proxy.userinfo;

import com.gitofolio.api.service.user.proxy.CrudProxy;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.domain.user.UserInfo;
import com.gitofolio.api.service.user.UserInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class UserInfoCrudProxy implements CrudProxy<UserInfo>{
	
	private CrudProxy<UserInfo> userInfoStringCrudProxy;
	private UserInfoService userInfoService;
		
	@Override
	public UserInfo create(Object ...args){
		if(args.length==1 && args[0].getClass().equals(UserInfo.class)) return userInfoService.save((UserInfo)args[0]);
		return this.userInfoStringCrudProxy.create(args);
	}
	
	@Override
	public UserInfo read(Object ...args){
		return this.userInfoStringCrudProxy.read(args);
	}
	
	@Override
	public UserInfo update(Object ...args){
		if(args.length==1 && args[0].getClass().equals(UserInfo.class)) return userInfoService.edit((UserInfo)args[0]);
		return this.userInfoStringCrudProxy.update(args);
	}
	
	@Override
	public void delete(Object ...args){
		this.userInfoStringCrudProxy.delete(args);
	}
	
	@Autowired
	public UserInfoCrudProxy(@Qualifier("userInfoStringCrudProxy") CrudProxy<UserInfo> userInfoStringCrudProxy,
							UserInfoService userInfoService){
		this.userInfoStringCrudProxy = userInfoStringCrudProxy;
		this.userInfoService = userInfoService;
	}
	
}