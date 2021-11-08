package com.gitofolio.api.controller.user;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.factory.UserFactory;

@RestController
@RequestMapping(path="/user/stat")
public class UserStatController{
	
	@Autowired
	@Qualifier("userStatFactory")
	private UserFactory userStatFactory;
	
	@RequestMapping(path="/{name}", method=RequestMethod.GET)
	public UserDTO getUserStat(@PathVariable("name") String name){
		
		UserDTO userDTO = userStatFactory.getUser(name);
		
		return userDTO;
	}
	
	@RequestMapping(path="/{name}", method=RequestMethod.POST)
	public UserDTO saveUserStat(@RequestBody UserDTO userDTO){
		
		return this.userStatFactory.saveUser(userDTO);
	
	}
	
	
}