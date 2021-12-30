package com.gitofolio.api.controller.user;

import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
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

import com.gitofolio.api.service.user.proxy.UserProxy;
import com.gitofolio.api.service.user.eraser.UserEraser;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.dtos.PortfolioCardDTO;
import com.gitofolio.api.service.user.exception.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme="https", uriHost="api.gitofolio.com", uriPort=80)
public class UserStatisticsControllerTest{
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	@Qualifier("userStatisticsProxy")
	private UserProxy userStatisticsProxy;
	
	@Autowired
	@Qualifier("userInfoProxy")
	private UserProxy userInfoProxy;
	
	@Autowired
	@Qualifier("userInfoEraser")
	private UserEraser userInfoEraser;
	
	@Autowired
	@Qualifier("portfolioCardProxy")
	private UserProxy portfolioCardProxy;
	
	@Test
	public void userDailyStat_GET_Test() throws Exception{
		// given
		UserDTO user = this.portfolioCardProxy.getUser(this.getUser().getName());
		Long cardId = user.getPortfolioCards().get(0).getId();
		
		// when
		mockMvc.perform(get("/portfolio/{userId}/{cardId}?redirect=false", user.getId(), cardId)
						.accept(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.REFERER, "https://example.com"));
		
		// then
		mockMvc.perform(get("/user/dailystat/{name}", user.getName()).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("userdailystat/get",
							pathParameters(
								parameterWithName("name").description("일일 통계 정보를 요청할 유저 이름입니다.")
							),
							responseFields(
								fieldWithPath("id").description("요청한 유저의 id 입니다."),
								fieldWithPath("name").description("요청한 유저의 이름 입니다. 경로 파라미터값과 동일해야합니다."),
								fieldWithPath("profileUrl").description("유저의 프로필 URL입니다."),
								fieldWithPath("userDailyStat.visitorStatistics.[].visitDate")
									.description("방문자 수 통계가 저장된 날짜입니다. yyyy-mm-dd 형태로 응답되며, 최대 7개의 날짜가 저장되고 초과시 오래된 순으로 자동 삭제됩니다."),
								fieldWithPath("userDailyStat.visitorStatistics.[].visitorCount").description("visitDate에 해당하는 날짜에 방문한 방문자 수 입니다."),
								fieldWithPath("userDailyStat.refferingSites.[].refferingDate").description("유입경로 통계가 저장된 날짜입니다. yyyy-mm-dd 형태로 응답됩니다."),
								fieldWithPath("userDailyStat.refferingSites.[].refferingSiteName").description("refferingDate에 해당하는 날짜에 유입된 경로 입니다."),
								fieldWithPath("links.[].rel").description("선택가능한 다음 선택지에 대한 key 입니다."),
								fieldWithPath("links.[].method").description("HTTP METHOD"),
								fieldWithPath("links.[].href").description("다음 선택지 요청 URL 입니다.")
							)
						));
	}
	
	@Test
	public void userDailyStat_GET_Fail_Test() throws Exception{
		// given
		String name = "nonExistUser";
		
		// then
		mockMvc.perform(get("/user/dailystat/{name}", name).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andDo(document("userdailystat/get/fail",
							pathParameters(
								parameterWithName("name").description("일일 통계 정보를 요청할 유저 이름입니다.")
							),
							responseFields(
								fieldWithPath("title").description("에러의 주요 원인입니다."),
								fieldWithPath("message").description("에러가 발생한 이유에 대한 가장 근본적인 해결책 입니다."),
								fieldWithPath("request").description("에러가 발생한 request URL 입니다.")
							)
						));
	}
	
	@BeforeEach
	public void preInit(){
		UserDTO user = this.getUser();
		try{
			this.userInfoEraser.delete(user.getName());
		} catch(NonExistUserException NEUE){}
		try{
			this.userInfoProxy.saveUser(user);
			this.portfolioCardProxy.saveUser(user);
		}catch(DuplicationUserException DUE){}
		UserDTO result = this.userInfoProxy.getUser(user.getName());
		assertEquals(user.getName(), result.getName());
	}
	
	@AfterEach
	public void postInit(){
		UserDTO user = this.getUser();
		try{
			this.userInfoEraser.delete(user.getName());
		} catch(NonExistUserException NEUE){}
		try{
			this.userInfoProxy.saveUser(user);
			this.portfolioCardProxy.saveUser(user);
		}catch(DuplicationUserException DUE){}
		UserDTO result = this.userInfoProxy.getUser(user.getName());
		assertEquals(user.getName(), result.getName());
	}
	
	private UserDTO getUser(){
		PortfolioCardDTO portfolioCardDTO1 = new PortfolioCardDTO.Builder()
			.id(3L)
			.portfolioCardArticle("p1")
			.portfolioCardStars(1)
			.portfolioUrl("pu1")
			.build();
		
		return new UserDTO.Builder()
			.id(0L)
			.name("name")
			.profileUrl("https://example.profileUrl.com?1123u8413478")
			.portfolioCardDTO(portfolioCardDTO1)
			.build();
	}
	
}