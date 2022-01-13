package com.gitofolio.api.service.auth.oauth;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import com.gitofolio.api.service.auth.exception.AuthenticateException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;

import javax.crypto.KeyGenerator;

import java.security.NoSuchAlgorithmException;

@Service
public class OauthTokenPool{
	
	private final Map<String, Token> tokenPool; // cert -> token (ex. jwt)
	
	public OauthTokenPool(){
		this.tokenPool = new ConcurrentHashMap<String, Token>();
	}
	
	public void create(String cert, String token){
		this.tokenPool.put(cert, new Token(token));
	}
	
	public String getToken(String cert){
		String ans="";
		try{
			ans = this.tokenPool.get(cert).token;
			this.tokenPool.remove(cert);
		}catch(NullPointerException NPE){
			throw new AuthenticateException("cert에 해당하는 토큰을 찾을 수 없습니다.", "cert값을 확인해주거나, cert 유효시간(5분)이 지났는지 확인해주세요");
		}
		return ans;
	} 
	
	@Scheduled(fixedRate=30000)
	private void deleteOldToken(){
		for(Map.Entry<String, Token> token : this.tokenPool.entrySet()){
			if(token.getValue().isOldToken()){
				tokenPool.remove(token.getKey());
			}
		}
	}
	
	private static class Token{
		
		private final String token;
		private final LocalDateTime generatedTime; 
		
		private Token(String token){
			this.token = token;
			this.generatedTime = LocalDateTime.now();
		}
		
		private boolean isOldToken(){
			return this.generatedTime.isBefore(LocalDateTime.now().minusMinutes(5)); // token 유효기간 5분
		}
		
	}
	
}