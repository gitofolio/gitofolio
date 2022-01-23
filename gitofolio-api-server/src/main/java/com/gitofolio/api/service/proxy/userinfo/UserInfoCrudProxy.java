package com.gitofolio.api.service.proxy.userinfo;

import com.gitofolio.api.service.factory.mapper.UserMapper;
import com.gitofolio.api.service.proxy.CrudProxy;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.domain.user.UserInfo;
import com.gitofolio.api.service.user.UserInfoService;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserInfoCrudProxy implements CrudProxy<UserDTO>{
	
	private CrudProxy<UserDTO> crudProxy;
	private final UserInfoService userInfoService;
	private final UserMapper<UserInfo> userInfoMapper;
	
	@Override
	@Transactional
	public UserDTO create(Object ...args){
		if(args.length==1 && args[0].getClass().equals(UserDTO.class)){
			return this.userInfoMapper.doMap(
				userInfoService.save(
					this.userInfoMapper.resolveMap(
						(UserDTO)args[0]
					)
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
		if(args.length==1 && args[0].getClass().equals(UserDTO.class)) {
			return this.userInfoMapper.doMap(
				userInfoService.edit(
					this.userInfoMapper.resolveMap(
						(UserDTO)args[0]
					)
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
	
	@Override
	public void addProxy(CrudProxy<UserDTO> crudProxy){
		if(this.crudProxy == null) this.crudProxy = crudProxy;
		else this.crudProxy.addProxy(crudProxy);
	}
	
	@Autowired
	public UserInfoCrudProxy(UserInfoService userInfoService,
							 @Qualifier("userInfoMapper") UserMapper<UserInfo> userInfoMapper){
		this.userInfoService = userInfoService;
		this.userInfoMapper = userInfoMapper;
	}
	
}