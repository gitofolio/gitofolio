package com.gitofolio.api.controller.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable; 
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.auth.authenticate.Authenticator;
import com.gitofolio.api.service.proxy.CrudProxy;
import com.gitofolio.api.service.factory.CrudFactory;
import com.gitofolio.api.domain.user.EncodedProfileImage;
import com.gitofolio.api.domain.auth.PersonalAccessToken;
import com.gitofolio.api.service.auth.token.TokenGenerator;
import com.gitofolio.api.service.auth.token.TokenAble;
import com.gitofolio.api.service.auth.oauth.applications.OauthApplicationFactory;
import com.gitofolio.api.service.auth.oauth.OauthTokenPool;
import com.gitofolio.api.service.user.exception.IllegalParameterException;
import com.gitofolio.api.service.common.random.RandomKeyGenerator;

import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

@Controller
public class OAuthController{
	
	private final CrudProxy<UserDTO> userInfoCrudProxy;
	
	private final CrudProxy<EncodedProfileImage> encodedProfileImageCrudProxy;
	
	private final CrudProxy<PersonalAccessToken> personalAccessTokenCrudProxy;
	
	private final TokenGenerator jwtTokenGenerator;
	
	private final OauthApplicationFactory oauthApplicationFactory;
	
	private final OauthTokenPool oauthTokenPool;
	
	@RequestMapping(path = "/oauth", method = RequestMethod.GET)
	public String redirectToOauthApplication(@RequestParam(value = "application", defaultValue = "github", required=true) String application,
					    					@RequestParam(value = "redirect", required = false) String redirect,
											@RequestParam(value = "accesskey", required = false) String personalAccessKey){
		
		if(redirect == null) throw new IllegalParameterException("redirect url 오류", "redirect 파라미터값이 비어있습니다.");
		if(personalAccessKey == null) throw new IllegalParameterException("accesskey 오류", "accesskey 파라미터값이 비어있습니다.");
		
		String applicationUrl = this.oauthApplicationFactory.get(application).getUrlWithQueryString("?redirect=" + redirect + "+" + personalAccessKey);
		return "redirect:" + applicationUrl;
	}
	
	@RequestMapping(path = "/token/personal", method = RequestMethod.GET)
	public Object getPersonalAccessToken(@RequestParam(value = "application", defaultValue = "github", required = false) String application,
										 @RequestParam(value = "code", required = false) String code){

		if(code == null) return "redirect:"+this.oauthApplicationFactory.get(application).getUrl() + "&redirect_uri=http://api.gitofolio.com/token/personal";
		
		UserDTO userDTO = this.getUserDTO(application, code);
		PersonalAccessToken personalAccessToken = this.personalAccessTokenCrudProxy.create();
		
		return new ResponseEntity(personalAccessToken, HttpStatus.CREATED);
	}
	
	@RequestMapping(path = "/token/personal", method = RequestMethod.HEAD)
	public ResponseEntity<Object> isStillValidAccessToken(@RequestParam(value = "accesskey", required = false) Long personalAccesskey){
		if(personalAccesskey == null) throw new IllegalParameterException("accesskey 오류", "accesskey 파라미터값이 비어있습니다.");
		
		this.personalAccessTokenCrudProxy.read(personalAccesskey);
		
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@RequestMapping(path = "/token", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> getToken(@RequestBody Map<String, Object> payload, 
														HttpServletResponse httpServletResponse){
		String cert = (String)payload.get("cert");
		String personalAccessTokenValue = (String)payload.get("accessToken");
		if(cert == null) throw new IllegalParameterException("cert 오류", "payload에 cert값이 존재하지않습니다.");
		
		Map<String, String> responsePayload = new HashMap<String, String>();
		responsePayload.put("type", "Bearer");
		responsePayload.put("token", this.oauthTokenPool.getToken(cert, personalAccessTokenValue));
		this.oauthTokenPool.deleteToken(cert);
		
		return new ResponseEntity(responsePayload, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/oauth/{application}", method = RequestMethod.GET)
	public ResponseEntity<Object> authenticateOauth(@PathVariable(value = "application") String application,
													@RequestParam(value = "redirect", required=false) String redirect,
													@RequestParam(value = "code", defaultValue="invalidCode", required=false) String code,
													HttpServletResponse httpServletResponse){
		
		String[] queryStrings = redirect.split(" ");
		if(queryStrings.length != 2) throw new IllegalParameterException("queryString 오류", "redirect와 accesskey파라미터에 알수없는 오류가 있습니다.");
		redirect = queryStrings[0]; 
		Long personalAccessKey = Long.valueOf(queryStrings[1]);
		
		UserDTO userDTO = getUserDTO(application, code);
		String cert = RandomKeyGenerator.generateKey(2);
		saveTokenInPool(userDTO, cert, personalAccessKey);
		
		httpServletResponse.addHeader(HttpHeaders.LOCATION, redirect + "?cert=" + cert);
			
		this.encodedProfileImageCrudProxy.create(userDTO);
		
		return new ResponseEntity(HttpStatus.SEE_OTHER);
	}
	
	private void saveTokenInPool(UserDTO userDTO, String cert, Long personalAccessKey){
		String token = this.jwtTokenGenerator.generateToken((TokenAble)userDTO);
		String personalAccessTokenValue = this.personalAccessTokenCrudProxy.read(personalAccessKey).getTokenValue();
		this.oauthTokenPool.saveToken(cert, personalAccessTokenValue, token);
	}
	
	private UserDTO getUserDTO(String application, String code){
		Authenticator<UserDTO, String> authenticator = this.oauthApplicationFactory.get(application).getAuthenticator();
		UserDTO userDTO = authenticator.authenticate(code);
		return this.userInfoCrudProxy.create(userDTO);
	}
	
	@Autowired
	public OAuthController(@Qualifier("userInfoCrudFactory") CrudFactory<UserDTO> userInfoCrudFactory,
						   @Qualifier("encodedProfileImageCrudFactory") CrudFactory<EncodedProfileImage> encodedProfileImageCrudFactory,
						   @Qualifier("personalAccessTokenCrudFactory") CrudFactory<PersonalAccessToken> personalAccessTokenCrudFactory,
						   @Qualifier("jwtTokenGenerator") TokenGenerator jwtTokenGenerator,
						   OauthApplicationFactory oauthApplicationFactory,
						   OauthTokenPool oauthTokenPool){
		this.userInfoCrudProxy = userInfoCrudFactory.get();
		this.encodedProfileImageCrudProxy = encodedProfileImageCrudFactory.get();
		this.personalAccessTokenCrudProxy = personalAccessTokenCrudFactory.get();
		this.jwtTokenGenerator = jwtTokenGenerator;
		this.oauthApplicationFactory = oauthApplicationFactory;
		this.oauthTokenPool = oauthTokenPool;
	}
	
}