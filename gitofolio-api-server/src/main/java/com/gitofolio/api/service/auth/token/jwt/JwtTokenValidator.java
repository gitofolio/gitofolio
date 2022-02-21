package com.gitofolio.api.service.auth.token.jwt;

import com.gitofolio.api.service.auth.token.*;
import com.gitofolio.api.service.auth.exception.AuthenticateException;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import io.jsonwebtoken.*;

import javax.servlet.http.HttpServletRequest;

@Service
public class JwtTokenValidator implements TokenValidator{
	
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@Autowired
	private JwtSecret jwtSecret;
	
	@Override
	public String currentLogined(){
		return this.parseToken();
	}
	
	@Override
	public boolean validateToken(String validateTarget){
		if(!this.validateTokenRealTask(this.parseToken(), validateTarget)) 
			throw new AuthenticateException("JWT토큰인증 오류", "접근할 수 없는 문서입니다.");
		return true;
	}
	
	@Override
	public boolean validateToken(TokenAble validateTarget){
		if(!this.validateTokenRealTask(this.parseToken(), validateTarget.token())) 
			throw new AuthenticateException("JWT토큰인증 오류", "접근할 수 없는 문서입니다.");
		return true;
	}
	
	private boolean validateTokenRealTask(String actual, String expected){
		if(!expected.equals(actual)) return false;
		return true;
	}
	
	private String parseToken(){
		try{
			return parseTokenRealTask();
		}catch(IncorrectClaimException ICE){
			throw new AuthenticateException("유효하지 않은 토큰입니다.", "토큰이 포함하고있는 정보가 유효하지 않습니다.");
		}catch(MissingClaimException MCE){
			throw new AuthenticateException("유효하지 않은 토큰입니다.", "토큰에 제외된 구문이 있습니다.");
		}catch(SignatureException SE){
			throw new AuthenticateException("유효하지 않은 토큰입니다.", "토큰의 서명값이 올바르지 않습니다.");
		}catch(ExpiredJwtException EJE){
			throw new AuthenticateException("유효하지 않은 토큰입니다.", "만료(Time out)된 토큰입니다.");
		}
	}
	
	private String parseTokenRealTask() throws IncorrectClaimException, MissingClaimException, SignatureException, ExpiredJwtException{
		String token = extractTokenInHeader();
		return Jwts.parser()
			.setSigningKey(this.jwtSecret.getSecretKey())
			.requireIssuer(this.jwtSecret.getIssuer())
			.parseClaimsJws(token)
			.getBody()
			.get(this.jwtSecret.getId(), String.class);
	}
	
	private String extractTokenInHeader(){
		String authorization = this.httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
		if(authorization == null || !authorization.startsWith("Bearer ")) throw new UnsupportedJwtException("토큰이 존재하지않거나, 토큰 인증 타입이 Bearer로 시작하지 않습니다.");
		return authorization.substring("Bearer ".length());
	}
	
}