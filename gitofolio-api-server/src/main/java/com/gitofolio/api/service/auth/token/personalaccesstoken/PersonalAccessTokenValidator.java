package com.gitofolio.api.service.auth.token.personalaccesstoken;

import com.gitofolio.api.service.auth.token.*;
import com.gitofolio.api.service.auth.exception.*;
import com.gitofolio.api.service.factory.CrudFactory;
import com.gitofolio.api.service.proxy.CrudProxy;
import com.gitofolio.api.domain.auth.PersonalAccessToken;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletRequest;

@Service
public class PersonalAccessTokenValidator implements TokenValidator{
	
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@Autowired
	@Qualifier("personalAccessTokenCrudFactory")
	private CrudFactory<PersonalAccessToken> personalAccessTokenCrudFactory;
	
	@Override
	public String currentLogined(){
		CrudProxy<PersonalAccessToken> crudProxy = this.personalAccessTokenCrudFactory.get();
		return crudProxy.read(extractTokenInHeader()).token();
	}
	
	@Override
	public boolean validateToken(String validateTarget){
		CrudProxy<PersonalAccessToken> crudProxy = this.personalAccessTokenCrudFactory.get();
		String actualTarget = crudProxy.read(extractTokenInHeader()).token();
		return validateTokenRealTask(actualTarget, validateTarget);
	}
	
	@Override
	public boolean validateToken(TokenAble validateTarget){
		CrudProxy<PersonalAccessToken> crudProxy = this.personalAccessTokenCrudFactory.get();
		String actualTarget = crudProxy.read(extractTokenInHeader()).token();
		return validateTokenRealTask(actualTarget, validateTarget.token());
	}
	
	private boolean validateTokenRealTask(String actual, String expected){
		if(!actual.equals(expected)) return false;
		return true;
	}
	
	private String extractTokenInHeader(){
		String authorization = this.httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
		if(authorization == null || !authorization.startsWith("Pat ")) throw new AuthenticateException("인증 에러", "토큰이 존재하지않거나, 토큰 인증 타입이 Pat로 시작하지 않습니다.");
		return authorization.substring("Pat ".length());
	}
	
}