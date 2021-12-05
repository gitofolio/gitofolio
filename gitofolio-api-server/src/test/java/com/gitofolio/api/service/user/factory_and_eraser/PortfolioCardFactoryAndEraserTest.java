package com.gitofolio.api.service.user.factory;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import com.gitofolio.api.service.user.factory.UserFactory;
import com.gitofolio.api.service.user.eraser.UserEraser;
import com.gitofolio.api.service.user.dtos.*;
import com.gitofolio.api.domain.user.*;
import com.gitofolio.api.service.user.exception.*;

import java.util.List;
import java.util.ArrayList;

@SpringBootTest
public class PortfolioCardFactoryAndEraserTest{
	
	@Autowired
	@Qualifier("userInfoFactory")
	private UserFactory userInfoFactory;
	
	@Autowired
	@Qualifier("portfolioCardFactory")
	private UserFactory portfolioCardFactory;
	
	@Autowired
	@Qualifier("userInfoEraser")
	private UserEraser userInfoEraser;
	
	@Autowired
	@Qualifier("portfolioCardEraser")
	private UserEraser portfolioCardEraser;
	
	String name = "testName";
	String url = "testProfileUrl";
	
	@Test
	public void PortfolioCardFactory_Save_and_Get_Test(){
		// given
		PortfolioCardDTO portfolioCardDTO1 = new PortfolioCardDTO.Builder()
			.portfolioCardArticle("p1")
			.portfolioCardStars(1)
			.portfolioUrl("pu1")
			.build();
		
		PortfolioCardDTO portfolioCardDTO2 = new PortfolioCardDTO.Builder()
			.portfolioCardArticle("p2")
			.portfolioCardStars(2)
			.portfolioUrl("pu2")
			.build();
		
		PortfolioCardDTO portfolioCardDTO3 = new PortfolioCardDTO.Builder()
			.portfolioCardArticle("p3")
			.portfolioCardStars(3)
			.portfolioUrl("pu3")
			.build();
		
		UserDTO userDTO = new UserDTO.Builder()
			.name(this.name)
			.profileUrl(this.url)
			.portfolioCardDTO(portfolioCardDTO1)
			.portfolioCardDTO(portfolioCardDTO2)
			.portfolioCardDTO(portfolioCardDTO3)
			.build();
		
		// when
		userInfoFactory.saveUser(userDTO);
		portfolioCardFactory.saveUser(userDTO);
		
		// then
		UserDTO ans = this.portfolioCardFactory.getUser(this.name);
		List<PortfolioCardDTO> cardDTOs = ans.getPortfolioCards();
		
		assertEquals(ans.getName(), this.name);
		assertEquals(ans.getProfileUrl(), this.url);
		assertEquals(cardDTOs.size(), 3);
		assertEquals(cardDTOs.get(2).getPortfolioCardArticle(), "p3");
	}
	
	@AfterEach
	public void pre_PortfolioCardEraser_Delete_Test(){
		try{
			this.portfolioCardEraser.delete(this.name);
		}catch(NonExistUserException NUE){}
		
		assertThrows(NonExistUserException.class, ()->portfolioCardFactory.getUser(this.name));
	}
	
	@BeforeEach
	public void post_PortfolioCardEraser_Delete_Test(){
		try{
			this.portfolioCardEraser.delete(this.name);
		}catch(NonExistUserException NUE){}
		
		assertThrows(NonExistUserException.class, ()->portfolioCardFactory.getUser(this.name));
	}
}