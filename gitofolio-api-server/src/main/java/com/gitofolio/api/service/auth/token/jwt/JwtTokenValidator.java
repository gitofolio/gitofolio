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
	private HttpServletRequest httpServletRequest; // thread-safe하니 의심하지말것
	
	@Autowired
	private JwtSecret jwtSecret;
	
	@Override
	public String currentLogined(){
		return this.parseToken();
	}
	
	@Override
	public boolean validateToken(String validateTarget){
		String tokenValue = this.parseToken();
		if(!tokenValue.equals(validateTarget))
			throw new AuthenticateException("JWT토큰인증 오류", "접근할 수 없는 문서입니다.");
		return true;
	}
	
	@Override
	public boolean validateToken(TokenAble validateTarget){
		String tokenValue = this.parseToken();
		if(!tokenValue.equals(validateTarget.token()))
			throw new AuthenticateException("JWT토큰인증 오류", "접근할 수 없는 문서입니다.");
		return true;
	}
	
	private String parseToken(){
		String token = extractTokenInHeader();
		try{
			return Jwts.parser()
				.setSigningKey(this.jwtSecret.getSecretKey())
				.requireIssuer(this.jwtSecret.getIssuer())
				.parseClaimsJws(token)
				.getBody()
				.get(this.jwtSecret.getId(), String.class);
		}catch(IncorrectClaimException ICE){
			throw new IncorrectClaimException(ICE.getHeader(), ICE.getClaims(), "유효하지 않은 토큰입니다.");
		}catch(MissingClaimException MCE){
			throw new MissingClaimException(MCE.getHeader(), MCE.getClaims(), "유효하지 않은 토큰입니다.");
		}catch(SignatureException SE){
			throw new SignatureException("유효하지 않은 sign을 갖고있는 토큰입니다.");
		}
	}
	
	private String extractTokenInHeader(){
		String authorization = this.httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
		if(authorization == null || !authorization.startsWith("Bearer ")) throw new UnsupportedJwtException("토큰이 존재하지않거나, 토큰 인증 타입이 Bearer로 시작하지 않습니다.");
		return authorization.substring("Bearer ".length());
	}
	
}