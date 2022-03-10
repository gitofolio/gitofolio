package com.gitofolio.api.controller.auth;

import org.springframework.http.*;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gitofolio.api.service.proxy.CrudProxy;
import com.gitofolio.api.service.factory.CrudFactory;
import com.gitofolio.api.domain.auth.PersonalAccessToken;
import com.gitofolio.api.service.auth.oauth.Authenticator;
import com.gitofolio.api.service.auth.oauth.applications.*;
import com.gitofolio.api.service.auth.oauth.OauthTokenPool;
import com.gitofolio.api.service.user.exception.IllegalParameterException;
import com.gitofolio.api.service.user.dtos.*;
import com.gitofolio.api.service.auth.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme="https", uriHost="api.gitofolio.com", uriPort=80)
public class PersonalAccessTokenControllerTest{
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private PersonalAccessTokenService personalAccessTokenService;
	
	@Autowired
	@Qualifier("userInfoCrudFactory")
	private CrudFactory<UserDTO> userInfoCrudFactory;
	
	@MockBean
	private OauthApplicationFactory oauthApplicationFactory;
	
	@MockBean
	private OauthTokenPool oauthTokenPool;
	
	@MockBean
	@Qualifier("githubApplication")
	private OauthApplication oauthApplication; 
	
	@MockBean
	@Qualifier("githubAuthenticator")
	private Authenticator<UserDTO, String> authenticator;
	
	@Test
	public void personal_access_token_get_Test() throws Exception{
		// when
		setUpAuthenticator();
		
		// then
		mockMvc.perform(get("/token/personal?application={application}&code={code}","github", "ahdfbhjqwebfnsdbfsdahfbqwef").accept(MediaType.ALL).header(HttpHeaders.AUTHORIZATION, "Pat {token}"))
			.andExpect(status().isCreated())
			.andDo(document("personal/token",
							requestParameters(
								parameterWithName("application").description("personal access token 발급에 사용할 Oauth 애플리케이션 이름 입니다."),
								parameterWithName("code").description("사용자가 입력하는 칸이 아니며, oauth 로그인후 리다이렉트 과정에서 사용되는 파라미터 값 입니다.")
							),
							requestHeaders(
								headerWithName(HttpHeaders.AUTHORIZATION).description("인증에 사용할 토큰 입니다. 토큰 타입은 Pat이며, 공백 후, 실제 토큰이 입력됩니다.")
							),
							responseFields(
								fieldWithPath("tokenKey").description("Personal access token의 key 입니다."),
								fieldWithPath("tokenValue").description("Personal access token의 value 입니다."),
								fieldWithPath("lastUsedDate").description("Personal access token이 마지막으로 사용된 날짜 입니다."),
								fieldWithPath("userInfo.id").description("Personal access token에 연결된 유저의 id 입니다."),
								fieldWithPath("userInfo.name").description("Personal access token에 연결된 유저의 이름 입니다."),
								fieldWithPath("userInfo.profileUrl").description("Personal access token에 연결된 유저의 프로필 사진 주소 입니다.")
							)
					));
			
		
	}
	
	private PersonalAccessToken getPersonalAccessToken(){
		PersonalAccessToken personalAccessToken = new PersonalAccessToken();
		personalAccessToken.setTokenKey(1473L);
		return personalAccessToken;
	}
	
	private void setUpAuthenticator(){
		given(this.oauthApplicationFactory.get(any(String.class))).willReturn(this.oauthApplication);
		setUpOauthApplication();
		given(this.authenticator.authenticate(any(String.class))).willReturn(this.getUserDTO());
	}
	
	private void setUpOauthApplication(){
		given(this.oauthApplication.getAuthenticator()).willReturn(this.authenticator);
		given(this.oauthApplication.getUrl()).willReturn("redirecturl");
	}
	
	private UserDTO getUserDTO(){
		return new UserDTO.Builder()
			.name("name")
			.id(1L)
			.profileUrl("test.url")
			.build();
	}
	
	@AfterEach
	public void postInit(){
		CrudProxy<UserDTO> userInfoCrudProxy = this.userInfoCrudFactory.get();
		UserDTO user = this.getUserDTO();
		try{
			userInfoCrudProxy.delete(user.getName());
			this.personalAccessTokenService.delete(user.getName());
		} catch(Exception e){}
	}
	
}