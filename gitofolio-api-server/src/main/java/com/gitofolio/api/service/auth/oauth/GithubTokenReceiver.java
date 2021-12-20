package com.gitofolio.api.service.auth.oauth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;

@Service
public class GithubTokenReceiver implements TokenReceiver<String, String>{
	
	private GithubClientSecret githubClientSecret;
	private RestTemplate restTemplate;
	private String url = "https://github.com/login/oauth/access_token";
	private Object mockObject = new Object();
	
	@Override
	public String receiveToken(String code){
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Accept", "application/json");
		
		MultiValueMap<String, String> httpBody = new LinkedMultiValueMap<String, String>();
		httpBody.add("client_id", this.githubClientSecret.getId());
		httpBody.add("client_secret", this.githubClientSecret.getSecret());
		httpBody.add("code", code);
		
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(httpBody, httpHeaders);
		
		GithubTokenDTO result = this.restTemplate.postForObject(this.url, httpEntity, GithubTokenDTO.class, mockObject);
		return result.getToken();
	}
	
	@Autowired
	public GithubTokenReceiver(GithubClientSecret githubClientSecret,
							  RestTemplate restTemplate){
		this.githubClientSecret = githubClientSecret;
		this.restTemplate = restTemplate;
	}
	
	private static class GithubTokenDTO{
		
		private String access_token;
		private String scope;
		private String token_type;
		
		public void setAccess_token(String access_token){
			this.access_token = access_token;
		}
		
		public void setScope(String scope){
			this.scope = scope;
		}
		
		public void setToken_type(String token_type){
			this.token_type = token_type;
		}
		
		public String getToken(){
			return this.access_token;
		}
		
	}
	
}