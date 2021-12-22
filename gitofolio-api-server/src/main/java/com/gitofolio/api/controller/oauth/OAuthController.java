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
import com.gitofolio.api.service.auth.authenticate.Authenticator;
import com.gitofolio.api.service.user.factory.UserFactory;
import com.gitofolio.api.service.auth.SessionProcessor;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path="/oauth")
public class OAuthController{
	
	@Autowired
	@Qualifier("githubAuthenticator")
	private Authenticator<UserDTO, String> githubAuthenticator;
	
	@Autowired
	@Qualifier("userInfoFactory")
	private UserFactory userInfoFactory;
	
	@Autowired
	@Qualifier("loginSessionProcessor")
	private SessionProcessor<UserDTO> loginSessionProcessor;
	
	@RequestMapping(path="/github", method=RequestMethod.GET)
	public ResponseEntity<UserDTO> receiveGithubCode(@RequestParam(value="code") String code){
		UserDTO userDTO = this.githubAuthenticator.authenticate(code);
		
		loginSessionProcessor.setAttribute(userDTO);
		
		userDTO = this.userInfoFactory.saveUser(userDTO);
		
		return new ResponseEntity(userDTO, HttpStatus.CREATED);
	}
	
}