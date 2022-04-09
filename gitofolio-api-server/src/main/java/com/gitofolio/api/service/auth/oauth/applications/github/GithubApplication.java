package com.gitofolio.api.service.auth.oauth.applications.github;

import com.gitofolio.api.service.auth.oauth.applications.*;
import com.gitofolio.api.service.auth.oauth.Authenticator;
import com.gitofolio.api.service.user.dtos.UserDTO;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

@Component
public class GithubApplication implements OauthApplication{
	
	private final String name;
	private final String url;
	private final String testUrl;
	private final OauthApplicationCapsule githubCapsule;
	
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
		return this.url;
	}
	
	private String getTestUrl(){
		return this.testUrl;
	}
	
	@Override
	public String getName(){
		return this.name;
	}
	
	@Override
	public OauthApplicationCapsule getOauthApplicationCapsule(){
		return this.githubCapsule;
	}
	
	@Autowired
	public GithubApplication(@Qualifier("githubOauthApplicationCapsule") OauthApplicationCapsule githubCapsule){
		this.githubCapsule = githubCapsule;
		this.name = "github";
		this.url = "https://github.com/login/oauth/authorize?client_id=b86796ef36e991c00490&redirect_uri=https://api.gitofolio.com/oauth/github";
		this.testUrl = "https://github.com/login/oauth/authorize?client_id=b86796ef36e991c00490&redirect_uri=https://api-server-gitofolio-qfnxv.run.goorm.io/oauth/github";
	}
	
}