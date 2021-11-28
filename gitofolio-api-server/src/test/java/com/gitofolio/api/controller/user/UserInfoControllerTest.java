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

import com.gitofolio.api.service.user.factory.UserFactory;
import com.gitofolio.api.service.user.eraser.UserEraser;
import com.gitofolio.api.service.user.UserMapper;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.exception.*;

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
	
	@BeforeEach
	public void preInit(){
		UserDTO user = this.getUser();
		try{
			this.userInfoEraser.delete(user.getName());
			this.userInfoFactory.saveUser(user);
		} catch(NonExistUserException NEUE){}
		catch(DuplicationUserException DUE){}
	}
	
	@AfterEach
	public void postInit(){
		UserDTO user = this.getUser();
		try{
			this.userInfoEraser.delete(user.getName());
			this.userInfoFactory.saveUser(user);
		} catch(NonExistUserException NEUE){}
		catch(DuplicationUserException DUE){}
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
	
	
	// public void userInfo_POST_Test() throws Exception{
	// 	// given
	// 	UserDTO user = new UserDTO.Builder()
	// 		.name("savedUser")
	// 		.profileUrl("https://example.profileUrl.com?1123u8413478")
	// 		.build();
	//  
	//  String content = objectMapper.writeValueAsString(user);
	
	// 	// then
	// 	mockMvc.perform(post("/user").content(content).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
	// 		.andExpect(status().isCreated())
	// 		.andDo(document("user/get",
	// 					   	responseFields(
	// 							fieldWithPath("name").description("저장된 유저의 이름 입니다. 요청 HTTP 본문의 name과 동일해야합니다."),
	// 							fieldWithPath("profileUrl").description("저장된 유저의 프로필 URL입니다."),
	// 							fieldWithPath("links.[].rel").description("선택가능한 다음 선택지에 대한 key 입니다."),
	// 							fieldWithPath("links.[].method").description("HTTP METHOD"),
	// 							fieldWithPath("links.[].href").description("다음 선택지 요청 URL 입니다.")
	// 					   )
	// 			)
	// 	);
		
		
// 		try{
// 			this.userInfoEraser.deleteUser(user);
// 		} catch(NonExistUserException NEUE){}
// 		catch(DuplicationUserException DUE){}
// 	}
	
	private UserDTO getUser(){
		return new UserDTO.Builder()
			.name("name")
			.profileUrl("https://example.profileUrl.com?1123u8413478")
			.build();
	}
	
}