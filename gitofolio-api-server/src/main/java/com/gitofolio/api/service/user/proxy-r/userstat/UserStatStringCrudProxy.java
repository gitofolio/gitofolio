// package com.gitofolio.api.service.user.proxy.userstat;

// import com.gitofolio.api.service.user.proxy.CrudProxy;
// import com.gitofolio.api.service.user.dtos.UserDTO;
// import com.gitofolio.api.service.user.UserStatService;
// import com.gitofolio.api.domain.user.UserStat;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.stereotype.Service;

// @Service
// public class UserStatStringCrudProxy<UserDTO> implements CrudProxy<UserDTO>{
	
// 	private CrudProxy<UserDTO> crudProxy;
// 	private UserStatService userStatService;
	
// 	@Override
// 	public UserDTO create(Object ...args){
// 		if(args.size()==1 && args[0].getClass().equals(String.class)) return userStatService.save((String)args[0]);
// 		return this.userStatStringCrudProxy.create(args);
// 	}
	
// 	@Override
// 	public UserDTO read(Object ...args){
// 		if(args.size()==1 && args[0].getClass().equals(String.class)) return userStatService.get((String)args[0]);
// 		return this.userStatStringCrudProxy.read(args);
// 	}
	
// 	@Override
// 	public UserDTO update(Object ...args){
// 		if(args.size()==1 && args[0].getClass().equals(String.class)) return userStatService.update((String)args[0]);
// 		return this.userStatStringCrudProxy.update(args);
// 	}
	
// 	@Override
// 	public UserDTO delete(Object ...args){
// 		if(args.size()==1 && args[0].getClass().equals(String.class)) return userStatService.delete((String)args[0]);
// 		return this.userStatStringCrudProxy.delete(args);
// 	}
	
// 	@Autowired
// 	public UserStatStringCrudProxy(UserStatService userStatService){
// 		this.userStatService = userStatService;
// 	}
	
// }