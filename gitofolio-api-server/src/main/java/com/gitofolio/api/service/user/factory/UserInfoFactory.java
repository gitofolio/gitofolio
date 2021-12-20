package com.gitofolio.api.service.user.factory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.factory.mapper.UserMapper;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.factory.hateoas.Hateoas;
import com.gitofolio.api.service.user.UserInfoService;
import com.gitofolio.api.service.user.exception.IllegalParameterException;
import com.gitofolio.api.service.user.exception.DuplicationUserException;
import com.gitofolio.api.domain.user.UserInfo;

@Service
public class UserInfoFactory implements UserFactory{
	
	private UserInfoService userInfoService;
	private UserMapper<UserInfo> userInfoMapper;
	private Hateoas userInfoHateoas;
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name){
		return this.setHateoas(
			this.userInfoMapper.doMap(
				this.userInfoService.get(name)
			)
		);
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name, Object parameter){
		throw new IllegalParameterException("잘못된 파라미터 요청", "GET : /user/{name}의 parameter요청은 허용되지 않았습니다.", "https://api.gitofolio.com/user"+name);
	}
	
	@Override
	@Transactional
	public UserDTO saveUser(UserDTO userDTO){
		UserDTO result = new UserDTO();
		try{
			result = this.setHateoas(
				this.userInfoMapper.doMap(
					this.userInfoService.save(
						this.userInfoMapper.resolveMap(userDTO)
					)
				)
			);
		}catch(DuplicationUserException DUE){
			result = this.editUser(userDTO);
		}
		return result;
	}
	
	@Override
	@Transactional
	public UserDTO editUser(UserDTO userDTO){
		return this.setHateoas(
			this.userInfoMapper.doMap(
				this.userInfoService.edit(
					this.userInfoMapper.resolveMap(userDTO)
				)
			)
		);
	}
	
	@Override
	@Transactional
	public UserDTO editUser(UserDTO userDTO, Object parameter){
		throw new IllegalParameterException("잘못된 파라미터 요청", "GET : /user/{name}의 parameter요청은 허용되지 않았습니다.", "https://api.gitofolio.com/user"+userDTO.getName());
	}
	
	private UserDTO setHateoas(UserDTO userDTO){
		userDTO.setLinks(this.userInfoHateoas.getLinks());
		return userDTO;
	}
	
	@Autowired
	public UserInfoFactory(@Qualifier("userInfoMapper") UserMapper<UserInfo> userInfoMapper,
						  @Qualifier("userInfoHateoas") Hateoas hateoas,
						   UserInfoService userInfoService
						  ){
		this.userInfoService = userInfoService;
		this.userInfoHateoas = hateoas;
		this.userInfoMapper = userInfoMapper;
	}
	
}