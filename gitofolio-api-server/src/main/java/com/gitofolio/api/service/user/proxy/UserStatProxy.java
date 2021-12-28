package com.gitofolio.api.service.user.proxy;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.factory.mapper.UserMapper;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.factory.hateoas.Hateoas;
import com.gitofolio.api.service.user.exception.IllegalParameterException;
import com.gitofolio.api.service.user.UserStatService;
import com.gitofolio.api.domain.user.UserStat;

@Service
public class UserStatProxy implements UserProxy{
	
	private UserStatService userStatService;
	private UserProxy userInfoProxy;
	private Hateoas userStatHateoas;
	private UserMapper<UserStat> userStatMapper;
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name){
		return setHateoas(
			this.userStatMapper.doMap(
				this.userStatService.get(name)
			)
		);
	}
	
	@Override
	public UserDTO getUser(Long id){
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name, Object parameter){
		throw new IllegalParameterException("잘못된 파라미터 요청", "GET : /user/stat/{name}의 parameter요청은 허용되지 않았습니다.", "https://api.gitofolio.com/stat/"+name);
	}
	
	@Override
	@Transactional
	public UserDTO saveUser(UserDTO userDTO){
		throw new IllegalStateException("userstat의 saveUser요청은 허용되지 않았습니다.");
	}
	
	@Override
	@Transactional
	public UserDTO editUser(UserDTO userDTO){
		throw new IllegalStateException("PUT : /user/stat/{name}의 parameter요청은 허용되지 않았습니다.");
	}
	
	@Override
	@Transactional
	public UserDTO editUser(UserDTO userDTO, Object parameter){
		throw new IllegalParameterException("잘못된 파라미터 요청", "GET : /user/stat/{name}의 parameter요청은 허용되지 않았습니다.", "https://api.gitofolio.com/stat/"+userDTO.getName());
	}
	
	private UserDTO setHateoas(UserDTO userDTO){
		userDTO.setLinks(this.userStatHateoas.getLinks());
		return userDTO;
	}
	
	@Autowired
	public UserStatProxy(UserStatService userStatService,
						  @Qualifier("userInfoProxy") UserProxy userInfoProxy,
						  @Qualifier("userStatHateoas") Hateoas userStatHateoas,
						  @Qualifier("userStatMapper") UserMapper<UserStat> userStatMapper){
		this.userStatService = userStatService;
		this.userInfoProxy = userInfoProxy;
		this.userStatHateoas = userStatHateoas;
		this.userStatMapper = userStatMapper;
	}
	
}