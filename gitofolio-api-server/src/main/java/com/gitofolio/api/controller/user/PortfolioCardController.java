package com.gitofolio.api.controller.user;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.factory.UserFactory;
import com.gitofolio.api.service.user.eraser.UserEraser;
import com.gitofolio.api.service.user.parameter.ParameterHandler;

@RestController
@RequestMapping(path="/portfoliocards")
public class PortfolioCardController{
	
	@Autowired
	@Qualifier("portfolioCardFactory")
	private UserFactory portfolioCardFactory;
	
	@Autowired
	@Qualifier("portfolioCardEraser")
	private UserEraser portfolioCardEraser;
	
	@RequestMapping(path="/{name}", method=RequestMethod.GET)
	public ResponseEntity<UserDTO> getPortfolioCard(
		@PathVariable("name") String name,
		@RequestParam(value="cards", defaultValue="1,5") String cards){
		
		UserDTO userDTO = null;
		if(cards == null) userDTO = portfolioCardFactory.getUser(name);
		else userDTO = portfolioCardFactory.getUser(name, cards);
			
		return new ResponseEntity(userDTO, HttpStatus.OK);
	}
	
	@RequestMapping(path="", method=RequestMethod.POST)
	public ResponseEntity<UserDTO> savePortfolioCard(@RequestBody UserDTO userDTO){
		
		UserDTO result = this.portfolioCardFactory.saveUser(userDTO);
		
		return new ResponseEntity(result, HttpStatus.CREATED);
	}
	
	@RequestMapping(path="/{name}", method=RequestMethod.DELETE)
	public ResponseEntity<UserDTO> deletePortfolioCard(@PathVariable("name") String name,
													  @RequestParam(value="card", required=false) String card){
		
		String result = null;
		
		if(card == null) result = this.portfolioCardEraser.delete(name);
		else result = this.portfolioCardEraser.delete(name, card);
		
		return new ResponseEntity(result, HttpStatus.OK);
	}
	
	@RequestMapping(path="", method=RequestMethod.PUT)
	public ResponseEntity<UserDTO> putPortfolioCard(@RequestBody UserDTO userDTO,
												   @RequestParam(value="card", required=true) Integer card){
		UserDTO result = this.portfolioCardFactory.editUser(userDTO, card);
		
		return new ResponseEntity(result, HttpStatus.OK);
	}
	
}