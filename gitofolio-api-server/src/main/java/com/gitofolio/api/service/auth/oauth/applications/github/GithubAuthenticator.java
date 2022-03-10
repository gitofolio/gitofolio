package com.gitofolio.api.service.auth.oauth.applications.github;

import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.auth.oauth.OauthTokenReceiver;
import com.gitofolio.api.service.auth.exception.AuthenticateException;
import com.gitofolio.api.service.auth.oauth.Authenticator;

import java.util.HashMap;

@Service
public class GithubAuthenticator implements Authenticator<UserDTO, String>{
	
	private RestTemplate restTemplate;
	private OauthTokenReceiver<String, String> githubTokenReceiver;
	private String url = "https://api.github.com/user";
	
	@Override
	public UserDTO authenticate(String parameter){
		HttpHeaders headers = this.getHttpHeaders(parameter);
		HttpEntity httpEntity = new HttpEntity<>(headers);
		
		HashMap<String, Object> response = null;
		try{
			response = this.restTemplate.exchange(this.url, HttpMethod.GET, httpEntity, HashMap.class).getBody();
		}catch(Exception e){
			throw new AuthenticateException("깃허브 인증 오류","토큰에 해당하는 유저를 찾을 수 없습니다.");
		}
		
		return this.buildLoginedUser(response);
	}
	
	private HttpHeaders getHttpHeaders(String parameter){
		String token = this.githubTokenReceiver.receiveToken(parameter);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		headers.add("Authorization", "token " + token);
		
		return headers;
	}
	
	private UserDTO buildLoginedUser(HashMap<String, Object> response){
		return new UserDTO.Builder()
			.name((String)response.get("login"))
			.profileUrl((String)response.get("avatar_url"))
			.build();
	}
	
	@Autowired
	public GithubAuthenticator(RestTemplate restTemplate,
							  @Qualifier("githubTokenReceiver") OauthTokenReceiver githubTokenReceiver){
		this.githubTokenReceiver = githubTokenReceiver;
		this.restTemplate = restTemplate;
	}
	
}