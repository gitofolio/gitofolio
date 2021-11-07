package com.gitofolio.api.controller.user;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.factory.UserFactory;

@RestController
@RequestMapping(path="/user/statistics")
public class UserStatisticsController{
	
	@Autowired
	@Qualifier("userStatisticsFactory")
	private UserFactory userStatisticsFactory;
	
	@RequestMapping(path="/{name}", method=RequestMethod.GET)
	public UserDTO getUserStatistics(@PathVariable("name") String name){
		
		UserDTO userDTO = userStatisticsFactory.getUser(name);
		
		return userDTO;
	}
	
}