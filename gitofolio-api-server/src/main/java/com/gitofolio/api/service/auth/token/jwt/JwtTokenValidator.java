package com.gitofolio.api.service.auth.token.jwt;

import com.gitofolio.api.service.auth.token.TokenValidator;
import com.gitofolio.api.service.auth.token.TokenAble;
import com.gitofolio.api.service.auth.exception.AuthenticateException;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;

@Service
public class JwtTokenValidator implements TokenValidator{
	
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@Autowired
	private JwtSecret jwtSecret;
	
	@Override
	public String currentLogined(){
		String token = extractToken(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION));
		try{
			return Jwts.parser()
				.setSigningKey(this.jwtSecret.getSecretKey())
				.requireIssuer(this.jwtSecret.getIssuer())
				.parseClaimsJws(token)
				.getBody()
				.get(this.jwtSecret.getId(), String.class);
		}catch(IncorrectClaimException ICE){
			throw new IncorrectClaimException(ICE.getHeader(), ICE.getClaims(), "변조된 토큰이거나 기간 만료된 토큰입니다.");
		}catch(MissingClaimException MCE){
			throw new MissingClaimException(MCE.getHeader(), MCE.getClaims(), "사용할수없는 토큰입니다.");
		}catch(SignatureException SE){
			throw new SignatureException("유효하지 않은 sign을 갖고있는 토큰입니다.");
		}
	}
	
	@Override
	public boolean validateToken(String target){
		return getValue(target);
	}
	
	@Override
	public boolean validateToken(TokenAble target){
		return getValue(target.token());
	}
	
	private boolean getValue(String target){
		String token = extractToken(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION));
		try{
			Claims claims = Jwts.parser()
				.setSigningKey(this.jwtSecret.getSecretKey())
				.requireIssuer(this.jwtSecret.getIssuer())
				.parseClaimsJws(token)
				.getBody();
			
			if(!claims.get(this.jwtSecret.getId(), String.class).equals(target)) 
				throw new AuthenticateException("JWT토큰인증 오류", "접근할 수 없는 문서입니다.");
			
			return claims.get(this.jwtSecret.getId(), String.class).equals(target);
		}catch(IncorrectClaimException ICE){
			throw new IncorrectClaimException(ICE.getHeader(), ICE.getClaims(), "유효하지 않은 토큰입니다.");
		}catch(MissingClaimException MCE){
			throw new MissingClaimException(MCE.getHeader(), MCE.getClaims(), "유효하지 않은 토큰입니다.");
		}catch(SignatureException SE){
			throw new SignatureException("유효하지 않은 sign을 갖고있는 토큰입니다.");
		}
	}
	
	private String extractToken(String authorization){
		if(authorization == null || !authorization.startsWith("Bearer ")) throw new UnsupportedJwtException("토큰이 존재하지않거나, 토큰 인증 타입이 Bearer로 시작하지 않습니다.");
		return authorization.substring("Bearer ".length());
	}
	
}