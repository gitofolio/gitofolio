package com.gitofolio.api.controller.user;

import org.springframework.http.MediaType;
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

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gitofolio.api.service.user.factory.UserFactory;
import com.gitofolio.api.service.user.eraser.UserEraser;
import com.gitofolio.api.service.user.UserMapper;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.exception.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public class UserInfoControllerTest{
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	@Qualifier("userInfoEraser")
	private UserEraser userInfoEraser;
	
	@Autowired
	@Qualifier("userInfoFactory")
	private UserFactory userInfoFactory;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@BeforeEach
	public void preInit(){
		UserDTO user = this.getUser();
		try{
			this.userInfoEraser.delete(user.getName());
		} catch(NonExistUserException NEUE){}
		try{
			this.userInfoFactory.saveUser(user);
		}catch(DuplicationUserException DUE){}
		UserDTO result = this.userInfoFactory.getUser(user.getName());
		assertEquals(user.getName(), result.getName());
	}
	
	@AfterEach
	public void postInit(){
		UserDTO user = this.getUser();
		try{
			this.userInfoEraser.delete(user.getName());
		} catch(NonExistUserException NEUE){}
		try{
			this.userInfoFactory.saveUser(user);
		}catch(DuplicationUserException DUE){DUE.printStackTrace();}
	}
	
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
		mockMvc.perform(get("/user/{name}", "invalidUser").accept(MediaType.APPLICATION_JSON))
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
	public void userInfo_POST_Test() throws Exception{
		// when
		UserDTO user = new UserDTO.Builder()
			.name("savedUser")
			.profileUrl("https://example.profileUrl.com?1123u8413478")
			.build();
	 
		String httpBody = objectMapper.writeValueAsString(user);
	
		// then
		mockMvc.perform(post("/user").content(httpBody).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andDo(document("user/post",
							requestFields(
								fieldWithPath("name").description("저장될 유저의 이름입니다. 중복되면 안됩니다"),
								fieldWithPath("profileUrl").description("유저의 프로필 사진 Url입니다.").optional(),
								fieldWithPath("portfolioCards").description("유저가 갖고있는 포토폴리오 카드들입니다. 이 URL로 들어오는 portfolioCards 저장 요청은 무시됩니다.").optional(),
								fieldWithPath("userStat").description("유저의 토탈 통계 자료입니다. 서버에 의해 생성되며 입력 요청은 무시됩니다.").optional(),
								fieldWithPath("userDailyStat").description("유저의 일일 통계 자료입니다. 서버에 의해 생성되며 입력 요청은 무시됩니다.").optional(),
								fieldWithPath("links").description("HATEOAS 모음 입니다. 서버에 의해 생성되며 입력 요청은 무시됩니다.").optional()
							),
						   	responseFields(
								fieldWithPath("name").description("저장된 유저의 이름 입니다. 요청 HTTP 본문의 name과 동일해야합니다."),
								fieldWithPath("profileUrl").description("저장된 유저의 프로필 URL입니다."),
								fieldWithPath("links.[].rel").description("선택가능한 다음 선택지에 대한 key 입니다."),
								fieldWithPath("links.[].method").description("HTTP METHOD"),
								fieldWithPath("links.[].href").description("다음 선택지 요청 URL 입니다.")
						   )
				)
		);
		
		try{
			this.userInfoEraser.delete(user.getName());
		} catch(NonExistUserException NEUE){}
		
	}
	
	@Test
	public void userInfo_POST_FAIL_Test() throws Exception{
		// when
		UserDTO user = this.getUser();
		
		String httpBody = objectMapper.writeValueAsString(user);
		
		// then
		mockMvc.perform(post("/user").content(httpBody).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isConflict())
			.andDo(document("user/post/fail",
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
		// when
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
		// when
		String name = "nonExistUser";
		
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
	
	private UserDTO getUser(){
		return new UserDTO.Builder()
			.name("name")
			.profileUrl("https://example.profileUrl.com?1123u8413478")
			.build();
	}
	
}