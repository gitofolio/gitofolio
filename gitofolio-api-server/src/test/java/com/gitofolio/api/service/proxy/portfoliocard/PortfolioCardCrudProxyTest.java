package com.gitofolio.api.service.proxy.portfoliocard;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;

import com.gitofolio.api.service.proxy.portfoliocard.*;
import com.gitofolio.api.service.factory.mapper.UserMapper;
import com.gitofolio.api.service.user.*;
import com.gitofolio.api.service.user.dtos.*;
import com.gitofolio.api.domain.user.*;

import java.util.List;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
@Transactional
public class PortfolioCardCrudProxyTest{
	
	@Mock
	@Qualifier("portfolioCardMapper")
	private UserMapper<List<PortfolioCard>> portfolioCardMapper;
	
	@Mock
	private PortfolioCardService portfolioCardService;
	
	@InjectMocks
	private PortfolioCardCrudProxy portfolioCardCrudProxy;
	
	@InjectMocks
	private PortfolioCardLongCrudProxy portfolioCardLongCrudProxy;
	
	@InjectMocks
	private PortfolioCardStringCrudProxy portfolioCardStringCrudProxy;
	
	@InjectMocks
	private PortfolioCardStringLongCrudProxy portfolioCardStringLongCrudProxy;
	
	@BeforeEach
	public void setUpCrudProxy(){
		this.portfolioCardCrudProxy.addProxy(portfolioCardLongCrudProxy);
		this.portfolioCardCrudProxy.addProxy(portfolioCardStringCrudProxy);
		this.portfolioCardCrudProxy.addProxy(portfolioCardStringLongCrudProxy);
	}
	
	@Test
	public void portfolioCardCrudProxy_create_Test(){
		// given
		UserDTO userDTO = this.getUserDTO();
		
		// when
		given(this.portfolioCardMapper.resolveMap(any(UserDTO.class))).willReturn(Arrays.asList(this.getPortfolioCard(1)));
		given(this.portfolioCardMapper.doMap(any(List.class))).willReturn(userDTO);
		given(this.portfolioCardService.save(any(List.class))).willReturn(Arrays.asList(this.getPortfolioCard(1)));
		
		UserDTO result = this.portfolioCardCrudProxy.create(userDTO);
		
		// then
		assertEquals(result.getName(), userDTO.getName());
	}
	
	@Test
	public void portfolioCardCrudProxy_update_Test(){
		// given
		UserDTO userDTO = this.getUserDTO();
		
		// when
		given(this.portfolioCardMapper.resolveMap(any(UserDTO.class))).willReturn(Arrays.asList(this.getPortfolioCard(1)));
		given(this.portfolioCardMapper.doMap(any(List.class))).willReturn(userDTO);
		given(this.portfolioCardService.edit(any(List.class))).willReturn(Arrays.asList(this.getPortfolioCard(1)));
		
		UserDTO result = this.portfolioCardCrudProxy.update(userDTO);
		
		// then
		assertEquals(result.getName(), userDTO.getName());
	}
	
	@Test
	public void portfolioCardCrudProxy_Long_read_Test(){
		// given
		UserDTO userDTO = this.getUserDTO();
		
		// when
		given(this.portfolioCardService.get(any(Long.class))).willReturn(Arrays.asList(this.getPortfolioCard(1)));
		given(this.portfolioCardMapper.doMap(any(List.class))).willReturn(userDTO);
		
		UserDTO result = this.portfolioCardCrudProxy.read(userDTO.getId());
		
		// then
		assertEquals(result.getId(), userDTO.getId());
	}
	
	@Test
	public void portfolioCardCrudProxy_String_read_Test(){
		// given
		UserDTO userDTO = this.getUserDTO();
		
		// when
		given(this.portfolioCardService.get(any(String.class))).willReturn(Arrays.asList(this.getPortfolioCard(1)));
		given(this.portfolioCardMapper.doMap(any(List.class))).willReturn(userDTO);
		
		UserDTO result = this.portfolioCardCrudProxy.read(userDTO.getName());
		
		// then
		assertEquals(result.getName(), userDTO.getName());
	}
	
	private UserDTO getUserDTO(){
		return new UserDTO.Builder()
			.userInfo(this.getUserInfo())
			.portfolioCardDTO(this.getPortfolioCardDTO())
			.build();
	}
	
	private PortfolioCardDTO getPortfolioCardDTO(){
		return new PortfolioCardDTO.Builder()
			.portfolioCard(this.getPortfolioCard(1))
			.build();
	}
	
	private PortfolioCard getPortfolioCard(int cardCnt){
		PortfolioCard portfolioCard = new PortfolioCard();
		portfolioCard.setId(Long.valueOf(cardCnt));
		portfolioCard.setPortfolioCardArticle("article"+cardCnt);
		portfolioCard.setPortfolioCardWatched(cardCnt);
		portfolioCard.setPortfolioUrl("portfolioUrl"+cardCnt);
		
		portfolioCard.setUserInfo(this.getUserInfo());
		return portfolioCard;
	}
	
	private UserInfo getUserInfo(){
		UserInfo userInfo = new UserInfo();
		userInfo.setId(0L);
		userInfo.setName("name");
		userInfo.setProfileUrl("url.helloworld.com");
		return userInfo;
	}
	
}