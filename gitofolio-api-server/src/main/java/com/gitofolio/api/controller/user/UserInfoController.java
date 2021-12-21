package com.gitofolio.api.controller.user;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.factory.UserFactory;
import com.gitofolio.api.service.user.eraser.UserEraser;
import com.gitofolio.api.service.user.exception.InvalidHttpMethodException;
import com.gitofolio.api.service.auth.exception.AuthenticateException;
import com.gitofolio.api.service.auth.SessionProcessor;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path="/user")
public class UserInfoController {
	
	@Autowired
	@Qualifier("userInfoFactory")
	private UserFactory userInfoFactory;
	
	@Autowired
	@Qualifier("userInfoEraser")
	private UserEraser userInfoEraser;
	
	@Autowired
	@Qualifier("loginSessionProcessor")
	private SessionProcessor<UserDTO> loginSessionProcessor;
	
	@RequestMapping(path="", method=RequestMethod.GET)
	public ResponseEntity<UserDTO> getLoginedUser(HttpSession httpSession){
		
		UserDTO userDTO = this.loginSessionProcessor.getAttribute(httpSession).orElseThrow(()->new AuthenticateException("인증 오류", "로그인 되어있는 유저가 없습니다."));

		return new ResponseEntity(userDTO, HttpStatus.OK);
	}
	
	@RequestMapping(path="/{name}", method=RequestMethod.GET)
	public ResponseEntity<UserDTO> getUser(@PathVariable("name") String name){
		
		UserDTO userDTO = this.userInfoFactory.getUser(name);
		
		return new ResponseEntity(userDTO, HttpStatus.OK);
	}
	
	@RequestMapping(path="", method=RequestMethod.POST)
	public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO){

		throw new InvalidHttpMethodException("허용되지않은 HTTP METHOD 입니다.", "user 의 POST메소드는 허용되지 않았습니다.", "POST : user/dailystat");
		
	}
	
	@RequestMapping(path="/{name}", method=RequestMethod.DELETE)
	public ResponseEntity<UserDTO> deleteUser(@PathVariable("name") String name){
		
		String result = this.userInfoEraser.delete(name);
		
		return new ResponseEntity(result, HttpStatus.OK);
	}
	
	@RequestMapping(path="", method=RequestMethod.PUT)
	public ResponseEntity<UserDTO> putUser(@RequestBody UserDTO userDTO){
		
		throw new InvalidHttpMethodException("허용되지않은 HTTP METHOD 입니다.", "user 의 PUT메소드는 허용되지 않았습니다.", "PUT : user/dailystat");
		
	}
}