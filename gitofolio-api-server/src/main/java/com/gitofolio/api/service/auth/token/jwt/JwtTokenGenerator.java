package com.gitofolio.api.service.auth.token.jwt;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.gitofolio.api.service.auth.token.*;

import io.jsonwebtoken.*;

import java.util.Date;

@Service
public class JwtTokenGenerator implements TokenGenerator{
	
	private final JwtSecret jwtSecret;
	
	@Override
	public String generateToken(String target){
		return this.generate(target);
	}
	
	@Override
	public String generateToken(TokenAble target){
		return this.generate(target.token());
	}
	
	private String generate(String target){
		return Jwts.builder()
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.signWith(SignatureAlgorithm.HS256, this.jwtSecret.getSecretKey())
			.setIssuer(this.jwtSecret.getIssuer())
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + 3600000))
			.claim(this.jwtSecret.getId(), target)
			.compact();
	}
	
	@Autowired
	public JwtTokenGenerator(JwtSecret jwtSecret){
		this.jwtSecret = jwtSecret;
	}
	
}