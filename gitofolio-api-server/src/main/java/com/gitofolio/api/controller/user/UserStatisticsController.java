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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.factory.UserFactory;
import com.gitofolio.api.service.user.eraser.UserEraser;

@RestController
@RequestMapping(path="/user/statistics")
public class UserStatisticsController{
	
	@Autowired
	@Qualifier("userStatisticsFactory")
	private UserFactory userStatisticsFactory;
	
	@Autowired
	@Qualifier("userStatisticsEraser")
	private UserEraser userStatisticsEraser;
	
	@RequestMapping(path="/{name}", method=RequestMethod.GET)
	@ApiOperation(value="유저 statistics정보 가져오기", notes="{name}에 해당하는 유저의 statistics정보 를 가져옴 statistics에는 날짜별 방문수와 방문사이트가 있음")
	@ApiResponses({
		@ApiResponse(code=200, message="유저 조회 성공"),   
		@ApiResponse(code=404, message="유저 조회 실패 - 없는 유저 조회할 경우 발생")
	})
	public ResponseEntity<UserDTO> getUserStatistics(@PathVariable("name") String name){
		
		UserDTO userDTO = this.userStatisticsFactory.getUser(name);
		
		return new ResponseEntity(userDTO, HttpStatus.OK);
	}
	
	// @RequestMapping(path="", method=RequestMethod.POST)
	// public ResponseEntity<UserDTO> saveUserStatistics(@RequestBody UserDTO userDTO){
		
	// 	UserDTO result = this.userStatisticsFactory.saveUser(userDTO);
		
	// 	return new ResponseEntity(result, HttpStatus.CREATED);
		
	// }
	
	// @RequestMapping(path="/{name}", method=RequestMethod.DELETE)
	// public ResponseEntity<UserDTO> deleteUserStatistics(@PathVariable("name") String name){
		
	// 	this.userStatisticsEraser.delete(name);
		
	// 	return new ResponseEntity(HttpStatus.OK);
	// }
	
}