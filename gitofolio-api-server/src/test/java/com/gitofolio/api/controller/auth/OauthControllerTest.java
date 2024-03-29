package com.gitofolio.api.controller.auth;

import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.any;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.gitofolio.api.service.auth.oauth.OauthTokenPool;
import com.gitofolio.api.service.auth.oauth.applications.OauthApplicationFactory;
import com.gitofolio.api.service.common.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme="https", uriHost="api.gitofolio.com", uriPort=80)
public class OauthControllerTest{
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private OauthTokenPool oauthTokenPool;
	
	private ObjectMapper objectMapper = ApplicationContextProvider.getApplicationContext().getBean("prettyObjectMapper", ObjectMapper.class);
	
	@Test
	public void getToken() throws Exception{
		// given
		Map<String, String> payload = new HashMap<String, String>();
		payload.put("cert", "7f6538de87c744ed92b70d4fb9ee070c");
		payload.put("accessToken", "54255f6a03eb475996d1a75f4efc28fefeaa25962f9a4de28ce04e745562a4c30a98748751494f258a5498f3de345c71e4c58ae0695e4e539d29c478d1a958191427ffab41564d5e862d3e8a75742232");
		
		// when
		given(this.oauthTokenPool.getToken(any(String.class), any(String.class))).willReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
		String body = objectMapper.writeValueAsString(payload);
			
		// then
		mockMvc.perform(post("/token").content(body).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("oauth/token",
							requestFields(
								fieldWithPath("cert").description("쿼리스트링으로 전달받은 cert값을 입력합니다."),
								fieldWithPath("accessToken").description("사전에 발급받은 personal access token의 tokenvalue를 입력합니다.")
							),
							responseFields(
								fieldWithPath("type").description("token의 인증 타입입니다."),
								fieldWithPath("token").description("유저의 인증에 사용될 실제 토큰입니다.")
							)
						));
		
	}
	
}