package com.gitofolio.api.service.user.factory.crudproxy;

import com.gitofolio.api.domain.user.EncodedProfileImage;
import com.gitofolio.api.service.user.proxy.CrudProxy;
import com.gitofolio.api.service.user.factory.CrudFactory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class EncodedProfileImageCrudFactory implements CrudFactory<EncodedProfileImage>{
	
	private final CrudProxy<EncodedProfileImage> crudProxy;
	
	@Override
	public CrudProxy<EncodedProfileImage> get(){
		return this.crudProxy;
	}
	
	@Autowired
	public EncodedProfileImageCrudFactory(@Qualifier("encodedProfileImageCrudProxy") CrudProxy<EncodedProfileImage> encodedProfileImageCrudProxy,
										 @Qualifier("encodedProfileImageStringCrudProxy") CrudProxy<EncodedProfileImage> encodedProfileImageStringCrudProxy){
		this.crudProxy = encodedProfileImageCrudProxy;
		this.crudProxy.addProxy(encodedProfileImageStringCrudProxy);
	}
	
}