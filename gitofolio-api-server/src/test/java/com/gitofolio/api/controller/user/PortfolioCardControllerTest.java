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
import com.gitofolio.api.service.user.dtos.*;
import com.gitofolio.api.service.user.exception.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme="https", uriHost="api.gitofolio.com")
public class PortfolioCardControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	@Qualifier("userInfoEraser")
	private UserEraser userInfoEraser;
	
	@Autowired
	@Qualifier("portfolioCardEraser")
	private UserEraser portfolioCardEraser;
	
	@Autowired
	@Qualifier("userInfoFactory")
	private UserFactory userInfoFactory;
	
	@Autowired
	@Qualifier("portfolioCardFactory")
	private UserFactory portfolioCardFactory;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	public void PortfolioCard_GET_테스트() throws Exception{
		// when
		String name = "name";
		
		// then
		mockMvc.perform(get("/portfoliocards/{name}?cards=1,5", name).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("portfoliocards/get",
							pathParameters(
								parameterWithName("name").description("포트폴리오 카드를 가져올 유저 이름입니다.")
							),
							requestParameters(
								parameterWithName("cards").description("={from},{to} 가져올 포트폴리오카드 범위입니다. from 에서 to까지의 포트폴리오 카드를 가져옵니다.")
							),
							responseFields(
								fieldWithPath("name").description("요청한 유저의 이름 입니다. 경로 파라미터값과 동일해야합니다."),
								fieldWithPath("profileUrl").description("유저의 프로필 URL입니다."),
								fieldWithPath("portfolioCards.[].portfolioCardArticle").description("포트폴리오 카드의 본문 내용입니다."),
								fieldWithPath("portfolioCards.[].portfolioCardStars").description("포트폴리오가 받은 스타 갯수 입니다."),
								fieldWithPath("portfolioCards.[].portfolioUrl").description("포트폴리오 카드에 연결된 포트폴리오 링크입니다."),
								fieldWithPath("links.[].rel").description("선택가능한 다음 선택지에 대한 key 입니다."),
								fieldWithPath("links.[].method").description("HTTP METHOD"),
								fieldWithPath("links.[].href").description("다음 선택지 요청 URL 입니다."),
								fieldWithPath("links.[].parameter").description("요청 가능한 파라미터들 입니다.").optional()
							)
						));
	}
	
	@Test
	public void PortfolioCard_GET_Fail_테스트() throws Exception{
		// when
		String name = "nonExistUser";
		
		// then
		mockMvc.perform(get("/portfoliocards/{name}?cards=1,5", name).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andDo(document("portfoliocards/get/fail",
							pathParameters(
								parameterWithName("name").description("포토폴리오 카드를 가져올 유저 이름입니다.")
							),
							requestParameters(
								parameterWithName("cards").description("={from},{to} 가져올 포트폴리오카드 범위입니다. from 에서 to까지의 포트폴리오 카드를 가져옵니다.")
							),
							responseFields(
								fieldWithPath("title").description("에러의 주요 원인입니다."),
								fieldWithPath("message").description("에러가 발생한 이유에 대한 가장 근본적인 해결책 입니다."),
								fieldWithPath("request").description("에러가 발생한 request URL 입니다.")
							)
						));
	}
	
	@Test
	public void Portfolio_GET_ParameterFail_테스트() throws Exception{
		// when
		String name = "name";
		
		// then
		mockMvc.perform(get("/portfoliocards/{name}?cards=here,tohere", name).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotAcceptable())
			.andDo(document("portfoliocards/get/parameterfail",
						   pathParameters(
								parameterWithName("name").description("포토폴리오 카드를 가져올 유저 이름입니다.")
							),
							requestParameters(
								parameterWithName("cards").description("={from},{to} 가져올 포트폴리오카드 범위입니다. from 에서 to까지의 포트폴리오 카드를 가져옵니다.")
							),
							responseFields(
								fieldWithPath("title").description("에러의 주요 원인입니다."),
								fieldWithPath("message").description("에러가 발생한 이유에 대한 가장 근본적인 해결책 입니다."),
								fieldWithPath("request").description("에러가 발생한 request URL 입니다.")
							)
						));
	}
	
	@Test
	public void PortfolioCard_DELETE_테스트() throws Exception{
		// when
		String name = "name";
		
		// then
		mockMvc.perform(delete("/portfoliocards/{name}?card=1", name).accept(MediaType.ALL))
			.andExpect(status().isOk())
			.andDo(document("portfoliocards/delete",
							pathParameters(
								parameterWithName("name").description("포트폴리오 카드를 삭제할 유저 이름입니다.")
							),
							requestParameters(
								parameterWithName("card").description("={a} 삭제할 포트폴리오 카드 번호를 파라미터로 넘기면 해당 포트폴리오카드를 삭제합니다. 아니라면, 모든 포트폴리오 카드를 삭제합니다.")
							)
						));
	}
	
	@Test
	public void PortfolioCard_DELETE_Fail_테스트() throws Exception{
		// when
		String name = "nonExistUser";
		
		// then
		mockMvc.perform(delete("/portfoliocards/{name}?card=1", name).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andDo(document("portfoliocards/delete/fail",
							pathParameters(
								parameterWithName("name").description("포트폴리오 카드를 삭제할 유저 이름입니다.")
							),
							requestParameters(
								parameterWithName("card").description("={a} 삭제할 포트폴리오 카드 번호를 파라미터로 넘기면 해당 포트폴리오카드를 삭제합니다. 아니라면, 모든 포트폴리오 카드를 삭제합니다.")
							),
							responseFields(
								fieldWithPath("title").description("에러의 주요 원인입니다."),
								fieldWithPath("message").description("에러가 발생한 이유에 대한 가장 근본적인 해결책 입니다."),
								fieldWithPath("request").description("에러가 발생한 request URL 입니다.")
							)
						));
	}
	
	@Test
	public void PortfolioCard_DELETE_ParameterFail_테스트() throws Exception{
		// when
		String name = "name";
		
		// then
		mockMvc.perform(delete("/portfoliocards/{name}?card=iwantdeletethis",name).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotAcceptable())
			.andDo(document("portfoliocards/delete/parameterfail",
						   pathParameters(
								parameterWithName("name").description("포토폴리오 카드를 가져올 유저 이름입니다.")
							),
							requestParameters(
								parameterWithName("card").description("={a} 삭제할 포트폴리오 카드 번호를 파라미터로 넘기면 해당 포트폴리오카드를 삭제합니다. 아니라면, 모든 포트폴리오 카드를 삭제합니다.")
							),
							responseFields(
								fieldWithPath("title").description("에러의 주요 원인입니다."),
								fieldWithPath("message").description("에러가 발생한 이유에 대한 가장 근본적인 해결책 입니다."),
								fieldWithPath("request").description("에러가 발생한 request URL 입니다.")
							)
						));
	}
	
	@Test
	public void PortfolioCard_POST_테스트() throws Exception{
		// given
		UserDTO user = new UserDTO.Builder()
			.name("name")
			.profileUrl("https://example.profileUrl.com?1123u8413478")
			.portfolioCardDTO(this.getPortfolioCard("this is new portfoliocard", 0, "https://api.gitofolio.com/portfolio/name/6"))
			.build();
		
		// when
		String content = objectMapper.writeValueAsString(user);
		
		// then
		mockMvc.perform(post("/portfoliocards").content(content).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andDo(document("portfoliocards/post",
							relaxedRequestFields(
								fieldWithPath("name").description("저장할 유저의 이름 입니다. 매칭되는 유저에 포토폴리오 카드가 저장됩니다."),
								fieldWithPath("profileUrl").description("유저의 프로필 URL입니다."),
								fieldWithPath("portfolioCards.[].portfolioCardArticle").description("포트폴리오 카드의 본문 내용입니다."),
								fieldWithPath("portfolioCards.[].portfolioCardStars").description("포트폴리오가 받은 스타 갯수 입니다."),
								fieldWithPath("portfolioCards.[].portfolioUrl").description("포트폴리오 카드에 연결된 포트폴리오 링크입니다.")
							),
							responseFields(
								fieldWithPath("name").description("요청한 유저의 이름 입니다. 경로 파라미터값과 동일해야합니다."),
								fieldWithPath("profileUrl").description("유저의 프로필 URL입니다."),
								fieldWithPath("portfolioCards.[].portfolioCardArticle").description("포트폴리오 카드의 본문 내용입니다."),
								fieldWithPath("portfolioCards.[].portfolioCardStars").description("포트폴리오가 받은 스타 갯수 입니다."),
								fieldWithPath("portfolioCards.[].portfolioUrl").description("포트폴리오 카드에 연결된 포트폴리오 링크입니다."),
								fieldWithPath("links.[].rel").description("선택가능한 다음 선택지에 대한 key 입니다."),
								fieldWithPath("links.[].method").description("HTTP METHOD"),
								fieldWithPath("links.[].href").description("다음 선택지 요청 URL 입니다."),
								fieldWithPath("links.[].parameter").description("요청 가능한 파라미터들 입니다.").optional()
							)
						));
		
	}
	
	@Test
	public void PortfolioCard_POST_Fail_테스트() throws Exception{
		// given
		UserDTO user = new UserDTO.Builder()
			.name("nonExistUser")
			.profileUrl("https://example.profileUrl.com?1123u8413478")
			.portfolioCardDTO(this.getPortfolioCard("this is new portfoliocard", 0, "https://api.gitofolio.com/portfolio/name/6"))
			.build();
		
		// when
		String content = objectMapper.writeValueAsString(user);
		
		// then
		mockMvc.perform(post("/portfoliocards").content(content).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andDo(document("portfoliocards/post/fail",
						   relaxedRequestFields(
								fieldWithPath("name").description("저장할 유저의 이름 입니다. 매칭되는 유저에 포토폴리오 카드가 저장됩니다."),
								fieldWithPath("profileUrl").description("유저의 프로필 URL입니다."),
								fieldWithPath("portfolioCards.[].portfolioCardArticle").description("포트폴리오 카드의 본문 내용입니다."),
								fieldWithPath("portfolioCards.[].portfolioCardStars").description("포트폴리오가 받은 스타 갯수 입니다."),
								fieldWithPath("portfolioCards.[].portfolioUrl").description("포트폴리오 카드에 연결된 포트폴리오 링크입니다.")
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
			this.userInfoFactory.saveUser(user);
			this.portfolioCardFactory.saveUser(user);
		}catch(DuplicationUserException DUE){}
		UserDTO result = this.portfolioCardFactory.getUser(user.getName());
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
			this.portfolioCardFactory.saveUser(user);
		}catch(DuplicationUserException DUE){}
		UserDTO result = this.portfolioCardFactory.getUser(user.getName());
		assertEquals(user.getName(), result.getName());
	}
	
	private UserDTO getUser(){
		return new UserDTO.Builder()
			.name("name")
			.profileUrl("https://example.profileUrl.com?1123u8413478")
			.portfolioCardDTO(this.getPortfolioCard("Lorem ipsum", 0, "https://api.gitofolio.com/portfolio/name/1"))
			.portfolioCardDTO(this.getPortfolioCard("Lorem ipsum", 0, "https://api.gitofolio.com/portfolio/name/2"))
			.portfolioCardDTO(this.getPortfolioCard("Lorem ipsum", 0, "https://api.gitofolio.com/portfolio/name/3"))
			.portfolioCardDTO(this.getPortfolioCard("Lorem ipsum", 0, "https://api.gitofolio.com/portfolio/name/4"))
			.portfolioCardDTO(this.getPortfolioCard("Lorem ipsum", 0, "https://api.gitofolio.com/portfolio/name/5"))
			.build();
	}
	
	private PortfolioCardDTO getPortfolioCard(String article, Integer stars, String url){
		return new PortfolioCardDTO.Builder()
			.portfolioCardArticle(article)
			.portfolioCardStars(stars)
			.portfolioUrl(url)
			.build(); 
	}
	
}