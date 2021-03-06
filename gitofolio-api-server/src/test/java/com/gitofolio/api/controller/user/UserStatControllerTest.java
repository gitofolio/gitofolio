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

import com.gitofolio.api.service.proxy.CrudProxy;
import com.gitofolio.api.service.factory.CrudFactory;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.exception.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme="https", uriHost="api.gitofolio.com", uriPort=80)
public class UserStatControllerTest{
	
	private MockMvc mockMvc;
	
	private CrudProxy<UserDTO> userStatCrudProxy;
	
	private CrudProxy<UserDTO> userInfoCrudProxy;
	
	@Autowired
	public UserStatControllerTest(@Qualifier("userInfoCrudFactory") CrudFactory<UserDTO> userInfoCrudFactory,
								 @Qualifier("userStatCrudFactory") CrudFactory<UserDTO> userStatCrudFactory,
								 MockMvc mockMvc){
		this.userInfoCrudProxy = userInfoCrudFactory.get();
		this.userStatCrudProxy = userStatCrudFactory.get();
		this.mockMvc = mockMvc;
	}
	
	@BeforeEach
	public void preInit(){
		UserDTO user = this.getUser();
		try{
			this.userInfoCrudProxy.delete(user.getName());
		} catch(NonExistUserException NEUE){}
		try{
			this.userInfoCrudProxy.create(user);
		}catch(DuplicationUserException DUE){}
		UserDTO result = this.userInfoCrudProxy.read(user.getName());
		assertEquals(user.getName(), result.getName());
	}
	
	@AfterEach
	public void postInit(){
		UserDTO user = this.getUser();
		try{
			this.userInfoCrudProxy.delete(user.getName());
		} catch(NonExistUserException NEUE){}
	}
	
	@Test
	public void userStat_GET_Test() throws Exception{
		//given 
		UserDTO user = this.getUser();
		
		// when
		mockMvc.perform(get("/user/{name}", user.getName()).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		
		// then
		mockMvc.perform(get("/user/stat/{name}", user.getName()).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("userstat/get",
						pathParameters(
							parameterWithName("name").description("?????? ??? ???????????? ?????? ?????? ???????????????.")
						),
						responseFields(
							fieldWithPath("id").description("????????? ????????? id ?????????."),
							fieldWithPath("name").description("????????? ????????? ?????? ?????????. ?????? ?????????????????? ?????????????????????."),
							fieldWithPath("profileUrl").description("????????? ????????? URL?????????."),
							fieldWithPath("userStat.totalVisitors").description("{name}??? ???????????? ????????? ????????? ???????????? ??? ?????????."),
							fieldWithPath("links.[].rel").description("??????????????? ?????? ???????????? ?????? key ?????????."),
							fieldWithPath("links.[].method").description("HTTP METHOD"),
							fieldWithPath("links.[].href").description("?????? ????????? ?????? URL ?????????.")
						)
			));
		
	}
	
	@Test
	public void userStat_GET_Fail_Test() throws Exception{
		// given
		String name = "nonExistUser";
		
		// then
		mockMvc.perform(get("/user/stat/{name}", name).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andDo(document("userstat/get/fail",
						pathParameters(
							parameterWithName("name").description("?????? ??? ???????????? ?????? ?????? ???????????????.")
						),
						responseFields(
							fieldWithPath("title").description("????????? ?????? ???????????????."),
							fieldWithPath("message").description("????????? ????????? ????????? ?????? ?????? ???????????? ????????? ?????????."),
							fieldWithPath("request").description("????????? ????????? request URL ?????????.")
						)
					));
	}
	
	private UserDTO getUser(){
		return new UserDTO.Builder()
			.id(0L)
			.name("name")
			.profileUrl("https://example.profileUrl.com?ust")
			.build();
	}
	
}