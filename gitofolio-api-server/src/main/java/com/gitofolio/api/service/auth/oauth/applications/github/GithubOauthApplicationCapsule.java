package com.gitofolio.api.service.auth.oauth.applications.github;

import com.gitofolio.api.service.auth.oauth.applications.OauthApplicationCapsule;

import org.springframework.http.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.util.*;

@Service
public class GithubOauthApplicationCapsule implements OauthApplicationCapsule{
	
	private GithubClientSecret githubClientSecret;
	
	@Override
	public String tokenUri(){
		return "https://github.com/login/oauth/access_token";
	}
	
	@Override
	public HttpMethod tokenHttpMethod(){
		return HttpMethod.POST;
	}
	
	@Override
	public HttpEntity tokenHttpEntity(String code){
		HttpHeaders httpHeaders = this.getHttpHeaders();
		MultiValueMap<String, String> httpBody = this.getHttpBody(code);
		return new HttpEntity<MultiValueMap<String, String>>(httpBody, httpHeaders);
	}
	
	private HttpHeaders getHttpHeaders(){
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Accept", "application/json");
		return httpHeaders;
	}
	
	private MultiValueMap<String, String> getHttpBody(String code){
		MultiValueMap<String, String> httpBody = new LinkedMultiValueMap<String, String>();
		httpBody.add("client_id", this.githubClientSecret.getId());
		httpBody.add("client_secret", this.githubClientSecret.getSecret());
		httpBody.add("code", code);
		
		return httpBody;
	}
	
	@Override
	public String tokenFieldName(){
		return "access_token";
	}
	
	@Override
	public String authenticateUri(){
		return "https://api.github.com/user";
	}
	
	@Override
	public HttpMethod authenticateMethod(){
		return HttpMethod.GET;
	}
	
	@Override
	public HttpEntity authenticateHttpEntity(String token){
		HttpHeaders headers = this.getHttpHeaders(token);
		return new HttpEntity<>(headers);
	}
	
	private HttpHeaders getHttpHeaders(String token){
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		headers.add("Authorization", "token " + token);
		
		return headers;
	}
	
	@Override
	public String userNameFieldName(){
		return "login";
	}
	
	@Override
	public String profileUriFieldName(){
		return "avatar_url";
	}
	
	@Autowired
	public GithubOauthApplicationCapsule(GithubClientSecret githubClientSecret){
		this.githubClientSecret = githubClientSecret;
	}
	
}