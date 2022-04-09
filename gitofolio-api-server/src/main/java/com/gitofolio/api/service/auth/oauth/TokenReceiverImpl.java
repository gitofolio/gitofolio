package com.gitofolio.api.service.auth.oauth;

import com.gitofolio.api.service.auth.oauth.applications.OauthApplicationCapsule;
import com.gitofolio.api.service.auth.exception.AuthenticateException;

import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.*;

@Service
public class TokenReceiverImpl implements TokenReceiver{
	
	private RestTemplate restTemplate;
	private ObjectMapper objectMapper;
	
	@Override
	public String receiveToken(String code, OauthApplicationCapsule capsule){
		try{
			JsonNode jsonNode = this.objectMapper.readTree(
				this.restTemplate.exchange(capsule.tokenUri(), capsule.tokenHttpMethod(), capsule.tokenHttpEntity(code), String.class).getBody()
			);
			return this.findToken(jsonNode, capsule);
		}catch(Exception e){
			throw new AuthenticateException("OAuth application 인증 오류", "OAuth application에서 토큰을 받아올수 없습니다. 올바른 유저인지 확인하세요.");
		}
	}
	
	private String findToken(JsonNode jsonNode, OauthApplicationCapsule capsule) throws Exception{
		return jsonNode.findValue(capsule.tokenFieldName()).asText();
	}
	
	@Autowired
	public TokenReceiverImpl(RestTemplate restTemplate,
							@Qualifier("prettyObjectMapper") ObjectMapper objectMapper){
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
	}
	
}