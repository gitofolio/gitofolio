package com.gitofolio.api.service.proxy.personalaccesstoken;

import com.gitofolio.api.domain.auth.PersonalAccessToken;
import com.gitofolio.api.service.proxy.CrudProxy;
import com.gitofolio.api.service.auth.PersonalAccessTokenService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class PersonalAccessTokenStringCrudProxy implements CrudProxy<PersonalAccessToken>{
	
	private CrudProxy<PersonalAccessToken> crudProxy = null;
	private final PersonalAccessTokenService personalAccessTokenService;
	
	@Override
	public PersonalAccessToken create(Object ...args){
		return this.crudProxy.create(args);
	}
	
	@Override
	public PersonalAccessToken read(Object ...args){
		if(args.length==1 && args[0].getClass().equals(String.class)) return this.personalAccessTokenService.get((String)args[0]);
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
	public PersonalAccessTokenStringCrudProxy(PersonalAccessTokenService personalAccessTokenService){
		this.personalAccessTokenService = personalAccessTokenService;
	}
	
}