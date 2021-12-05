package com.gitofolio.api.service.user;

import com.gitofolio.api.service.user.exception.NonExistUserException;
import com.gitofolio.api.service.user.exception.DuplicationUserException;
import com.gitofolio.api.repository.user.UserInfoRepository;
import com.gitofolio.api.domain.user.UserInfo;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserInfoService{
	
	private UserInfoRepository userInfoRepository;
	
	public UserInfo get(String name){
		UserInfo user = this.userInfoRepository.findByName(name).orElseThrow(()->new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/user/"+name));
		return user;
	}
	
	public UserInfo save(UserInfo user){
		UserInfo exist = this.userInfoRepository.findByName(user.getName()).orElseGet(()->new UserInfo());
		if(exist.getName() == null) userInfoRepository.save(user);
		else if(exist.getName() != null) throw new DuplicationUserException("유저 이름이 중복되었습니다.", "다른 유저이름을 사용해 저장하세요", "/user/"+exist.getName());
			
		return this.get(user.getName());
	}
	
	public void delete(String name){
		UserInfo user = this.userInfoRepository.findByName(name)
			.orElseThrow(() -> new NonExistUserException("존재 하지 않는 유저에 대한 삭제 요청입니다.", "유저 이름을 확인해주세요", "/user/"+name));
		this.userInfoRepository.deleteByName(name);
		return;
	}
	
	// constructor
	@Autowired
	public UserInfoService(UserInfoRepository userInfoRepository){
		this.userInfoRepository = userInfoRepository;
	}
	
}