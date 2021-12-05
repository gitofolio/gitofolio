package com.gitofolio.api.service.user.eraser;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.UserStatService;

@Service
public class UserStatEraser implements UserEraser{
	
	private UserStatService userStatService;
	
	@Override
	@Transactional
	public String delete(String name){
		this.userStatService.delete(name);
		return name;
	}
	
	@Override
	public String delete(String name, Object parameter){
		throw new IllegalStateException("DELETE : /user/stat의 parameter요청은 허용되지 않았습니다.");
	}
	
	@Autowired
	public UserStatEraser(UserStatService userStatService){
		this.userStatService = userStatService;
	}
	
}