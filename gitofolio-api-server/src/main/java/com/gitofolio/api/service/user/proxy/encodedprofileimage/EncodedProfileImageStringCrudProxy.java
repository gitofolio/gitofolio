package com.gitofolio.api.service.user.proxy.encodedprofileimage;

import com.gitofolio.api.domain.user.EncodedProfileImage;
import com.gitofolio.api.service.user.EncodedProfileImageService;
import com.gitofolio.api.service.user.proxy.CrudProxy;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.factory.mapper.UserMapper;
import com.gitofolio.api.domain.user.UserInfo;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Async;

@Service
public class EncodedProfileImageStringCrudProxy implements CrudProxy<EncodedProfileImage>{
	
	private CrudProxy<EncodedProfileImage> crudProxy = null;
	private EncodedProfileImageService encodedProfileImageService;
	private UserMapper<UserInfo> userInfoMapper; 
	
	@Async
	@Override
	public EncodedProfileImage create(Object ...args){
		return this.crudProxy.create(args);
	}
	
	@Override
	public EncodedProfileImage read(Object ...args){
		return this.crudProxy.read(args);
	}
	
	@Override
	public EncodedProfileImage update(Object ...args){
		return this.crudProxy.update(args);
	}

	@Override
	public void delete(Object ...args){
		if(args.length==1 && args[0].getClass().equals(String.class)) this.encodedProfileImageService.delete((String)args[0]);
		else this.crudProxy.delete(args);
	}
	
	@Override
	public void addProxy(CrudProxy<EncodedProfileImage> crudProxy){
		this.crudProxy = crudProxy;
	}
	
	@Autowired
	public EncodedProfileImageStringCrudProxy(EncodedProfileImageService encodedProfileImageService,
									   @Qualifier("userInfoMapper") UserMapper userInfoMapper){
		this.encodedProfileImageService = encodedProfileImageService;
		this.userInfoMapper = userInfoMapper;
	}
	
}