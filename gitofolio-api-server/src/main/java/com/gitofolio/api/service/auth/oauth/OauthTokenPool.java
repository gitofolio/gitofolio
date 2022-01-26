package com.gitofolio.api.service.auth.oauth;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Async;

import com.gitofolio.api.service.auth.exception.AuthenticateException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;

import javax.crypto.KeyGenerator;

import java.security.NoSuchAlgorithmException;

@Service
public class OauthTokenPool{
	
	private final Map<String, Token> tokenPool;
	
	public OauthTokenPool(){
		this.tokenPool = new ConcurrentHashMap<String, Token>();
	}
	
	public void saveToken(String cert, String personalAccessTokenValue, String token){
		this.tokenPool.put(cert, new Token(personalAccessTokenValue, token));
	}
	
	public String getToken(String cert, String personalAccessTokenValue){
		Token ans = null;
		try{
			ans = this.tokenPool.get(cert);
			this.validatePersonalAccessTokenValue(ans.personalAccessTokenValue, personalAccessTokenValue);
		}catch(NullPointerException NPE){
			throw new AuthenticateException("cert에 해당하는 토큰을 찾을 수 없습니다.", "cert값을 확인해주거나, cert 유효시간(5분)이 지났는지 확인해주세요");
		}catch(AuthenticateException AUE){
			throw AUE;
		}
		return ans.token;
	} 
	
	public void deleteToken(String cert){
		try{
			this.tokenPool.remove(cert);
		}catch(NullPointerException NPE){
			throw new AuthenticateException("cert에 해당하는 토큰을 찾을 수 없습니다.", "cert값을 확인해주거나, cert 유효시간(5분)이 지났는지 확인해주세요");
		}
	}
	
	@Async
	@Scheduled(fixedRate=30000)
	private void deleteOldToken(){
		for(Map.Entry<String, Token> token : this.tokenPool.entrySet())
			if(token.getValue().isOldToken()) tokenPool.remove(token.getKey());
	}
	
	private boolean validatePersonalAccessTokenValue(String actual, String expected) throws AuthenticateException{
		if(actual.equals(expected)) return true;
		else throw new AuthenticateException("personal access token 오류", "유효하지않은 personal access token입니다.");
	}
	
	private static class Token{
		
		private final String token;
		private final String personalAccessTokenValue;
		private final LocalDateTime generatedTime; 
		
		private Token(String personalAccessTokenValue, String token){
			this.personalAccessTokenValue = personalAccessTokenValue;
			this.token = token;
			this.generatedTime = LocalDateTime.now();
		}
		
		private boolean isOldToken(){
			return this.generatedTime.isBefore(LocalDateTime.now().minusMinutes(5));
		}
		
	}
	
}