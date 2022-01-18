package com.gitofolio.api.service.proxy.encodedprofileimage;

import com.gitofolio.api.domain.user.EncodedProfileImage;
import com.gitofolio.api.service.user.EncodedProfileImageService;
import com.gitofolio.api.service.proxy.CrudProxy;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.factory.mapper.UserMapper;
import com.gitofolio.api.domain.user.UserInfo;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Async;

@Service
public class EncodedProfileImageCrudProxy implements CrudProxy<EncodedProfileImage>{
	
	private CrudProxy<EncodedProfileImage> crudProxy = null;
	private EncodedProfileImageService encodedProfileImageService;
	private UserMapper<UserInfo> userInfoMapper; 
	
	@Async
	@Override
	@Transactional
	public EncodedProfileImage create(Object ...args){
		if(args.length==1 && args[0].getClass().equals(UserDTO.class)){
			return this.encodedProfileImageService.save(
				this.userInfoMapper.resolveMap(
					(UserDTO)args[0]
				)
			);
		}
		return this.crudProxy.create(args);
	}
	
	@Override
	@Transactional // get에 업데이트 로직이 포함되어 있음! 나중에 읽었을때 readOnly로 수정하지말것 
	public EncodedProfileImage read(Object ...args){
		if(args.length==1 && args[0].getClass().equals(UserDTO.class)){
			return this.encodedProfileImageService.get(
				this.userInfoMapper.resolveMap(
					(UserDTO)args[0]
				)
			);
		}
		return this.crudProxy.read(args);
	}
	
	@Override
	@Transactional
	public EncodedProfileImage update(Object ...args){
		return this.crudProxy.update(args);
	}

	@Override
	@Transactional
	public void delete(Object ...args){
		this.crudProxy.delete(args);
	}
	
	@Override
	public void addProxy(CrudProxy<EncodedProfileImage> crudProxy){
		this.crudProxy = crudProxy;
	}
	
	@Autowired
	public EncodedProfileImageCrudProxy(EncodedProfileImageService encodedProfileImageService,
									   @Qualifier("userInfoMapper") UserMapper userInfoMapper){
		this.encodedProfileImageService = encodedProfileImageService;
		this.userInfoMapper = userInfoMapper;
	}
	
}