package com.gitofolio.api.service.auth.oauth.applications.github;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;

import com.gitofolio.api.service.auth.exception.AuthenticateException;
import com.gitofolio.api.service.auth.oauth.OauthTokenReceiver;

@Service
public class GithubTokenReceiver implements OauthTokenReceiver<String, String>{
	
	private GithubClientSecret githubClientSecret;
	private RestTemplate restTemplate;
	private String url = "https://github.com/login/oauth/access_token";
	private Object mockObject = new Object();
	
	@Override
	public String receiveToken(String code){
		GithubTokenDTO result = null;
		try{
			result = this.restTemplate.postForObject(this.url, getHttpEntity(code), GithubTokenDTO.class, mockObject);
		}catch(Exception e){
			throw new AuthenticateException("깃허브 인증 오류","깃허브에서 유저 토큰을 가져올 수 없습니다. 올바른 유저인지 확인해주세요");
		}
		return result.getToken();
	}
	
	private HttpEntity<MultiValueMap<String, String>> getHttpEntity(String code) throws Exception{
		HttpHeaders httpHeaders = getHttpHeaders();
		MultiValueMap<String, String> httpBody = getHttpBody(code);
		return new HttpEntity<MultiValueMap<String, String>>(httpBody, httpHeaders);
	}
	
	private HttpHeaders getHttpHeaders() throws Exception{
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Accept", "application/json");
		return httpHeaders;
	}
	
	private MultiValueMap<String, String> getHttpBody(String code) throws Exception{
		MultiValueMap<String, String> httpBody = new LinkedMultiValueMap<String, String>();
		httpBody.add("client_id", this.githubClientSecret.getId());
		httpBody.add("client_secret", this.githubClientSecret.getSecret());
		httpBody.add("code", code);
		
		return httpBody;
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