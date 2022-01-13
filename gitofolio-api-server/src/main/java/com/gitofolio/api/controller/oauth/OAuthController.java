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
import com.gitofolio.api.service.user.proxy.CrudProxy;
import com.gitofolio.api.service.user.factory.CrudFactory;
import com.gitofolio.api.domain.user.EncodedProfileImage;
import com.gitofolio.api.service.auth.token.TokenGenerator;
import com.gitofolio.api.service.auth.token.TokenAble;
import com.gitofolio.api.service.auth.oauth.applications.OauthApplicationFactory;
import com.gitofolio.api.service.auth.oauth.OauthTokenPool;
import com.gitofolio.api.service.user.exception.IllegalParameterException;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

@Controller
public class OAuthController{
	
	private final CrudProxy<UserDTO> userInfoCrudProxy;
	
	private final CrudProxy<EncodedProfileImage> encodedProfileImageCrudProxy;
	
	private final TokenGenerator jwtTokenGenerator;
	
	private final OauthApplicationFactory oauthApplicationFactory;
	
	private final OauthTokenPool oauthTokenPool;
	
	@RequestMapping(path="/oauth", method=RequestMethod.GET)
	public String redirectToOauthApplication(@RequestParam(value="application", defaultValue="invalidApplication" ,required=true) String application,
					    @RequestParam(value="redirect", required=false) String redirect){
		
		if(redirect == null) throw new IllegalParameterException("redirect url 오류", "redirect 파라미터값이 비어있습니다.");
		
		String applicationUrl = this.oauthApplicationFactory.get(application).getUrlWithRedirect(redirect);
		
		return "redirect:"+applicationUrl;
	}
	
	@RequestMapping(path="/token", method=RequestMethod.POST)
	public ResponseEntity<Map<String, String>> getToken(@RequestBody Map<String, Object> payload, 
														HttpServletResponse httpServletResponse){
		String cert = (String)payload.get("cert");
		if(cert == null) throw new IllegalParameterException("cert 오류", "payload에 cert값이 존재하지않습니다.");
		
		Map<String, String> responsePayload = new HashMap<String, String>();
		responsePayload.put("type", "Bearer");
		responsePayload.put("token", this.oauthTokenPool.getToken(cert));
		
		return new ResponseEntity(responsePayload, HttpStatus.OK);
	}
	
	@RequestMapping(path="/oauth/{application}", method=RequestMethod.GET)
	public ResponseEntity<Object> authenticateOauth(@PathVariable(value="application") String application,
													@RequestParam(value="redirect", required=false) String redirect,
													@RequestParam(value="code", defaultValue="invalidCode", required=false) String code,
													HttpServletResponse httpServletResponse){
		
		if(redirect == null) throw new IllegalParameterException("redirect url 오류", "redirect 파라미터값이 비어있습니다. 올바른 경로로 요청해주세요");
		
		Authenticator<UserDTO, String> authenticator = this.oauthApplicationFactory.get(application).getAuthenticator();
		UserDTO userDTO = authenticator.authenticate(code);
		
		userDTO = this.userInfoCrudProxy.create(userDTO);
		
		String token = jwtTokenGenerator.generateToken((TokenAble)userDTO);
		String uuid = UUID.randomUUID().toString().replaceAll("-","");
		this.oauthTokenPool.create(uuid, token);
		
		httpServletResponse.addHeader(HttpHeaders.LOCATION, redirect+"?cert="+uuid);
			
		this.encodedProfileImageCrudProxy.create(userDTO);
		
		return new ResponseEntity(HttpStatus.SEE_OTHER);
	}
	
	@Autowired
	public OAuthController(@Qualifier("userInfoCrudFactory") CrudFactory<UserDTO> userInfoCrudFactory,
						   @Qualifier("encodedProfileImageCrudFactory") CrudFactory<EncodedProfileImage> encodedProfileImageCrudFactory,
						   @Qualifier("jwtTokenGenerator") TokenGenerator jwtTokenGenerator,
						   OauthApplicationFactory oauthApplicationFactory,
						   OauthTokenPool oauthTokenPool){
		this.userInfoCrudProxy = userInfoCrudFactory.get();
		this.encodedProfileImageCrudProxy = encodedProfileImageCrudFactory.get();
		this.jwtTokenGenerator = jwtTokenGenerator;
		this.oauthApplicationFactory = oauthApplicationFactory;
		this.oauthTokenPool = oauthTokenPool;
	}
	
}