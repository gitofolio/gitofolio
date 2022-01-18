package com.gitofolio.api.service.proxy.userstat;

import com.gitofolio.api.service.proxy.CrudProxy;
import com.gitofolio.api.service.factory.mapper.UserMapper;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.domain.user.UserStat;
import com.gitofolio.api.service.user.UserStatService;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserStatCrudProxy implements CrudProxy<UserDTO>{
	
	private CrudProxy<UserDTO> crudProxy;
	private final UserMapper<UserStat> userStatMapper;
	private final UserStatService userStatService;
	
	@Override
	@Transactional
	public UserDTO create(Object ...args){
		return this.crudProxy.create(args);
	}
	
	@Override
	@Transactional(readOnly=true)
	public UserDTO read(Object ...args){
		return this.crudProxy.read(args);
	}
	
	@Override
	@Transactional
	public UserDTO update(Object ...args){
		if(args.length==1 && args[0].getClass().equals(UserDTO.class)) {
			return this.userStatMapper.doMap(
				userStatService.edit(
					this.userStatMapper.resolveMap((UserDTO)args[0])
				)
			);
		}
		return this.crudProxy.update(args);
	}
	
	@Override
	@Transactional
	public void delete(Object ...args){
		this.crudProxy.delete(args);
	}
	
	@Override
	public void addProxy(CrudProxy<UserDTO> crudProxy){
		if(this.crudProxy == null) this.crudProxy = crudProxy;
		else this.crudProxy.addProxy(crudProxy);
	}
	
	@Autowired
	public UserStatCrudProxy(@Qualifier("userStatMapper") UserMapper<UserStat> userStatMapper,
							 UserStatService userStatService){
		this.userStatMapper = userStatMapper;
		this.userStatService = userStatService;
	}
	
}