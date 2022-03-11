package com.gitofolio.api.service.auth.oauth.applications.kakao;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.auth.exception.AuthenticateException;
import com.gitofolio.api.service.auth.oauth.*;

import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;

import com.fasterxml.jackson.databind.*;

@Service
public class KakaoAuthenticator implements Authenticator<UserDTO, String>{
	
	private final RestTemplate restTemplate;
	private final OauthTokenReceiver<String, String> kakaoTokenReceiver;
	private final String url;
	private final ObjectMapper objectMapper;
	
	@Override
	public UserDTO authenticate(String code){
		HttpEntity httpEntity = this.getHttpEntity(code);
		JsonNode response = null;
		try{
			response = this.objectMapper.readTree(this.restTemplate.exchange(this.url, HttpMethod.GET, httpEntity, String.class).getBody());
		}catch(Exception e){
			throw new AuthenticateException("카카오 인증 오류", "토큰에 해당하는 유저를 찾을 수 없습니다.");
		}
		return this.buildUserDTO(response);
	}
	
	private HttpEntity getHttpEntity(String code){
		HttpHeaders httpHeaders = this.getHttpHeaders(code);
		return new HttpEntity<>(httpHeaders);
	}
	
	private HttpHeaders getHttpHeaders(String code){
		String token = this.kakaoTokenReceiver.receiveToken(code);
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		httpHeaders.add("Authorization", "Bearer " + token);
		
		return httpHeaders;
	}
	
	private UserDTO buildUserDTO(JsonNode jsonNode){
		JsonNode properties = jsonNode.get("properties");
		String name = properties.get("nickname").asText();
		String profileUrl = properties.get("profile_image").asText();
		return new UserDTO.Builder()
			.name(name)
			.profileUrl(profileUrl)
			.build();
	}
	
	@Autowired
	public KakaoAuthenticator(RestTemplate restTemplate,
							  @Qualifier("kakaoTokenReceiver") OauthTokenReceiver<String, String> kakaoTokenReceiver,
							  ObjectMapper objectMapper){
		this.restTemplate = restTemplate;
		this.kakaoTokenReceiver = kakaoTokenReceiver;
		this.url = "https://kapi.kakao.com/v2/user/me?secure_resource=true&property_keys=[\"properties.nickname\", \"properties.profile_image\"]";
		this.objectMapper = objectMapper;
	}
	
	private static class KakaoUserDTO{
		
	}
	
}