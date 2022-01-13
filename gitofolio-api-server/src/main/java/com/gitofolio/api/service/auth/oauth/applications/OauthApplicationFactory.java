package com.gitofolio.api.service.auth.oauth.applications;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.gitofolio.api.service.user.exception.IllegalParameterException;

import java.util.List;

@Service
public class OauthApplicationFactory{
	
	private final List<OauthApplication> oauthApplicaitons;
	
	public OauthApplication get(String applicationName){
		for(OauthApplication oauthApplicaiton : oauthApplicaitons){
			if(oauthApplicaiton.getName().equals(applicationName)) return oauthApplicaiton;
		}
		throw new IllegalParameterException("oauth 애플리케이션 선택 오류", "파라미터 " + applicationName + " 에 맞는 oauth애플리케이션을 찾을 수 없습니다.");
	}
	
	@Autowired
	public OauthApplicationFactory(List<OauthApplication> oauthApplicaitons){
		this.oauthApplicaitons = oauthApplicaitons;
	}
	
}