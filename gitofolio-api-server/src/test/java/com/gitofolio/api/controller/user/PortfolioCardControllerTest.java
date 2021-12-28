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

import org.mockito.Mock;
import static org.mockito.Mockito.when;

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

import com.gitofolio.api.service.user.proxy.UserProxy;
import com.gitofolio.api.service.user.eraser.UserEraser;
import com.gitofolio.api.service.user.dtos.*;
import com.gitofolio.api.service.user.exception.*;
import com.gitofolio.api.service.auth.LoginSessionProcessor;
import com.gitofolio.api.service.auth.SessionProcessor;
import com.gitofolio.api.service.user.factory.hateoas.Hateoas;
import com.gitofolio.api.service.user.factory.hateoas.PortfolioCardHateoas;
import com.gitofolio.api.service.user.UserStatService;
import com.gitofolio.api.service.user.UserStatisticsService;
import com.gitofolio.api.service.user.svg.portfoliocard.PortfolioCardSvgFactory;
import com.gitofolio.api.service.user.proxy.EncodedProfileImageProxy;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PortfolioCardController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme="https", uriHost="api.gitofolio.com", uriPort=80)
public class PortfolioCardControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	@Qualifier("portfolioCardEraser")
	private UserEraser portfolioCardEraser;
	
	@MockBean
	@Qualifier("portfolioCardProxy")
	private UserProxy portfolioCardProxy;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@MockBean
	@Qualifier("loginSessionProcessor")
	private SessionProcessor loginSessionProcessor;
	
	@MockBean
	private UserStatService userStatService;
	
	@MockBean
	private UserStatisticsService userStatisticsService;
	
	@MockBean
	private PortfolioCardSvgFactory portfolioCardSvgFactory;
	
	@MockBean
	private EncodedProfileImageProxy encodedProfileImageProxy;
	
	@Test
	public void PortfolioCard_GET_테스트() throws Exception{
		// when
		String name = "name";
		
		given(portfolioCardProxy.getUser(name, "1,5")).willReturn(this.getUser());
		
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
								fieldWithPath("id").description("요청한 유저의 id입니다. 깃허브 id와 동일합니다"),
								fieldWithPath("name").description("요청한 유저의 이름 입니다. 경로 파라미터값과 동일해야합니다."),
								fieldWithPath("profileUrl").description("유저의 프로필 URL입니다."),
								fieldWithPath("portfolioCards.[].id").description("포트폴리오 카드의 id 입니다"),
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
		
		given(portfolioCardProxy.getUser(name,"1,5"))
			.willThrow(new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/portfoliocards/nonExistUser"));
		
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
	public void PortfolioCard_GET_Fail_But_UserExist_테스트() throws Exception{
		// when
		String name = "name";
		
		// when
		given(portfolioCardProxy.getUser(any(String.class)))
			.willThrow(new NonExistUserException("이 유저는 어떠한 포트폴리오 카드도 갖고있지 않습니다.", "요청 전 포트폴리오 카드를 생성해주세요", "/portfoliocards/"+name));
		
		// then
		mockMvc.perform(get("/portfoliocards/{name}", name).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andDo(document("portfoliocards/get/fail_but_exist_user",
							pathParameters(
								parameterWithName("name").description("포토폴리오 카드를 가져올 유저 이름입니다.")
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
		
		given(portfolioCardProxy.getUser(name, "here,tohere"))
			.willThrow(new IllegalParameterException("잘못된 파라미터 요청", "포트폴리오 카드요청 파라미터를 잘못 입력하셨습니다.", "https://api.gitofolio.com/portfoliocards/name?cards=here,tohere"));
		
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
		
		given(portfolioCardEraser.delete(name, "1")).willReturn("name");
		
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
		
		given(portfolioCardEraser.delete(name, "1")).willThrow(new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/portfoliocards/nonExistUser"));
		
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
		
		given(portfolioCardEraser.delete(name, "iwantdeletethis"))
			.willThrow(new IllegalParameterException("잘못된 파라미터 요청", "포트폴리오 카드요청 파라미터를 잘못 입력하셨습니다.", "https://api.gitofolio.com/portfoliocards/user?card=iwantdeletethis"));
		
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
			.id(0L)
			.name("name")
			.profileUrl("https://example.profileUrl.com?1123u8413478")
			.portfolioCardDTO(this.getPortfolioCard(6L, "this is new portfoliocard", 0, "https://api.gitofolio.com/portfolio/name/6"))
			.build();
		
		UserDTO userCopy = new UserDTO.Builder()
			.id(0L)
			.name("name")
			.profileUrl("https://example.profileUrl.com?1123u8413478")
			.portfolioCardDTO(this.getPortfolioCard(6L, "this is new portfoliocard", 0, "https://api.gitofolio.com/portfolio/name/6"))
			.build();
		
		Hateoas portfolioCardHateoas = new PortfolioCardHateoas();
		userCopy.setLinks(portfolioCardHateoas.getLinks());
		
		// when
		String content = objectMapper.writeValueAsString(user);
		given(portfolioCardProxy.saveUser(any(UserDTO.class))).willReturn(userCopy);
		
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
								fieldWithPath("id").description("요청한 유저의 id입니다. 깃허브 Id와 동일합니다"),
								fieldWithPath("name").description("요청한 유저의 이름 입니다. 경로 파라미터값과 동일해야합니다."),
								fieldWithPath("profileUrl").description("유저의 프로필 URL입니다."),
								fieldWithPath("portfolioCards.[].id").description("저장된 포트폴리오 카드의 id 입니다"),
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
			.id(1L)
			.name("nonExistUser")
			.profileUrl("https://example.profileUrl.com?1123u8413478")
			.portfolioCardDTO(this.getPortfolioCard(6L, "this is new portfoliocard", 0, "https://api.gitofolio.com/portfolio/name/6"))
			.build();
		
		// when
		String content = objectMapper.writeValueAsString(user);
		given(this.portfolioCardProxy.saveUser(any(UserDTO.class)))
			.willThrow(new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/portfoliocards/nonExistUser"));
		
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
	
	@Test
	public void PortfolioCard_PUT_Test() throws Exception{
		// given
		UserDTO editUser = new UserDTO.Builder()
			.id(this.getUser().getId())
			.name("name")
			.profileUrl("https://example.profileUrl.com?1123u8413478")
			.portfolioCardDTO(this.getPortfolioCard(0L, "Lorem ipsum", 0, "https://api.gitofolio.com/portfolio/name/1"))
			.build();
		
		UserDTO userCopy = new UserDTO.Builder()
			.id(this.getUser().getId())
			.name("name")
			.profileUrl("https://example.profileUrl.com?1123u8413478")
			.portfolioCardDTO(this.getPortfolioCard(0L, "Lorem ipsum", 0, "https://api.gitofolio.com/portfolio/name/1"))
			.build();
		
		Hateoas portfolioCardHateoas = new PortfolioCardHateoas();
		userCopy.setLinks(portfolioCardHateoas.getLinks());
		
		// when
		String content = objectMapper.writeValueAsString(editUser);
		given(portfolioCardProxy.editUser(any(UserDTO.class))).willReturn(userCopy);
		
		// then
		mockMvc.perform(put("/portfoliocards").content(content).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("portfoliocards/put",
				relaxedRequestFields(
					fieldWithPath("id").description("수정할 유저의 id입니다. 수정 요청은 유저의 이름이 아닌 id값을 기반으로 동작합니다"),
					fieldWithPath("name").description("수정할 유저의 이름 입니다. 매칭되는 유저에 포토폴리오 카드가 저장됩니다."),
					fieldWithPath("profileUrl").description("유저의 프로필 URL입니다."),
					fieldWithPath("portfolioCards.[].id").description("수정대상 포트폴리오 카드 id 입니다. id값에 해당하는 포트폴리오 카드가 수정됩니다."),
					fieldWithPath("portfolioCards.[].portfolioCardArticle").description("수정할 포트폴리오 카드의 본문 내용입니다."),
					fieldWithPath("portfolioCards.[].portfolioCardStars").description("포트폴리오가 받은 스타 갯수 입니다. 임의로 수정 불가능 합니다."),
					fieldWithPath("portfolioCards.[].portfolioUrl").description("수정할 포트폴리오 카드에 연결된 포트폴리오 링크입니다.")
				),
				responseFields(
					fieldWithPath("id").description("수정할 유저의 id입니다."),
					fieldWithPath("name").description("수정할 유저의 이름 입니다. 경로 파라미터값과 동일해야합니다."),
					fieldWithPath("profileUrl").description("유저의 프로필 URL입니다."),
					fieldWithPath("portfolioCards.[].id").description("포트폴리오 카드의 id 입니다"),
					fieldWithPath("portfolioCards.[].portfolioCardArticle").description("수정된 포트폴리오 카드의 본문 내용입니다."),
					fieldWithPath("portfolioCards.[].portfolioCardStars").description("포트폴리오가 받은 스타 갯수 입니다."),
					fieldWithPath("portfolioCards.[].portfolioUrl").description("수정된 포트폴리오 카드에 연결된 포트폴리오 링크입니다."),
					fieldWithPath("links.[].rel").description("선택가능한 다음 선택지에 대한 key 입니다."),
					fieldWithPath("links.[].method").description("HTTP METHOD"),
					fieldWithPath("links.[].href").description("다음 선택지 요청 URL 입니다."),
					fieldWithPath("links.[].parameter").description("요청 가능한 파라미터들 입니다.").optional()
				)
			)
		);
	}
	
	@BeforeEach
	public void initMockObj(){
		given(loginSessionProcessor.getAttribute()).willReturn(Optional.ofNullable(this.getUser()));
	}
	
	private UserDTO getUser(){
		UserDTO user = new UserDTO.Builder()
			.id(0L)
			.name("name")
			.profileUrl("https://example.profileUrl.com?1123u8413478")
			.portfolioCardDTO(this.getPortfolioCard(0L, "Lorem ipsum", 0, "https://api.gitofolio.com/portfolio/name/1"))
			.portfolioCardDTO(this.getPortfolioCard(1L, "Lorem ipsum", 0, "https://api.gitofolio.com/portfolio/name/2"))
			.portfolioCardDTO(this.getPortfolioCard(2L, "Lorem ipsum", 0, "https://api.gitofolio.com/portfolio/name/3"))
			.portfolioCardDTO(this.getPortfolioCard(3L, "Lorem ipsum", 0, "https://api.gitofolio.com/portfolio/name/4"))
			.portfolioCardDTO(this.getPortfolioCard(4L, "Lorem ipsum", 0, "https://api.gitofolio.com/portfolio/name/5"))
			.build();
		
		Hateoas portfolioCardHateoas = new PortfolioCardHateoas();
		user.setLinks(portfolioCardHateoas.getLinks());
		return user;
	}
	
	private PortfolioCardDTO getPortfolioCard(Long id, String article, Integer stars, String url){
		return new PortfolioCardDTO.Builder()
			.id(id)
			.portfolioCardArticle(article)
			.portfolioCardStars(stars)
			.portfolioUrl(url)
			.build(); 
	}
	
}