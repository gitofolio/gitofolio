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
import com.gitofolio.api.service.user.exception.InvalidHttpMethodException;
import com.gitofolio.api.service.auth.exception.AuthenticateException;
import com.gitofolio.api.service.user.factory.hateoas.Hateoas;
import com.gitofolio.api.service.auth.SessionProcessor;
import com.gitofolio.api.aop.auth.annotation.UserAuthorizationer;
import com.gitofolio.api.aop.hateoas.annotation.HateoasSetter;
import com.gitofolio.api.aop.hateoas.annotation.HateoasType;
import com.gitofolio.api.aop.log.time.annotation.ExpectedTime;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path="/user")
public class UserInfoController {
	
	private final CrudProxy<UserDTO> userInfoCrudProxy;
	
	private final SessionProcessor<UserDTO> loginSessionProcessor;
	
	@ExpectedTime
	@HateoasSetter(hateoasType=HateoasType.USERINFOHATEOAS)
	@RequestMapping(path="", method=RequestMethod.GET)
	public ResponseEntity<UserDTO> getLoginedUser(){
		
		UserDTO userDTO = this.loginSessionProcessor.getAttribute().orElseThrow(()->new AuthenticateException("인증 오류", "로그인 되어있는 유저가 없습니다."));
		
		return new ResponseEntity(userDTO, HttpStatus.OK);
	}
	
	@ExpectedTime
	@HateoasSetter(hateoasType=HateoasType.USERINFOHATEOAS)
	@RequestMapping(path="/{name}", method=RequestMethod.GET)
	public ResponseEntity<UserDTO> getUser(@PathVariable("name") String name){
		
		UserDTO userDTO = this.userInfoCrudProxy.read(name);
		
		return new ResponseEntity(userDTO, HttpStatus.OK);
	}
	
	@ExpectedTime
	@UserAuthorizationer(idx=0)
	@RequestMapping(path="/{name}", method=RequestMethod.DELETE)
	public ResponseEntity<UserDTO> deleteUser(@PathVariable("name") String name){
		
		this.userInfoCrudProxy.delete(name);
		
		return new ResponseEntity(name, HttpStatus.OK);
	}
	
	@Autowired
	public UserInfoController(@Qualifier("userInfoCrudFactory") CrudFactory<UserDTO> userInfoCrudFactory,
							  @Qualifier("loginSessionProcessor") SessionProcessor<UserDTO> loginSessionProcessor){
		this.userInfoCrudProxy = userInfoCrudFactory.get();
		this.loginSessionProcessor = loginSessionProcessor;
	}
}