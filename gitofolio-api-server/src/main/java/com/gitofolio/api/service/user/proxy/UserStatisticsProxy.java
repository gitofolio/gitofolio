package com.gitofolio.api.service.user.proxy;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.factory.mapper.UserMapper;
import com.gitofolio.api.service.user.factory.hateoas.Hateoas;
import com.gitofolio.api.service.user.UserStatisticsService;
import com.gitofolio.api.service.user.exception.IllegalParameterException;
import com.gitofolio.api.domain.user.UserStatistics;

@Service
public class UserStatisticsProxy implements UserProxy{
	
	private UserStatisticsService userStatisticsService;
	private UserMapper<UserStatistics> userStatisticsMapper;
	private UserProxy userInfoProxy;
	private Hateoas userStatisticsHateoas;
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO getUser(String name){
		return this.setHateoas(
			this.userStatisticsMapper.doMap(
				this.userStatisticsService.get(name)
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
		throw new IllegalParameterException("잘못된 파라미터 요청", "GET : /user/dailystat/{name}의 parameter요청은 허용되지 않았습니다.", "https://api.gitofolio.com/dailystat/"+name);
	}
	
	@Override
	@Transactional
	public UserDTO saveUser(UserDTO userDTO){
		throw new IllegalStateException("/userStatisticsProxy의 saveUser메소드는 허용되지 않았습니다.");
	}
	
	@Override
	@Transactional
	public UserDTO editUser(UserDTO userDTO){
		throw new IllegalStateException("/user/dailystat/{name}의 parameter요청은 허용되지 않았습니다.");
	}
	
	@Override
	@Transactional
	public UserDTO editUser(UserDTO userDTO, Object parameter){
		throw new IllegalParameterException("잘못된 파라미터 요청", "GET : /user/dailystat/{name}의 parameter요청은 허용되지 않았습니다.", "https://api.gitofolio.com/dailystat/"+userDTO.getName());
	}
	
	private UserDTO setHateoas(UserDTO userDTO){
		userDTO.setLinks(this.userStatisticsHateoas.getLinks());
		return userDTO;
	}
	
	@Autowired
	public UserStatisticsProxy(UserStatisticsService userStatisticsService,
								@Qualifier("userInfoProxy") UserProxy userInfoProxy,
								@Qualifier("userStatisticsHateoas") Hateoas userStatisticsHateoas,
								@Qualifier("userStatisticsMapper") UserMapper<UserStatistics> userStatisticsMapper){
		this.userStatisticsService = userStatisticsService;
		this.userInfoProxy = userInfoProxy;
		this.userStatisticsHateoas = userStatisticsHateoas;
		this.userStatisticsMapper = userStatisticsMapper;
	}
	
}