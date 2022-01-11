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
import com.gitofolio.api.service.user.proxy.CrudProxy;
import com.gitofolio.api.service.user.factory.CrudFactory;
import com.gitofolio.api.service.user.exception.InvalidHttpMethodException;
import com.gitofolio.api.aop.hateoas.annotation.HateoasSetter;
import com.gitofolio.api.aop.hateoas.annotation.HateoasType;
import com.gitofolio.api.aop.log.time.annotation.ExpectedTime;

@RestController
@RequestMapping(path="/user/dailystat")
public class UserStatisticsController{
	
	private final CrudProxy<UserDTO> userStatisticsCrudProxy;
	
	@ExpectedTime
	@HateoasSetter(hateoasType=HateoasType.USERSTATISTICSHATEOAS)
	@RequestMapping(path="/{name}", method=RequestMethod.GET)
	public ResponseEntity<UserDTO> getUserStatistics(@PathVariable("name") String name){
		
		UserDTO userDTO = this.userStatisticsCrudProxy.read(name);
		
		return new ResponseEntity(userDTO, HttpStatus.OK);
	}
	
	@Autowired
	public UserStatisticsController(@Qualifier("userStatisticsCrudProxy") CrudProxy<UserDTO> userStatisticsCrudProxy){
		this.userStatisticsCrudProxy = userStatisticsCrudProxy;
	}
	
}