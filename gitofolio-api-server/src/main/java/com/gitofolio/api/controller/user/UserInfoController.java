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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

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
	
	@ApiOperation(value="유저 정보 가져오기", notes="/user/{name}에 입력된 name에 해당하는 유저의 아이디와 프로필URL을 가져옴")
	@ApiResponses({
		@ApiResponse(code=200, message="유저 조회 성공"),
		@ApiResponse(code=404, message="유저 조회 실패 - 없는 유저 조회할 경우 발생")
	})
	@RequestMapping(path="/{name}", method=RequestMethod.GET)
	public ResponseEntity<UserDTO> getUser(@PathVariable("name") String name){
		
		UserDTO userDTO = this.userInfoFactory.getUser(name);
		
		return new ResponseEntity(userDTO, HttpStatus.OK);
	}
	
	@ApiOperation(value="유저 저장", notes="HTTP Body에 JSON타입으로 유저 저장, name과 profileUrl만 명시해도 되며, 이외의 필드는 입력되도 무시함")
	@ApiResponses({
		@ApiResponse(code=201, message="유저 저장 성공"),
		@ApiResponse(code=404, message="유저 저장 후 조회 실패 - 저장 단계에서 발생하지않고, 조회 단계에서 발생함. 이 오류가 나왔다면, 서버오류일 확률이 매우 높음")
	})
	@RequestMapping(path="", method=RequestMethod.POST)
	public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO){

		UserDTO result = this.userInfoFactory.saveUser(userDTO);
		
		return new ResponseEntity(result, HttpStatus.CREATED);
		
	}
	
	@ApiOperation(value="유저 삭제", notes="/user/{name}에 해당하는 유저 삭제")
	@ApiResponse(code=200, message="유저 삭제 성공")
	@RequestMapping(path="/{name}", method=RequestMethod.DELETE)
	public ResponseEntity<UserDTO> deleteUser(@PathVariable("name") String name){
		
		this.userInfoEraser.delete(name);
		
		return new ResponseEntity(HttpStatus.OK);
	}
	
}