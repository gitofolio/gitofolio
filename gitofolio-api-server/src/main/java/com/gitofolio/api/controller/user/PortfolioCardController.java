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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.factory.UserFactory;
import com.gitofolio.api.service.user.eraser.UserEraser;

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
	@ApiOperation(value="유저 포트폴리오 카드 가져오기", notes="유저의 포트폴리오 카드들을 가져옴. 없다면, 빈 배열 반환")
	public ResponseEntity<UserDTO> getPortfolioCard(
		@PathVariable("name") String name,
		@RequestParam(value="cards", required=false) String cards){
		
		UserDTO userDTO = portfolioCardFactory.getUser(name);
		
		return new ResponseEntity(userDTO, HttpStatus.OK);
	}
	
	@RequestMapping(path="", method=RequestMethod.POST)
	@ApiOperation(value="유저 포트폴리오 카드 저장", notes="포트폴리오 카드 저장 타겟 User는 HttpBody에 name에 적음")
	public ResponseEntity<UserDTO> savePortfolioCard(@RequestBody UserDTO userDTO){
		
		UserDTO result = this.portfolioCardFactory.saveUser(userDTO);
		
		return new ResponseEntity(result, HttpStatus.CREATED);
		
	}
	
	@RequestMapping(path="/{name}", method=RequestMethod.DELETE)
	@ApiOperation(value="유저 포트폴리오카드 삭제", notes="유저 포트폴리오카드 삭제 name에 해당하는 모든 포트폴리오 카드를 삭제함")
	public ResponseEntity<UserDTO> deletePortfolioCard(@PathVariable("name") String name){
		
		this.portfolioCardEraser.delete(name);
		
		return new ResponseEntity(HttpStatus.OK);
	}
	
}