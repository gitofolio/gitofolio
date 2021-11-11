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

@RestController
@RequestMapping(path="/user")
public class UserInfoController {
	
	@Autowired
	@Qualifier("userInfoFactory")
	private UserFactory userInfoFactory;
	
	@Autowired
	@Qualifier("userInfoEraser")
	private UserEraser userInfoEraser;
	
	@RequestMapping(path="/{name}", method=RequestMethod.GET)
	public ResponseEntity<UserDTO> getUser(@PathVariable("name") String name){
		
		UserDTO userDTO = this.userInfoFactory.getUser(name);
		
		return new ResponseEntity(userDTO, HttpStatus.OK);
	}
	
	@RequestMapping(path="/", method=RequestMethod.POST)
	public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO){

		UserDTO result = this.userInfoFactory.saveUser(userDTO);
		
		return new ResponseEntity(result, HttpStatus.CREATED);
		
	}
	
	@RequestMapping(path="/{name}", method=RequestMethod.DELETE)
	public ResponseEntity<UserDTO> deleteUser(@PathVariable("name") String name){
		
		this.userInfoEraser.deleteUser(name);
		
		return new ResponseEntity(HttpStatus.OK);
	}
	
}