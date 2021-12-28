package com.gitofolio.api.controller.user;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.proxy.UserProxy;
import com.gitofolio.api.service.user.eraser.UserEraser;
import com.gitofolio.api.service.user.parameter.ParameterHandler;
import com.gitofolio.api.aop.auth.annotation.UserAuthorizationer;
import com.gitofolio.api.service.user.svg.portfoliocard.PortfolioCardSvgDTO;
import com.gitofolio.api.service.user.svg.portfoliocard.PortfolioCardSvgFactory;
import com.gitofolio.api.service.user.proxy.EncodedProfileImageProxy;

@RestController
@RequestMapping
public class PortfolioCardController{
	
	@Autowired
	@Qualifier("portfolioCardProxy")
	private UserProxy portfolioCardProxy;
	
	@Autowired
	@Qualifier("portfolioCardEraser")
	private UserEraser portfolioCardEraser;
	
	@Autowired
	private PortfolioCardSvgFactory portfolioCardSvgFactory;
	
	@Autowired
	private EncodedProfileImageProxy encodedProfileImageProxy;
	
	@RequestMapping(path="/portfoliocards/{name}", method=RequestMethod.GET)
	public ResponseEntity<UserDTO> getPortfolioCard(
		@PathVariable("name") String name,
		@RequestParam(value="cards", required = false) String cards){
		
		UserDTO userDTO = null;
		if(cards == null) userDTO = portfolioCardProxy.getUser(name);
		else userDTO = portfolioCardProxy.getUser(name, cards);
			
		return new ResponseEntity(userDTO, HttpStatus.OK);
	}
	
	// @UserAuthorizationer(idx=0)
	@RequestMapping(path="/portfoliocards", method=RequestMethod.POST)
	public ResponseEntity<UserDTO> savePortfolioCard(@RequestBody UserDTO userDTO){
		
		UserDTO result = this.portfolioCardProxy.saveUser(userDTO);
		
		return new ResponseEntity(result, HttpStatus.CREATED);
	}
	
	// @UserAuthorizationer(idx=0)
	@RequestMapping(path="/portfoliocards/{name}", method=RequestMethod.DELETE)
	public ResponseEntity<UserDTO> deletePortfolioCard(@PathVariable("name") String name,
													  @RequestParam(value="card", required=false) String card){
		
		String result = null;
		
		if(card == null) result = this.portfolioCardEraser.delete(name);
		else result = this.portfolioCardEraser.delete(name, card);
		
		return new ResponseEntity(result, HttpStatus.OK);
	}
	
	// @UserAuthorizationer(idx=0)
	@RequestMapping(path="/portfoliocards", method=RequestMethod.PUT)
	public ResponseEntity<UserDTO> putPortfolioCard(@RequestBody UserDTO userDTO){
		
		UserDTO result = this.portfolioCardProxy.editUser(userDTO);
		
		return new ResponseEntity(result, HttpStatus.OK);
	}
	
	@RequestMapping(path="/portfoliocard/svg/{cardId}", method=RequestMethod.GET)
	public ModelAndView getPortfolioCardSvg(@PathVariable("cardId") Long cardId, 
											@RequestParam(value="color", defaultValue="white") String color){
		
		UserDTO userDTO = this.portfolioCardProxy.getUser(cardId);
		String encodedImage = this.encodedProfileImageProxy.get(userDTO);
		PortfolioCardSvgDTO svgDTO = this.portfolioCardSvgFactory.getPortfolioCardSvgDTO(userDTO.getName(), 
																						 encodedImage,
																						 userDTO.getPortfolioCards().get(0).getPortfolioUrl(),
																						 userDTO.getPortfolioCards().get(0).getPortfolioCardStars(), 
																						 userDTO.getPortfolioCards().get(0).getPortfolioCardArticle(),
																						 color);
		
		return new ModelAndView("portfolioCard", "svgDTO", svgDTO);
	}
}