package com.gitofolio.api.service.user;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.service.user.exception.NonExistUserException;
import com.gitofolio.api.repository.user.UserStatRepository;
import com.gitofolio.api.repository.user.UserInfoRepository;
import com.gitofolio.api.domain.user.UserStat;
import com.gitofolio.api.domain.user.UserInfo;

@Service
public class UserStatService{
	
	private UserStatRepository userStatRepository;
	private UserInfoRepository userInfoRepository;
	
	public UserStat get(String name){
		UserStat userStat = this.userStatRepository.findByName(name).orElseGet(()->new UserStat());
		
		if(userStat.getUserInfo() == null){
			UserInfo userInfo = this.userInfoRepository.findByName(name)
				.orElseThrow(()->new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/user/stat/"+name));
			userStat.setTotalStars(0);
			userStat.setTotalVisitors(0);
			userStat.setUserInfo(userInfo);
			
			userStatRepository.save(userStat);
		}
		
		return userStat;
	}
	
	public UserStat edit(UserStat userStat){
		UserInfo userInfo = this.userInfoRepository.findByName(userStat.getUserInfo().getName())
			.orElseThrow(()->new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/user/stat/"));
		
		UserStat exist = this.userStatRepository.findByName(userInfo.getName()).orElseGet(()->new UserStat());
		if(exist.getUserInfo() == null) userStatRepository.save(userStat);
		else userStat.addTotalStars();
		
		return exist;
	}
	
	public void delete(String name){
		this.userStatRepository.deleteByName(name);
		return;
	}
	
	@Transactional
	public void increaseTotalVisitors(String name){
		UserStat userStat = this.userStatRepository.findByName(name).orElseGet(()->new UserStat());
		if(userStat.getUserInfo() == null){
			UserInfo userInfo = this.userInfoRepository.findByName(name)
				.orElseThrow(()->new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/user/stat/"));
			userStat.setTotalStars(0);
			userStat.setTotalVisitors(0);
			userStat.setUserInfo(userInfo);
			
			userStatRepository.save(userStat);
		}
		userStat.addTotalVisitors();
		return;
	}
	
	@Transactional
	public void increaseTotalVisitors(Long id){
		UserStat userStat = this.userStatRepository.findById(id).orElseGet(()->new UserStat());
		if(userStat.getUserInfo() == null){
			UserInfo userInfo = this.userInfoRepository.findById(id)
				.orElseThrow(()->new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/user/stat/"));
			userStat.setTotalStars(0);
			userStat.setTotalVisitors(0);
			userStat.setUserInfo(userInfo);
			
			userStatRepository.save(userStat);
		}
		userStat.addTotalVisitors();
	}
	
	@Transactional
	public void increaseTotalStars(String name){
		UserStat userStat = this.userStatRepository.findByName(name).orElseGet(()->new UserStat());
		if(userStat.getUserInfo() == null){
			UserInfo userInfo = this.userInfoRepository.findByName(name)
				.orElseThrow(()->new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/user/stat/"));
			userStat.setTotalStars(0);
			userStat.setTotalVisitors(1);
			userStat.setUserInfo(userInfo);
			
			userStatRepository.save(userStat);
		}
		userStat.addTotalStars();
		return;
	}
	
	@Transactional
	public void increaseTotalStars(Long id){
		UserStat userStat = this.userStatRepository.findById(id).orElseGet(()->new UserStat());
		if(userStat.getUserInfo() == null){
			UserInfo userInfo = this.userInfoRepository.findById(id)
				.orElseThrow(()->new NonExistUserException("존재 하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/user/stat/"));
			userStat.setTotalStars(0);
			userStat.setTotalVisitors(1);
			userStat.setUserInfo(userInfo);
			
			userStatRepository.save(userStat);
		}
		userStat.addTotalStars();
		return;
	}
	
	@Autowired
	public UserStatService(UserStatRepository userStatRepository
						  , UserInfoRepository userInfoRepository){
		this.userStatRepository = userStatRepository;
		this.userInfoRepository = userInfoRepository;
	}
	
}