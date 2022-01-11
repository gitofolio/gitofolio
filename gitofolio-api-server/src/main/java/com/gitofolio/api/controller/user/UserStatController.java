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
import com.gitofolio.api.service.user.proxy.CrudProxy;
import com.gitofolio.api.service.user.factory.CrudFactory;
import com.gitofolio.api.aop.hateoas.annotation.HateoasSetter;
import com.gitofolio.api.aop.hateoas.annotation.HateoasType;
import com.gitofolio.api.aop.log.time.annotation.ExpectedTime;

@RestController
@RequestMapping(path="/user/stat")
public class UserStatController{
	
	private final CrudProxy<UserDTO> userStatCrudProxy;
	
	@ExpectedTime
	@HateoasSetter(hateoasType=HateoasType.USERSTATHATEOAS)
	@RequestMapping(path="/{name}", method=RequestMethod.GET)
	public ResponseEntity<UserDTO> getUserStat(@PathVariable("name") String name){
		
		UserDTO userDTO = userStatCrudProxy.read(name);
		
		return new ResponseEntity(userDTO, HttpStatus.OK);
	}
	
	@Autowired
	public UserStatController(@Qualifier("userStatCrudFactory") CrudFactory<UserDTO> userStatCrudFactory){
		this.userStatCrudProxy = userStatCrudFactory.get();
	}
	
}