package com.gitofolio.api.service.auth.oauth;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.auth.oauth.applications.OauthApplicationCapsule;
import com.gitofolio.api.service.auth.exception.AuthenticateException;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.*;

@Service
public class AuthenticatorImpl implements Authenticator{
	
	private RestTemplate restTemplate;
	private TokenReceiver tokenReceiver;
	private ObjectMapper objectMapper;
	
	@Override
	public UserDTO authenticate(String code, OauthApplicationCapsule capsule){
		String token = this.tokenReceiver.receiveToken(code, capsule);
		try{
			JsonNode jsonNode = this.objectMapper.readTree(
				this.restTemplate.exchange(capsule.authenticateUri(), 
										   capsule.authenticateMethod(), 
										   capsule.authenticateHttpEntity(token),
										   String.class).getBody()
			);
			return this.buildLoginedUserDTO(jsonNode, capsule);
		}catch(Exception e){
			throw new AuthenticateException("OAuth application 인증 오류", "OAuth application에서 유저정보를 가져올수 없습니다. 올바른 요청인지 확인하세요.");
		}
	}
	
	private UserDTO buildLoginedUserDTO(JsonNode jsonNode, OauthApplicationCapsule capsule) throws Exception{
		return new UserDTO.Builder()
			.name(jsonNode.findValue(capsule.userNameFieldName()).asText())
			.profileUrl(jsonNode.findValue(capsule.profileUriFieldName()).asText())
			.build();
	}
	
	@Autowired
	public AuthenticatorImpl(RestTemplate restTemplate,
							 @Qualifier("prettyObjectMapper") ObjectMapper objectMapper,
							 TokenReceiver tokenReceiver){
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
		this.tokenReceiver = tokenReceiver;
	}
	
}