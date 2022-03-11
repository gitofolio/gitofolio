package com.gitofolio.api.service.auth.oauth.applications.kakao;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.*;

import com.gitofolio.api.service.auth.oauth.OauthTokenReceiver;
import com.gitofolio.api.service.auth.exception.AuthenticateException;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.*;

@Service
public class KakaoTokenReceiver implements OauthTokenReceiver<String, String>{
	
	private final KakaoClientSecret kakaoClientSecret;
	private final RestTemplate restTemplate;
	private final String url;
	private final Object mockObject;
	private final ObjectMapper objectMapper;
	
	@Override
	public String receiveToken(String code){
		JsonNode result = null;
		try{
			result = this.objectMapper.readTree(this.restTemplate.postForObject(this.url, this.getHttpEntity(code), String.class, this.mockObject));
		}catch(Exception e){
			throw new AuthenticateException("카카오 인증 오류", "카카오에서 유저 토큰을 가져올 수 없습니다. 올바른 유저인지 확인해주세요");
		}
		return result.get("access_token").asText();
	}
	
	private HttpEntity<MultiValueMap<String, String>> getHttpEntity(String code) throws Exception{
		HttpHeaders httpHeaders = this.getHttpHeaders();
		MultiValueMap<String, String> httpBody = this.getHttpBody(code);
		return new HttpEntity<MultiValueMap<String, String>>(httpBody, httpHeaders);
	}
	
	private HttpHeaders getHttpHeaders() throws Exception{
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		return httpHeaders;
	}
	
	private MultiValueMap<String, String> getHttpBody(String code){
		MultiValueMap<String, String> httpBody = new LinkedMultiValueMap<String, String>();
		httpBody.add("grant_type", "authorization_code");
		httpBody.add("client_id", this.kakaoClientSecret.getId());
		httpBody.add("redirect_uri", "https://api-server-gitofolio-qfnxv.run.goorm.io/oauth/kakao");
		httpBody.add("code", code);
		httpBody.add("client_secret", this.kakaoClientSecret.getSecret());
		return httpBody;
	}
	
	@Autowired
	public KakaoTokenReceiver(KakaoClientSecret kakaoClientSecret,
							 RestTemplate restTemplate,
							 ObjectMapper objectMapper){
		this.kakaoClientSecret = kakaoClientSecret;
		this.restTemplate = restTemplate;
		this.url = "https://kauth.kakao.com/oauth/token";
		this.mockObject = new Object();
		this.objectMapper = objectMapper;
	}
	
}