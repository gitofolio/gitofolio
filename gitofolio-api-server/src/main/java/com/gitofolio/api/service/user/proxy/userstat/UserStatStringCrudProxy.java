package com.gitofolio.api.service.user.proxy.userstat;

import com.gitofolio.api.service.user.proxy.CrudProxy;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.user.UserStatService;
import com.gitofolio.api.domain.user.UserStat;
import com.gitofolio.api.service.user.factory.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserStatStringCrudProxy implements CrudProxy<UserDTO>{
	
	private CrudProxy<UserDTO> crudProxy = null;
	private final UserMapper<UserStat> userStatMapper;
	private final UserStatService userStatService;
	
	@Override
	public UserDTO create(Object ...args){
		return this.crudProxy.create(args);
	}
	
	@Override
	public UserDTO read(Object ...args){
		if(args.length==1 && args[0].getClass().equals(String.class)) {
			return this.userStatMapper.doMap(
				userStatService.get((String)args[0])
			);
		}
		return this.crudProxy.read(args);
	}
	
	@Override
	public UserDTO update(Object ...args){
		return this.crudProxy.update(args);
	}
	
	@Override
	public void delete(Object ...args){
		if(args.length==1 && args[0].getClass().equals(String.class)) userStatService.delete((String)args[0]);
		else this.crudProxy.delete(args);
	}
	
	@Override
	public void addProxy(CrudProxy<UserDTO> crudProxy){
		if(this.crudProxy == null) this.crudProxy = crudProxy;
		else this.crudProxy.addProxy(crudProxy);
	}
	
	@Autowired
	public UserStatStringCrudProxy(UserStatService userStatService,
								  @Qualifier("userStatMapper") UserMapper<UserStat> userStatMapper){
		this.userStatService = userStatService;
		this.userStatMapper = userStatMapper;
	}
	
}