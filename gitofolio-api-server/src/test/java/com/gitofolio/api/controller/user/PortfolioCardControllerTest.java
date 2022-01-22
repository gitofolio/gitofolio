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
import static org.mockito.BDDMockito.any;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gitofolio.api.service.proxy.CrudProxy;
import com.gitofolio.api.service.factory.CrudFactory;
import com.gitofolio.api.service.user.dtos.*;
import com.gitofolio.api.service.user.exception.*;
import com.gitofolio.api.service.auth.token.TokenValidator;
import com.gitofolio.api.service.auth.token.TokenAble;
import com.gitofolio.api.service.factory.hateoas.Hateoas;
import com.gitofolio.api.service.factory.hateoas.PortfolioCardHateoas;
import com.gitofolio.api.service.user.UserStatService;
import com.gitofolio.api.service.user.UserStatisticsService;
import com.gitofolio.api.service.user.svg.portfoliocard.PortfolioCardSvgFactory;
import com.gitofolio.api.service.user.svg.portfoliocard.PortfolioCardSvgDTO;
import com.gitofolio.api.service.factory.Factory;
import com.gitofolio.api.service.factory.parameter.PortfolioCardSvgParameter;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme="https", uriHost="api.gitofolio.com", uriPort=80)
public class PortfolioCardControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	@Qualifier("portfolioCardCrudFactory")
	private CrudFactory<UserDTO> portfolioCardCrudFactory;
	
	@Autowired
	@Qualifier("userInfoCrudFactory")
	private CrudFactory<UserDTO> userInfoCrudFactory;
	
	private CrudProxy<UserDTO> portfolioCardCrudProxy;
	
	private CrudProxy<UserDTO> userInfoCrudProxy;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@MockBean
	@Qualifier("jwtTokenValidator")
	private TokenValidator tokenValidator;
	
	@Test
	public void PortfolioCard_GET_테스트() throws Exception{
		// when
		String name = "name";
		
		// then
		mockMvc.perform(get("/portfoliocards/{name}", name).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("portfoliocards/get",
							pathParameters(
								parameterWithName("name").description("포트폴리오 카드를 가져올 유저 이름입니다.")
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
		
		// then
		mockMvc.perform(get("/portfoliocards/{name}", name).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andDo(document("portfoliocards/get/fail",
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
	public void PortfolioCard_DELETE_테스트() throws Exception{
		// when
		String name = "name";
		
		Long id = this.portfolioCardCrudProxy.read(name).getPortfolioCards().get(0).getId();
		
		// then
		mockMvc.perform(delete("/portfoliocards/{name}?id={id}", name, id).accept(MediaType.ALL))
			.andExpect(status().isOk())
			.andDo(document("portfoliocards/delete",
							pathParameters(
								parameterWithName("name").description("포트폴리오 카드를 삭제할 유저 이름입니다.")
							),
							requestParameters(
								parameterWithName("id").description("={cardId} 삭제할 포트폴리오 카드 id를 파라미터로 넘기면 해당 포트폴리오카드를 삭제합니다. 아니라면, 모든 포트폴리오 카드를 삭제합니다.")
							)
						));
	}
	
	@Test
	public void PortfolioCard_DELETE_Fail_테스트() throws Exception{
		// when
		String name = "nonExistUser";
		UserDTO user = new UserDTO();
		user.setName(name);
		given(tokenValidator.validateToken((TokenAble)user)).willReturn(true);
		
		// then
		mockMvc.perform(delete("/portfoliocards/{name}?id={cardId}", name, 1L).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andDo(document("portfoliocards/delete/fail",
							pathParameters(
								parameterWithName("name").description("포트폴리오 카드를 삭제할 유저 이름입니다.")
							),
							requestParameters(
								parameterWithName("id").description("={cardId} 삭제할 포트폴리오 카드 id를 파라미터로 넘기면 해당 포트폴리오카드를 삭제합니다. 아니라면, 모든 포트폴리오 카드를 삭제합니다.")
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
			.profileUrl("https://example.profileUrl.com?pct")
			.portfolioCardDTO(this.getPortfolioCard(6L, "this is new portfoliocard", 0, "https://api.gitofolio.com/portfolio/name/6"))
			.build();
		
		// when
		String content = objectMapper.writeValueAsString(user);
		given(tokenValidator.validateToken((TokenAble)user)).willReturn(true);
		
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
			.profileUrl("https://example.profileUrl.com?pct")
			.portfolioCardDTO(this.getPortfolioCard(6L, "this is new portfoliocard", 0, "https://api.gitofolio.com/portfolio/name/6"))
			.build();
		
		// when
		String content = objectMapper.writeValueAsString(user);
		given(tokenValidator.validateToken((TokenAble)user)).willReturn(true);
		
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
			.profileUrl("https://example.profileUrl.com?pct")
			.portfolioCardDTO(this.portfolioCardCrudProxy.read("name").getPortfolioCards().get(0))
			.build();
		
		// when
		String content = objectMapper.writeValueAsString(editUser);
		given(tokenValidator.validateToken((TokenAble)editUser)).willReturn(true);
		
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
	public void preInit(){
		given(tokenValidator.validateToken(any(TokenAble.class))).willReturn(true);
		given(tokenValidator.validateToken(any(String.class))).willReturn(true);
		this.userInfoCrudProxy = this.userInfoCrudFactory.get();
		this.portfolioCardCrudProxy = this.portfolioCardCrudFactory.get();
		UserDTO user = this.getUser();
		try{
			this.userInfoCrudProxy.delete(user.getName());
		} catch(NonExistUserException NEUE){}
		try{
			this.userInfoCrudProxy.create(user);
			this.portfolioCardCrudProxy.create(user);
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
		// try{
		// 	this.userInfoCrudProxy.create(user);
		// 	this.portfolioCardCrudProxy.create(user);
		// }catch(DuplicationUserException DUE){DUE.printStackTrace();}
	}
	
	private UserDTO getUser(){
		UserDTO user = new UserDTO.Builder()
			.id(0L)
			.name("name")
			.profileUrl("https://example.profileUrl.com?pct")
			.portfolioCardDTO(this.getPortfolioCard(0L, "Lorem ipsum", 0, "https://api.gitofolio.com/portfolio/name/1"))
			.portfolioCardDTO(this.getPortfolioCard(1L, "Lorem ipsum", 0, "https://api.gitofolio.com/portfolio/name/2"))
			.portfolioCardDTO(this.getPortfolioCard(2L, "Lorem ipsum", 0, "https://api.gitofolio.com/portfolio/name/3"))
			.portfolioCardDTO(this.getPortfolioCard(3L, "Lorem ipsum", 0, "https://api.gitofolio.com/portfolio/name/4"))
			.portfolioCardDTO(this.getPortfolioCard(4L, "Lorem ipsum", 0, "https://api.gitofolio.com/portfolio/name/5"))
			.build();
		
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