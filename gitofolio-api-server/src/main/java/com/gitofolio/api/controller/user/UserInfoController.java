package com.gitofolio.api.controller.user;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.factory.UserFactory;

@RestController
@RequestMapping(path="/user")
public class UserInfoController {
	
	@Autowired
	@Qualifier("userInfoFactory")
	private UserFactory userInfoFactory;
	
	@RequestMapping(path="/{name}", method=RequestMethod.GET)
	public UserDTO getUser(@PathVariable("name") String name){
		
		UserDTO userDTO = userInfoFactory.getUser(name);
		
		return userDTO;
	}
	
}