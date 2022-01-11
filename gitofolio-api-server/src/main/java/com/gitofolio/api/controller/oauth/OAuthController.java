package com.gitofolio.api.controller.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.auth.authenticate.Authenticator;
import com.gitofolio.api.service.user.proxy.CrudProxy;
import com.gitofolio.api.service.user.factory.CrudFactory;
import com.gitofolio.api.domain.user.EncodedProfileImage;
import com.gitofolio.api.service.auth.token.TokenGenerator;
import com.gitofolio.api.service.auth.token.TokenAble;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path="/oauth")
public class OAuthController{
	
	private final Authenticator<UserDTO, String> githubAuthenticator;
	
	private final CrudProxy<UserDTO> userInfoCrudProxy;
	
	private final CrudProxy<EncodedProfileImage> encodedProfileImageCrudProxy;
	
	private final TokenGenerator jwtTokenGenerator;
	
	@RequestMapping(path="/github", method=RequestMethod.GET)
	public ResponseEntity<UserDTO> receiveGithubCode(@RequestParam(value="code", defaultValue="invalidCode", required=false) String code, HttpServletResponse httpServletResponse){
		
		UserDTO userDTO = this.githubAuthenticator.authenticate(code);
		
		userDTO = this.userInfoCrudProxy.create(userDTO);
		
		String token = jwtTokenGenerator.generateToken((TokenAble)userDTO);
		httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		
		this.encodedProfileImageCrudProxy.create(userDTO);
		
		return new ResponseEntity(userDTO, HttpStatus.CREATED);
	}
	
	@Autowired
	public OAuthController(@Qualifier("githubAuthenticator") Authenticator<UserDTO, String> githubAuthenticator,
						   @Qualifier("userInfoCrudFactory") CrudFactory<UserDTO> userInfoCrudFactory,
						   @Qualifier("encodedProfileImageCrudFactory") CrudFactory<EncodedProfileImage> encodedProfileImageCrudFactory,
						   @Qualifier("jwtTokenGenerator") TokenGenerator jwtTokenGenerator){
		this.githubAuthenticator = githubAuthenticator;
		this.userInfoCrudProxy = userInfoCrudFactory.get();
		this.encodedProfileImageCrudProxy = encodedProfileImageCrudFactory.get();
		this.jwtTokenGenerator = jwtTokenGenerator;
	}
	
}