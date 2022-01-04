package com.gitofolio.api.controller.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.proxy.EncodedProfileImageProxy;
import com.gitofolio.api.service.auth.authenticate.Authenticator;
import com.gitofolio.api.service.user.proxy.CrudProxy;
import com.gitofolio.api.service.auth.SessionProcessor;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path="/oauth")
public class OAuthController{
	
	private final Authenticator<UserDTO, String> githubAuthenticator;
	
	private final CrudProxy<UserDTO> userInfoCrudProxy;
	
	private final  SessionProcessor<UserDTO> loginSessionProcessor;
	
	private final EncodedProfileImageProxy encodedProfileImageProxy;
	
	@RequestMapping(path="/github", method=RequestMethod.GET)
	public ResponseEntity<UserDTO> receiveGithubCode(@RequestParam(value="code") String code){
		
		UserDTO userDTO = this.githubAuthenticator.authenticate(code);
		loginSessionProcessor.setAttribute(userDTO);
		
		userDTO = this.userInfoCrudProxy.create(userDTO);
		
		this.encodedProfileImageProxy.save(userDTO);
		
		return new ResponseEntity(userDTO, HttpStatus.CREATED);
	}
	
	@Autowired
	public OAuthController(@Qualifier("githubAuthenticator") Authenticator<UserDTO, String> githubAuthenticator,
						   @Qualifier("userInfoCrudProxy") CrudProxy<UserDTO> userInfoCrudProxy,
						   @Qualifier("loginSessionProcessor") SessionProcessor<UserDTO> loginSessionProcessor,
						   @Qualifier("encodedProfileImageProxy") EncodedProfileImageProxy encodedProfileImageProxy){
		this.githubAuthenticator = githubAuthenticator;
		this.userInfoCrudProxy = userInfoCrudProxy;
		this.loginSessionProcessor = loginSessionProcessor;
		this.encodedProfileImageProxy = encodedProfileImageProxy;
	}
	
}