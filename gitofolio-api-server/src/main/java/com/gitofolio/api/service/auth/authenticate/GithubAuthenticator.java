package com.gitofolio.api.service.auth.authenticate;

import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.auth.oauth.TokenReceiver;

import java.util.HashMap;

@Service
public class GithubAuthenticator implements Authenticator<UserDTO, String>{
	
	private RestTemplate restTemplate;
	private TokenReceiver<String, String> githubTokenReceiver;
	private String url = "https://api.github.com/user";
	
	@Override
	public UserDTO authenticate(String parameter){
		String token = this.githubTokenReceiver.receiveToken(parameter);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		headers.add("Authorization", "token " + token);
		
		HttpEntity httpEntity = new HttpEntity<>(headers);
		
		HashMap<String, Object> response = this.restTemplate.exchange(this.url, HttpMethod.GET, httpEntity, HashMap.class).getBody();
		
		return new UserDTO.Builder()
			.id(new Long((Integer)response.get("id")))
			.name((String)response.get("login"))
			.profileUrl((String)response.get("avatar_url"))
			.build();
	}
	
	@Autowired
	public GithubAuthenticator(RestTemplate restTemplate,
							  @Qualifier("githubTokenReceiver") TokenReceiver githubTokenReceiver){
		this.githubTokenReceiver = githubTokenReceiver;
		this.restTemplate = restTemplate;
	}
	
}