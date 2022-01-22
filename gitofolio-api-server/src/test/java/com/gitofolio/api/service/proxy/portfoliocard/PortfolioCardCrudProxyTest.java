package com.gitofolio.api.service.proxy.portfoliocard;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;

import com.gitofolio.api.service.proxy.CrudProxy;
import com.gitofolio.api.service.factory.mapper.UserMapper;
import com.gitofolio.api.service.user.*;
import com.gitofolio.api.service.user.dtos.*;

@ExtendWith(MockitoExtension.class)
@Transactional
public class PortfolioCardCrudProxyTest{
	
	@Mock
	@Qualifier("portfolioCardMapper")
	private UserMapper<List<PortfolioCard>> portfolioCardMapper;
	
	@Mock
	private PortfolioCardService portfolioCardService;
	
	@Mock
	private CrudProxy<UserDTO> crudProxy;
	
	@InjectMocks
	@Qualifier("portfolioCardCrudProxy")
	private CrudProxy<UserDTO> portfolioCardCrudProxy;
	
	@InjectMocks
	@Qualifier("portfolioCardLongCrudProxy")
	private CrudProxy<UserDTO> portfolioCardLongCrudProxy;
	
	@InjectMocks
	@Qualifier("portfolioCardStringCrudProxy")
	private CrudProxy<UserDTO> portfolioCardStringCrudProxy;
	
	@InjectMocks
	@Qualifier("portfolioCardStringLongCrudProxy")
	private CrudProxy<UserDTO> portfolioCardStringLongCrudProxy;
	
	@Test
	public void portfolioCardCrudProxy_Save_Test(){
		// given
		UserDTO userDTO = this.getUserDTO();
		UserInfo userInfo = this.getUserInfo();
		
		// when
		given(this.portfolioCardService.save(any(List.class))).willReturn(this.getPortfolioCard(1));
		given(this.portfolioCardMapper.resolveMap(any(UserDTO.class))).willReturn(Arrays.asList(this.getPortfolioCard(1)))
		
			
		UserDTO result = this.portfolioCardCrudProxy.create(userDTO);
		
		// then
	}
	
	private UserDTO getUserDTO(){
		return new UserDTO.Builder()
			.userInfo(this.getUserInfo())
			.build();
	}
	
	
	
	private PortfolioCardDTO getPortfolioCardDTO(){
		return new PortfolioCardDTO.Builder()
			.this.getPortfolioCard(1)
			.build();
	}
	
	private PortfolioCard getPortfolioCard(int cardCnt){
		PortfolioCard portfolioCard = new PortfolioCard();
		portfolioCard.setId(Long.valueOf(cardCnt));
		portfolioCard.setPortfolioCardArticle("article"+cardCnt);
		portfolioCard.setPortfolioCardStars(cardCnt);
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