package com.gitofolio.api.service.user.factory;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.factory.mapper.UserMapper;
import com.gitofolio.api.service.user.factory.hateoas.Hateoas;
import com.gitofolio.api.service.user.UserStatisticsService;
import com.gitofolio.api.domain.user.UserStatistics;

@Service
public class UserStatisticsFactory implements UserFactory{
	
	private UserStatisticsService userStatisticsService;
	private UserMapper<UserStatistics> userStatisticsMapper;
	private UserFactory userInfoFactory;
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
	@Transactional(readOnly = true)
	public UserDTO getUser(String name, Object parameter){
		throw new IllegalStateException("/user/dailystat/{name}의 parameter요청은 허용되지 않았습니다.");
	}
	
	@Override
	@Transactional
	public UserDTO saveUser(UserDTO userDTO){
		throw new IllegalStateException("/userStatisticsFactory의 saveUser메소드는 허용되지 않았습니다.");
	}
	
	private UserDTO setHateoas(UserDTO userDTO){
		userDTO.setLinks(this.userStatisticsHateoas.getLinks());
		return userDTO;
	}
	
	@Autowired
	public UserStatisticsFactory(UserStatisticsService userStatisticsService,
								@Qualifier("userInfoFactory") UserFactory userInfoFactory,
								@Qualifier("userStatisticsHateoas") Hateoas userStatisticsHateoas,
								@Qualifier("userStatisticsMapper") UserMapper<UserStatistics> userStatisticsMapper){
		this.userStatisticsService = userStatisticsService;
		this.userInfoFactory = userInfoFactory;
		this.userStatisticsHateoas = userStatisticsHateoas;
		this.userStatisticsMapper = userStatisticsMapper;
	}
	
}