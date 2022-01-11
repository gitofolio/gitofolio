package com.gitofolio.api.controller.user;

import org.springframework.http.MediaType;
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

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.any;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gitofolio.api.service.user.proxy.CrudProxy;
import com.gitofolio.api.service.user.factory.CrudFactory;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.exception.*;
import com.gitofolio.api.service.auth.token.TokenValidator;
import com.gitofolio.api.service.auth.token.TokenAble;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.util.Optional;

@SpringBootTest
@AutoConfigureRestDocs(uriScheme="https", uriHost="api.gitofolio.com", uriPort=80)
@AutoConfigureMockMvc
public class UserInfoControllerTest{
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	@Qualifier("userInfoCrudFactory")
	private CrudFactory<UserDTO> crudFactory;
	
	private CrudProxy<UserDTO> crudProxy;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@MockBean
	@Qualifier("jwtTokenValidator")
	private TokenValidator tokenValidator;
	
	@Test
	public void userInfo_GET_Test() throws Exception{
		// then
		mockMvc.perform(get("/user/{name}", "name").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("user/get", 
						   	pathParameters(
								parameterWithName("name").description("name에 해당하는 유저의 표현이 응답됩니다.")
						   	),
						   	responseFields(
								fieldWithPath("id").description("요청한 유저의 id입니다."),
								fieldWithPath("name").description("요청한 유저의 이름 입니다. 경로 파라미터값과 동일해야합니다."),
								fieldWithPath("profileUrl").description("유저의 프로필 URL입니다."),
								fieldWithPath("links.[].rel").description("선택가능한 다음 선택지에 대한 key 입니다."),
								fieldWithPath("links.[].method").description("HTTP METHOD"),
								fieldWithPath("links.[].href").description("다음 선택지 요청 URL 입니다.")
						   )
				)
			);			
	}
	
	@Test
	public void userInfo_GET_Fail_Test() throws Exception{
		// then
		mockMvc.perform(get("/user/{name}", "nonExistUser").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andDo(document("user/get/fail",
							responseFields(
								fieldWithPath("title").description("에러의 주요 원인입니다."),
								fieldWithPath("message").description("에러가 발생한 이유에 대한 가장 근본적인 해결책 입니다."),
								fieldWithPath("request").description("에러가 발생한 request URL 입니다.")
						   	)
					)
			);
	}
	
	@Test
	public void userInfo_DELETE_Test() throws Exception{
		// given
		String name = this.getUser().getName();
		
		// then
		mockMvc.perform(delete("/user/{name}", name).accept(MediaType.ALL))
			.andExpect(status().isOk())
			.andDo(document("user/delete",
							pathParameters(
								parameterWithName("name").description("삭제할 유저의 이름 입니다.")
							)
					));
		
	}
	
	@Test
	public void userInfo_DELETE_Fail_Test() throws Exception{
		// given
		String name = "nonExistUser";
		
		UserDTO user = new UserDTO();
		user.setName(name);
		given(tokenValidator.validateToken((TokenAble)user)).willReturn(true);
		// then
		mockMvc.perform(delete("/user/{name}", name).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andDo(document("user/delete/fail",
							pathParameters(
								parameterWithName("name").description("삭제할 유저의 name 입니다.")
							),
							responseFields(
								fieldWithPath("title").description("에러의 주요 원인입니다."),
								fieldWithPath("message").description("에러가 발생한 이유에 대한 가장 근본적인 해결책 입니다."),
								fieldWithPath("request").description("에러가 발생한 request URL 입니다.")
							)
					));
	}
	
	@Test
	public void loginedUser_Get_Test() throws Exception{
		// when
		given(tokenValidator.currentLogined()).willReturn(this.getUser().getName());
		
		// then
		mockMvc.perform(get("/user").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("user/logined",
							responseFields(
								fieldWithPath("id").description("요청한 유저의 id입니다."),
								fieldWithPath("name").description("요청한 유저의 이름 입니다. 경로 파라미터값과 동일해야합니다."),
								fieldWithPath("profileUrl").description("유저의 프로필 URL입니다."),
								fieldWithPath("links.[].rel").description("선택가능한 다음 선택지에 대한 key 입니다."),
								fieldWithPath("links.[].method").description("HTTP METHOD"),
								fieldWithPath("links.[].href").description("다음 선택지 요청 URL 입니다.")
							)
						)
				);
	}
	
	@Test
	public void loginedUser_Get_Fail_Test() throws Exception{
		// when
		given(tokenValidator.currentLogined()).willThrow(UnsupportedJwtException.class);
		// then
		mockMvc.perform(get("/user").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andDo(document("user/logined/fail",
							relaxedResponseFields(
								fieldWithPath("title").description("에러의 주요 원인입니다.")
							)
						)
				);
	}
	
	@BeforeEach
	public void preInit(){
		given(tokenValidator.validateToken(any(String.class))).willReturn(true);
		given(tokenValidator.validateToken(any(TokenAble.class))).willReturn(true);
		this.crudProxy = this.crudFactory.get();
		UserDTO user = this.getUser();
		try{
			this.crudProxy.delete(user.getName());
		} catch(NonExistUserException NEUE){}
		try{
			this.crudProxy.create(user);
		}catch(DuplicationUserException DUE){}
		UserDTO result = this.crudProxy.read(user.getName());
		assertEquals(user.getName(), result.getName());
	}
	
	@AfterEach
	public void postInit(){
		UserDTO user = this.getUser();
		try{
			this.crudProxy.delete(user.getName());
		} catch(NonExistUserException NEUE){}
		try{
			this.crudProxy.create(user);
		}catch(DuplicationUserException DUE){DUE.printStackTrace();}
	}
	
	private UserDTO getUser(){
		UserDTO user = new UserDTO.Builder()
			.id(0L)
			.name("name")
			.profileUrl("https://example.profileUrl.com?uit")
			.build();
		return user;
	}
	
}