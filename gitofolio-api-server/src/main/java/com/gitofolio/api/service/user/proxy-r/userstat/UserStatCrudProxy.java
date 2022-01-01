// package com.gitofolio.api.service.user.proxy.userstat;

// import com.gitofolio.api.service.user.proxy.CrudProxy;
// import com.gitofolio.api.service.user.dtos.UserDTO;
// import com.gitofolio.api.domain.user.UserStat;
// import com.gitofolio.api.service.user.UserStatService;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.stereotype.Service;

// @Service
// public class UserStatCrudProxy implements CrudProxy<UserDTO>{
	
// 	private CrudProxy<UserDTO> userStatStringCrudProxy;
// 	private UserStatService userStatService;
	
// 	@Override
// 	public UserDTO create(Object ...args){
// 		if(args.size()==1 && args[0].getClass().equals(UserStat.class)) return userStatService.save((UserStat)args[0]);
// 		return this.userStatStringCrudProxy.create(args);
// 	}
	
// 	@Override
// 	public UserDTO read(Object ...args){
// 		if(args.size()==1 && args[0].getClass().equals(UserStat.class)) return userStatService.get((UserStat)args[0]);
// 		return this.userStatStringCrudProxy.read(args);
// 	}
	
// 	@Override
// 	public UserDTO update(Object ...args){
// 		if(args.size()==1 && args[0].getClass().equals(UserStat.class)) return userStatService.update((UserStat)args[0]);
// 		return this.userStatStringCrudProxy.update(args);
// 	}
	
// 	@Override
// 	public UserDTO delete(Object ...args){
// 		if(args.size()==1 && args[0].getClass().equals(UserStat.class)) return userStatService.delete((UserStat)args[0]);
// 		return this.userStatStringCrudProxy.delete(args);
// 	}
	
// 	@Autowired
// 	public UserStatCrudProxy(@Qualifier("userStatStringCrudProxy") CrudProxy<UserDTO> userStatStringCrudProxy,
// 							UserStatService userStatService){
// 		this.userStatStringCrudProxy = userStatStringCrudProxy;
// 		this.userStatService = userStatService;
// 	}
	
// }