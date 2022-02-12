package com.gitofolio.api.controller.endpoint;

import org.springframework.http.MediaType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.*;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.exception.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public class EndPointControllerTest{
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void EndPoint_GET_TEST() throws Exception{
		// given
		String url = "/";
		
		// then
		mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("endpoint/get",
							relaxedResponseFields(
								fieldWithPath("links.[].rel").description("선택가능한 다음 선택지에 대한 key 입니다."),
								fieldWithPath("links.[].method").description("HTTP METHOD"),
								fieldWithPath("links.[].href").description("다음 선택지 요청 URL 입니다.")
							)
						)
					);
	}
		
	@Test
	public void TodayInteraction_GET_Test() throws Exception{
		// given
		String url = "/todayinteraction";

		// then
		mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("endpoint/todayinteraction",
						relaxedResponseFields(
							fieldWithPath("info.interact").description("오늘 애플리케이션이 유저와 상호작용한 횟수 입니다."),
							fieldWithPath("links.[].rel").description("선택가능한 다음 선택지에 대한 key 입니다."), 
							fieldWithPath("links.[].method").description("HTTP METHOD"),
							fieldWithPath("links.[].href").description("다음 선택지 요청 URL 입니다.")
						)
					)
				);
	}

}
