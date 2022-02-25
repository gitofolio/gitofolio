package com.gitofolio.api.controller.auth;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.http.*;

import com.gitofolio.api.service.proxy.CrudProxy;
import com.gitofolio.api.service.factory.CrudFactory;
import com.gitofolio.api.domain.auth.PersonalAccessToken;
import com.gitofolio.api.domain.user.*;
import com.gitofolio.api.service.auth.authenticate.Authenticator;
import com.gitofolio.api.service.auth.oauth.applications.OauthApplicationFactory;
import com.gitofolio.api.service.user.exception.IllegalParameterException;
import com.gitofolio.api.service.user.dtos.*;
import com.gitofolio.api.aop.log.datacollector.annotation.RequestDataCollector;

@Controller
public class PersonalAccessTokenController{
	
	private final CrudProxy<PersonalAccessToken> personalAccessTokenCrudProxy;
	private final CrudProxy<UserDTO> userInfoCrudProxy;
	private final CrudProxy<EncodedProfileImage> encodedProfileImageCrudProxy;
	private final OauthApplicationFactory oauthApplicationFactory;
	private String accessTokenRedirectUrl = "&redirect_uri=https://api.gitofolio.com/token/personal";
	private String testAccessTokenRedirectUrl = "&redirect_uri=https://api-server-gitofolio-qfnxv.run.goorm.io/token/personal";
	
	@RequestDataCollector(path="/token/personal")
	@RequestMapping(path="/token/personal", method = RequestMethod.GET)
	public Object getPersonalAccessToken(@RequestParam(value = "application", defaultValue = "github", required = false) String application,
										 @RequestParam(value = "code", required = false) String code){

		if(code == null) return "redirect:"+this.oauthApplicationFactory.get(application).getUrl() + this.accessTokenRedirectUrl;
		
		UserDTO userDTO = this.getUserDTO(application, code);
		PersonalAccessToken personalAccessToken = this.personalAccessTokenCrudProxy.create(userDTO);
		
		this.encodedProfileImageCrudProxy.create(userDTO);
		
		return new ResponseEntity(personalAccessToken, HttpStatus.CREATED);
	}
	
	private UserDTO getUserDTO(String application, String code){
		Authenticator<UserDTO, String> authenticator = this.oauthApplicationFactory.get(application).getAuthenticator();
		UserDTO userDTO = authenticator.authenticate(code);
		return this.userInfoCrudProxy.create(userDTO);
	}
	
	@RequestDataCollector(path="/token/personal")
	@RequestMapping(path = "/token/personal", method = RequestMethod.HEAD)
	public ResponseEntity<Object> isStillValidAccessToken(@RequestParam(value = "accesskey", required = false) Long personalAccesskey){
		if(personalAccesskey == null) throw new IllegalParameterException("accesskey 오류", "accesskey 파라미터값이 비어있습니다.");
		
		this.personalAccessTokenCrudProxy.read(personalAccesskey);
		
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@Autowired
	public PersonalAccessTokenController(@Qualifier("personalAccessTokenCrudFactory") CrudFactory<PersonalAccessToken> personalAccessTokenCrudFactory,
										 @Qualifier("userInfoCrudFactory") CrudFactory<UserDTO> userInfoCrudFactory,
										 @Qualifier("encodedProfileImageCrudFactory") CrudFactory<EncodedProfileImage> encodedProfileImageCrudFactory,
										 OauthApplicationFactory oauthApplicationFactory){
		this.personalAccessTokenCrudProxy = personalAccessTokenCrudFactory.get();
		this.userInfoCrudProxy = userInfoCrudFactory.get();
		this.oauthApplicationFactory = oauthApplicationFactory;
		this.encodedProfileImageCrudProxy = encodedProfileImageCrudFactory.get();
	}
	
}