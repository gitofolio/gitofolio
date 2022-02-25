package com.gitofolio.api.controller.auth;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.http.*;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.auth.authenticate.Authenticator;
import com.gitofolio.api.service.proxy.CrudProxy;
import com.gitofolio.api.service.factory.CrudFactory;
import com.gitofolio.api.domain.user.EncodedProfileImage;
import com.gitofolio.api.domain.auth.PersonalAccessToken;
import com.gitofolio.api.service.auth.token.*;
import com.gitofolio.api.service.auth.oauth.applications.OauthApplicationFactory;
import com.gitofolio.api.service.auth.oauth.OauthTokenPool;
import com.gitofolio.api.service.user.exception.IllegalParameterException;
import com.gitofolio.api.service.common.random.RandomKeyGenerator;
import com.gitofolio.api.aop.log.datacollector.annotation.RequestDataCollector;

import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

@Controller
public class OAuthController{
	
	private final CrudProxy<UserDTO> userInfoCrudProxy;
	private final CrudProxy<EncodedProfileImage> encodedProfileImageCrudProxy;
	private final CrudProxy<PersonalAccessToken> personalAccessTokenCrudProxy;
	private final TokenGenerator tokenGenerator;
	private final OauthApplicationFactory oauthApplicationFactory;
	private final OauthTokenPool oauthTokenPool;
	
	@RequestDataCollector(path="/oauth")
	@RequestMapping(path="/oauth", method = RequestMethod.GET)
	public String redirectToOauthApplication(@RequestParam(value = "application", defaultValue = "github", required=true) String application,
					    					@RequestParam(value = "redirect", required = false) String redirect,
											@RequestParam(value = "accesskey", required = false) String personalAccessKey){
		
		if(isInvalidRedirectUrl(redirect)) throw new IllegalParameterException("redirect url 오류", "redirect 파라미터값이 비어있습니다.");
		if(isInvalidPersonalAccessKey(personalAccessKey)) throw new IllegalParameterException("accesskey 오류", "accesskey 파라미터값이 비어있습니다.");
		
		String applicationUrl = this.oauthApplicationFactory.get(application).getUrlWithQueryString("?redirect=" + redirect + "+" + personalAccessKey);
		return "redirect:" + applicationUrl;
	}
	
	
	private boolean isInvalidRedirectUrl(String redirect){
		return redirect == null;
	}
	
	private boolean isInvalidPersonalAccessKey(String personalAccessKey){
		return personalAccessKey == null;
	}
	
	@RequestDataCollector(path="/oauth/{application}")
	@RequestMapping(path = "/oauth/{application}", method = RequestMethod.GET)
	public ResponseEntity<Object> redirectWithCert(@PathVariable(value = "application") String application,
													@RequestParam(value = "redirect", required = false) String redirect,
													@RequestParam(value = "code", defaultValue = "invalidCode", required=false) String code,
													HttpServletResponse httpServletResponse){
		
		String[] queryStrings = this.getQueryStrings(redirect);
		
		Long personalAccessKey = Long.valueOf(queryStrings[1]);
		UserDTO userDTO = getUserDTO(application, code);
		String cert = RandomKeyGenerator.generateKey(2);
		saveTokenInPool(userDTO, cert, personalAccessKey);
		
		
		redirect = queryStrings[0]; 
		httpServletResponse.addHeader(HttpHeaders.LOCATION, redirect + "?cert=" + cert);
			
		this.encodedProfileImageCrudProxy.create(userDTO);
		
		return new ResponseEntity(HttpStatus.SEE_OTHER);
	}
	
	private String[] getQueryStrings(String queryString){
		String[] ans = queryString.split(" ");
		if(ans.length != 2) throw new IllegalParameterException("queryString 오류", "redirect와 accesskey파라미터에 알수없는 오류가 있습니다.");
		return ans;
	}
	
	private void saveTokenInPool(UserDTO userDTO, String cert, Long personalAccessKey){
		String token = this.tokenGenerator.generateToken((TokenAble)userDTO);
		String personalAccessTokenValue = this.personalAccessTokenCrudProxy.read(personalAccessKey).getTokenValue();
		this.oauthTokenPool.saveToken(cert, personalAccessTokenValue, token);
	}
	
	@RequestDataCollector(path="/token")
	@RequestMapping(path = "/token", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> getAuthToken(@RequestBody Map<String, Object> payload, 
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
	
	private UserDTO getUserDTO(String application, String code){
		Authenticator<UserDTO, String> authenticator = this.oauthApplicationFactory.get(application).getAuthenticator();
		UserDTO userDTO = authenticator.authenticate(code);
		return this.userInfoCrudProxy.create(userDTO);
	}
	
	@Autowired
	public OAuthController(@Qualifier("userInfoCrudFactory") CrudFactory<UserDTO> userInfoCrudFactory,
						   @Qualifier("encodedProfileImageCrudFactory") CrudFactory<EncodedProfileImage> encodedProfileImageCrudFactory,
						   @Qualifier("personalAccessTokenCrudFactory") CrudFactory<PersonalAccessToken> personalAccessTokenCrudFactory,
						   @Qualifier("jwtTokenGenerator") TokenGenerator tokenGenerator,
						   OauthApplicationFactory oauthApplicationFactory,
						   OauthTokenPool oauthTokenPool){
		this.userInfoCrudProxy = userInfoCrudFactory.get();
		this.encodedProfileImageCrudProxy = encodedProfileImageCrudFactory.get();
		this.personalAccessTokenCrudProxy = personalAccessTokenCrudFactory.get();
		this.tokenGenerator = tokenGenerator;
		this.oauthApplicationFactory = oauthApplicationFactory;
		this.oauthTokenPool = oauthTokenPool;
	}
	
}