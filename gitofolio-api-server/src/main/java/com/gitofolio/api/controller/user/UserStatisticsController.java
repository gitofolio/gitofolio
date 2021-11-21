package com.gitofolio.api.controller.user;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.factory.UserFactory;
import com.gitofolio.api.service.user.eraser.UserEraser;
import com.gitofolio.api.service.user.exception.InvalidHttpMethodException;

@RestController
@RequestMapping(path="/user/dailystat")
public class UserStatisticsController{
	
	@Autowired
	@Qualifier("userStatisticsFactory")
	private UserFactory userStatisticsFactory;
	
	@Autowired
	@Qualifier("userStatisticsEraser")
	private UserEraser userStatisticsEraser;
	
	@RequestMapping(path="/{name}", method=RequestMethod.GET)
	public ResponseEntity<UserDTO> getUserStatistics(@PathVariable("name") String name){
		
		UserDTO userDTO = this.userStatisticsFactory.getUser(name);
		
		return new ResponseEntity(userDTO, HttpStatus.OK);
	}
	
	@RequestMapping(path={"*", "**", ""}, method=RequestMethod.POST)
	public ResponseEntity<UserDTO> saveUserStatistics(){
		
		throw new InvalidHttpMethodException("허용되지않은 HTTP METHOD 입니다.", "user/dailystat URI에는 GET 메소드만 사용 가능합니다.", "POST : user/dailystat");
	}
	
	@RequestMapping(path={"*", "**", ""}, method=RequestMethod.DELETE)
	public ResponseEntity<UserDTO> deleteUserStatistics(){
		
		throw new InvalidHttpMethodException("허용되지않은 HTTP METHOD 입니다.", "user/dailystat URI에는 GET 메소드만 사용 가능합니다.", "DELETE : user/dailystat");
	}
	
}