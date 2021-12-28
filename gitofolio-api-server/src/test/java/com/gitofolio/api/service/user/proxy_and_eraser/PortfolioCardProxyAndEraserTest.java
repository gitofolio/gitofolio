package com.gitofolio.api.service.user.proxy;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import com.gitofolio.api.service.user.proxy.UserProxy;
import com.gitofolio.api.service.user.eraser.UserEraser;
import com.gitofolio.api.service.user.dtos.*;
import com.gitofolio.api.domain.user.*;
import com.gitofolio.api.service.user.exception.*;

import java.util.List;
import java.util.ArrayList;

@SpringBootTest
public class PortfolioCardProxyAndEraserTest{
	
	@Autowired
	@Qualifier("userInfoProxy")
	private UserProxy userInfoProxy;
	
	@Autowired
	@Qualifier("portfolioCardProxy")
	private UserProxy portfolioCardProxy;
	
	@Autowired
	@Qualifier("userInfoEraser")
	private UserEraser userInfoEraser;
	
	@Autowired
	@Qualifier("portfolioCardEraser")
	private UserEraser portfolioCardEraser;
	
	String name = "testName";
	String url = "testProfileUrl";
	
	@Test
	public void PortfolioCardProxy_Save_and_Get_Test(){
		// given
		PortfolioCardDTO portfolioCardDTO1 = new PortfolioCardDTO.Builder()
			.id(3L)
			.portfolioCardArticle("p1")
			.portfolioCardStars(1)
			.portfolioUrl("pu1")
			.build();
		
		PortfolioCardDTO portfolioCardDTO2 = new PortfolioCardDTO.Builder()
			.id(2L)
			.portfolioCardArticle("p2")
			.portfolioCardStars(2)
			.portfolioUrl("pu2")
			.build();
		
		PortfolioCardDTO portfolioCardDTO3 = new PortfolioCardDTO.Builder()
			.id(1L)
			.portfolioCardArticle("p3")
			.portfolioCardStars(3)
			.portfolioUrl("pu3")
			.build();
		
		UserDTO userDTO = new UserDTO.Builder()
			.name(this.name)
			.id(0L)
			.profileUrl(this.url)
			.portfolioCardDTO(portfolioCardDTO1)
			.portfolioCardDTO(portfolioCardDTO2)
			.portfolioCardDTO(portfolioCardDTO3)
			.build();
		
		// when
		userInfoProxy.saveUser(userDTO);
		portfolioCardProxy.saveUser(userDTO);
		
		// then
		UserDTO ans = this.portfolioCardProxy.getUser(this.name);
		List<PortfolioCardDTO> cardDTOs = ans.getPortfolioCards();
		
		assertEquals(ans.getName(), this.name);
		assertEquals(ans.getProfileUrl(), this.url);
		assertEquals(cardDTOs.size(), 3);
		assertEquals(cardDTOs.get(2).getPortfolioCardArticle(), "p3");
	}
	
	@Test
	public void PortfolioCardProxy_Edit_Test(){
		// given
		PortfolioCardDTO portfolioCardDTO1 = new PortfolioCardDTO.Builder()
			.id(3L)
			.portfolioCardArticle("p1")
			.portfolioCardStars(1)
			.portfolioUrl("pu1")
			.build();
		
		PortfolioCardDTO portfolioCardDTO2 = new PortfolioCardDTO.Builder()
			.id(2L)
			.portfolioCardArticle("p2")
			.portfolioCardStars(2)
			.portfolioUrl("pu2")
			.build();
		
		PortfolioCardDTO portfolioCardDTO3 = new PortfolioCardDTO.Builder()
			.id(1L)
			.portfolioCardArticle("p3")
			.portfolioCardStars(1)
			.portfolioUrl("pu3")
			.build();
		
		UserDTO userDTO = new UserDTO.Builder()
			.id(0L)
			.name(this.name)
			.profileUrl(this.url)
			.portfolioCardDTO(portfolioCardDTO1)
			.portfolioCardDTO(portfolioCardDTO2)
			.portfolioCardDTO(portfolioCardDTO3)
			.build();
		
		// when 
		userInfoProxy.saveUser(userDTO);
		portfolioCardProxy.saveUser(userDTO);
		
		PortfolioCardDTO editPortfolioCardDTO = new PortfolioCardDTO.Builder()
			.id(this.portfolioCardProxy.getUser(this.name).getPortfolioCards().get(0).getId())
			.portfolioCardArticle("edit")
			.portfolioCardStars(2)
			.portfolioUrl("edit")
			.build();
		
		UserDTO editUserDTO = new UserDTO.Builder()
			.id(0L)
			.name(this.name)
			.profileUrl(this.url)
			.portfolioCardDTO(editPortfolioCardDTO)
			.build();
		
		portfolioCardProxy.editUser(editUserDTO);
		
		// then
		UserDTO ans = this.portfolioCardProxy.getUser(this.name);
		List<PortfolioCardDTO> cardDTOs = ans.getPortfolioCards();
		
		assertEquals(ans.getName(), this.name);
		assertEquals(ans.getProfileUrl(), this.url);
		assertEquals(cardDTOs.get(0).getPortfolioCardArticle(), "edit");
		assertEquals(cardDTOs.get(0).getPortfolioUrl(), "edit");
		assertEquals(cardDTOs.get(0).getPortfolioCardStars(), 1);
	}
	
	@AfterEach
	public void pre_PortfolioCardEraser_Delete_Test(){
		try{
			this.portfolioCardEraser.delete(this.name);
			this.userInfoEraser.delete(this.name);
		}catch(NonExistUserException NUE){}
		
		assertThrows(NonExistUserException.class, ()->portfolioCardProxy.getUser(this.name));
	}
	
	@BeforeEach
	public void post_PortfolioCardEraser_Delete_Test(){
		try{
			this.portfolioCardEraser.delete(this.name);
			this.userInfoEraser.delete(this.name);
		}catch(NonExistUserException NUE){}
		
		assertThrows(NonExistUserException.class, ()->portfolioCardProxy.getUser(this.name));
	}
}