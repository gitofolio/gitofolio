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

import com.gitofolio.api.service.user.factory.UserFactory;
import com.gitofolio.api.service.user.eraser.UserEraser;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.exception.*;
import com.gitofolio.api.service.auth.SessionProcessor;
import com.gitofolio.api.controller.user.UserInfoController;
import com.gitofolio.api.service.user.UserStatisticsService;
import com.gitofolio.api.service.user.UserStatService;
import com.gitofolio.api.service.user.factory.hateoas.Hateoas;
import com.gitofolio.api.service.user.factory.hateoas.UserInfoHateoas;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserInfoController.class)
@AutoConfigureRestDocs(uriScheme="https", uriHost="api.gitofolio.com", uriPort=80)
@AutoConfigureMockMvc
public class UserInfoControllerTest{
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	@Qualifier("userInfoEraser")
	private UserEraser userInfoEraser;
	
	@MockBean
	@Qualifier("userInfoFactory")
	private UserFactory userInfoFactory;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@MockBean
	@Qualifier("loginSessionProcessor")
	private SessionProcessor loginSessionProcessor;
	
	@MockBean
	private UserStatService userStatService;
	
	@MockBean
	private UserStatisticsService userStatisticsService;
	
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
		// when
		given(userInfoFactory.getUser("nonExistUser")).willThrow(new NonExistUserException("존재하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/user/nonExistUser"));
		
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
								parameterWithName("name").description("삭제할 유저의 name 입니다.")
							)
					));
		
	}
	
	@Test
	public void userInfo_DELETE_Fail_Test() throws Exception{
		// given
		String name = "nonExistUser";
		
		// when
		given(userInfoEraser.delete("nonExistUser")).willThrow(new NonExistUserException("존재하지 않는 유저에 대한 삭제 요청입니다.", "유저 이름을 확인해주세요", "/user/nonExistUser"));
		
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
		// given
		UserDTO user = new UserDTO.Builder()
			.id(0L)
			.name("name")
			.profileUrl("https://example.profileUrl.com?1123u8413478")
			.build();
		
		// when
		given(loginSessionProcessor.getAttribute()).willReturn(Optional.ofNullable(user));
		
		// then
		mockMvc.perform(get("/user").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("user/logined",
							responseFields(
								fieldWithPath("id").description("요청한 유저의 id입니다."),
								fieldWithPath("name").description("요청한 유저의 이름 입니다. 경로 파라미터값과 동일해야합니다."),
								fieldWithPath("profileUrl").description("유저의 프로필 URL입니다.")
							)
						)
				);
	}
	
	@Test
	public void loginedUser_Get_Fail_Test() throws Exception{
		// when
		given(loginSessionProcessor.getAttribute()).willReturn(Optional.ofNullable(null));
		
		// then
		mockMvc.perform(get("/user").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andDo(document("user/logined/fail",
							responseFields(
								fieldWithPath("title").description("에러의 주요 원인입니다."),
								fieldWithPath("message").description("에러가 발생한 이유에 대한 가장 근본적인 해결책 입니다.")
							)
						)
				);
	}
	
	@BeforeEach
	public void initMockObj(){
		given(loginSessionProcessor.getAttribute()).willReturn(Optional.ofNullable(this.getUser()));
		given(userInfoFactory.saveUser(any(UserDTO.class))).willReturn(this.getUser());
		given(userInfoFactory.getUser(any(String.class))).willReturn(this.getUser());
		given(userInfoEraser.delete(any(String.class))).willReturn(this.getUser().getName());
	}
	
	private UserDTO getUser(){
		UserDTO user = new UserDTO.Builder()
			.id(0L)
			.name("name")
			.profileUrl("https://example.profileUrl.com?1123u8413478")
			.build();
		Hateoas userInfoHateoas = new UserInfoHateoas();
		user.setLinks(userInfoHateoas.getLinks());
		return user;
	}
	
	
	
	
}