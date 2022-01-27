package com.gitofolio.api.service.proxy.personalaccesstoken;

import com.gitofolio.api.domain.auth.PersonalAccessToken;
import com.gitofolio.api.domain.user.UserInfo;
import com.gitofolio.api.service.proxy.CrudProxy;
import com.gitofolio.api.service.auth.PersonalAccessTokenService;
import com.gitofolio.api.service.factory.mapper.UserMapper;
import com.gitofolio.api.service.user.dtos.UserDTO;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class PersonalAccessTokenUserDTOCrudProxy implements CrudProxy<PersonalAccessToken>{
	
	private CrudProxy<PersonalAccessToken> crudProxy = null;
	private final PersonalAccessTokenService personalAccessTokenService;
	private final UserMapper<UserInfo> userInfoMapper;
	
	@Override
	public PersonalAccessToken create(Object ...args){
		if(args.length==1 && args[0].getClass().equals(UserDTO.class)) {
			return this.personalAccessTokenService.save(
				this.userInfoMapper.resolveMap((UserDTO)args[0])
			);
		}
		return this.crudProxy.create(args);
	}
	
	@Override
	public PersonalAccessToken read(Object ...args){
		return this.crudProxy.read(args);
	}
	
	@Override
	public PersonalAccessToken update(Object ...args){
		return this.crudProxy.update(args);
	}
	
	@Override
	public void delete(Object ...args){
		this.crudProxy.delete(args);
	}
	
	@Override
	public void addProxy(CrudProxy<PersonalAccessToken> crudProxy){
		if(this.crudProxy!= null) this.crudProxy.addProxy(crudProxy);
		else this.crudProxy = crudProxy;
	}
	
	@Autowired
	public PersonalAccessTokenUserDTOCrudProxy(PersonalAccessTokenService personalAccessTokenService,
										   @Qualifier("userInfoMapper") UserMapper<UserInfo> userInfoMapper){
		this.personalAccessTokenService = personalAccessTokenService;
		this.userInfoMapper = userInfoMapper;
	}
	
}