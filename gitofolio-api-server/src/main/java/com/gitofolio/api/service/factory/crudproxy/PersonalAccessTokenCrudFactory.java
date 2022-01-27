package com.gitofolio.api.service.factory.crudproxy;

import com.gitofolio.api.domain.auth.PersonalAccessToken;
import com.gitofolio.api.service.factory.CrudFactory;
import com.gitofolio.api.service.proxy.CrudProxy;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class PersonalAccessTokenCrudFactory implements CrudFactory<PersonalAccessToken>{
	
	private final CrudProxy<PersonalAccessToken> crudProxy;
	
	@Override
	public CrudProxy<PersonalAccessToken> get(){
		return this.crudProxy;
	}
	
	@Autowired
	public PersonalAccessTokenCrudFactory(@Qualifier("personalAccessTokenCrudProxy") CrudProxy<PersonalAccessToken> personalAccessTokenCrudProxy,
										  @Qualifier("personalAccessTokenLongCrudProxy") CrudProxy<PersonalAccessToken> personalAccessTokenLongCrudProxy,
										  @Qualifier("personalAccessTokenUserDTOCrudProxy") CrudProxy<PersonalAccessToken> personalAccessTokenUserDTOCrudProxy,
										  @Qualifier("personalAccessTokenStringCrudProxy") CrudProxy<PersonalAccessToken> personalAccessTokenStringCrudProxy){
		this.crudProxy = personalAccessTokenCrudProxy;
		this.crudProxy.addProxy(personalAccessTokenLongCrudProxy);
		this.crudProxy.addProxy(personalAccessTokenUserDTOCrudProxy);
		this.crudProxy.addProxy(personalAccessTokenStringCrudProxy);
	}
	
}