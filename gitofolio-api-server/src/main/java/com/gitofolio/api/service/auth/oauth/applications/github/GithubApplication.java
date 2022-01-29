package com.gitofolio.api.service.auth.oauth.applications.github;

import com.gitofolio.api.service.auth.oauth.applications.OauthApplication;
import com.gitofolio.api.service.auth.authenticate.Authenticator;
import com.gitofolio.api.service.user.dtos.UserDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class GithubApplication implements OauthApplication{
	
	private final String name;
	private final String url;
	private final Authenticator<UserDTO, String> githubAuthenticator;
	
	@Override
	public String getUrl(){
		return this.url;
	}
	
	@Override
	public String getUrlWithQueryString(String queryString){
		return getRealUrl()+queryString;
		// return getTestUrl()+queryString;
	}
	
	private String getRealUrl(){
		return this.url+"&redirect_uri=https://api.gitofolio.com/oauth/github";
	}
	
	private String getTestUrl(){
		return this.url+"&redirect_uri=https://api-server-gitofolio-qfnxv.run.goorm.io/oauth/github";
	}
	
	@Override
	public String getName(){
		return this.name;
	}
	
	@Override
	public Authenticator getAuthenticator(){
		return this.githubAuthenticator;
	}
	
	@Autowired
	public GithubApplication(@Qualifier("githubAuthenticator") Authenticator<UserDTO, String> githubAuthenticator){
		this.githubAuthenticator = githubAuthenticator;
		this.name = "github";
		this.url = "https://github.com/login/oauth/authorize?client_id=b86796ef36e991c00490";
	}
	
}