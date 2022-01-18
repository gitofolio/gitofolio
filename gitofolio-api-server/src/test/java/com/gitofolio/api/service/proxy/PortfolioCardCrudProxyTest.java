package com.gitofolio.api.service.proxy;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.gitofolio.api.service.factory.CrudFactory;
import com.gitofolio.api.service.user.dtos.*;
import com.gitofolio.api.service.user.exception.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PortfolioCardCrudProxyTest{
	
	private CrudProxy<UserDTO> userInfoCrudProxy;
	private CrudProxy<UserDTO> portfolioCardCrudProxy;
	
	@Test
	public void portfolioCardCrudProxy_read_string_test(){
		// given
		String name = "name";
			
		// when
		UserDTO user = this.portfolioCardCrudProxy.read(name);
		UserDTO originUser = this.getUser();
		
		// then
		assertEquals(user.getName(), originUser.getName());
		assertEquals(user.getPortfolioCards().size(), originUser.getPortfolioCards().size());
	}
	
	@Test
	public void portfolioCardCrudProxy_read_long_test(){
		// given
		Long id = this.portfolioCardCrudProxy.read("name").getPortfolioCards().get(0).getId();
		
		// when
		UserDTO user = this.portfolioCardCrudProxy.read(id);
		
		// then
		assertEquals(user.getName(), "name");
		assertEquals(user.getPortfolioCards().size(), 1);
	}
	
	@Test
	public void portfolioCardCrudProxy_create_test(){
		// given
		PortfolioCardDTO portfolioCardDTO = this.getPortfolioCard(5L, "new card article", 0, "url");
		UserDTO user = new UserDTO.Builder()
			.id(0L)
			.name("name")
			.profileUrl("-")
			.portfolioCardDTO(portfolioCardDTO)
			.build();
		
		// when
		UserDTO ans = this.portfolioCardCrudProxy.create(user);
		
		// then
		assertEquals(ans.getName(), "name");
		assertEquals(ans.getPortfolioCards().get(5).getPortfolioCardArticle(), "new card article");
	}
	
	@Test
	public void portfolioCardCrudProxy_delete_test(){
		// given
		String name = "name";
		
		// when
		this.portfolioCardCrudProxy.delete(name);
		
		// then
		assertThrows(NonExistUserException.class, ()->this.portfolioCardCrudProxy.read(name));
	}
	
	@Test
	public void portfolioCardCrudProxy_delete_long_test(){
		// given
		String name = "name";
		Long id = this.portfolioCardCrudProxy.read(name).getPortfolioCards().get(0).getId();
		
		// when
		this.portfolioCardCrudProxy.delete(name, id);
		UserDTO user = this.portfolioCardCrudProxy.read(name);
		
		// then
		assertEquals(user.getPortfolioCards().size(), 4);
	}
	
	@Test
	public void portfolioCardCrudProxy_update_test(){
		// given
		Long id = this.portfolioCardCrudProxy.read("name").getPortfolioCards().get(0).getId();
		PortfolioCardDTO updateCard = this.getPortfolioCard(id, "Updated article", 0, "Updated url");
		
		UserDTO updateUser = new UserDTO.Builder()
			.id(0L)
			.name("name")
			.profileUrl("-")
			.portfolioCardDTO(updateCard)
			.build();
		
		// when
		UserDTO ans = this.portfolioCardCrudProxy.update(updateUser);
		
		// then
		assertEquals(ans.getPortfolioCards().size(), 5);
		assertEquals(ans.getPortfolioCards().get(0).getPortfolioCardArticle(), "Updated article");
		assertEquals(ans.getPortfolioCards().get(0).getPortfolioUrl(), "Updated url");
	}
	
	@Autowired
	public PortfolioCardCrudProxyTest(@Qualifier("portfolioCardCrudFactory") CrudFactory<UserDTO> portfolioCardCrudFactory,
								@Qualifier("userInfoCrudFactory") CrudFactory<UserDTO> userInfoCrudFactory){
		this.portfolioCardCrudProxy = portfolioCardCrudFactory.get();
		this.userInfoCrudProxy = userInfoCrudFactory.get();
	}
	
	
	@BeforeEach
	public void preInit(){
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
		try{
		}catch(DuplicationUserException DUE){DUE.printStackTrace();}
	}
	
	private UserDTO getUser(){
		UserDTO user = new UserDTO.Builder()
			.id(0L)
			.name("name")
			.profileUrl("https://example.profileUrl.com")
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