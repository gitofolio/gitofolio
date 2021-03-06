package com.gitofolio.api.service.user;

import com.gitofolio.api.service.user.exception.*;
import com.gitofolio.api.repository.user.*;
import com.gitofolio.api.repository.auth.*;
import com.gitofolio.api.domain.user.UserInfo;
import com.gitofolio.api.aop.log.time.annotation.ExpectedTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;

@Service
public class UserInfoService{
	
	private UserInfoRepository userInfoRepository;
	private UserStatService userStatService;
	private UserStatisticsService userStatisticsService;
	private PortfolioCardRepository portfolioCardRepository;
	private EncodedProfileImageRepository encodedProfileImageRepository;
	private PersonalAccessTokenRepository personalAccessTokenRepository;
	
	public UserInfo get(String name){
		UserInfo user = this.userInfoRepository.findByName(name).orElseThrow(()->new NonExistUserException("존재하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/user/"+name));
		return user;
	}
	
	public UserInfo save(UserInfo user){
		UserInfo exist = this.userInfoRepository.findByName(user.getName()).orElseGet(()->new UserInfo());
		if(isNewUser(exist)) initialUser(user);
		else {
			user.setId(exist.getId());
			return this.edit(user);
		}
		
		return this.get(user.getName());
	}
	
	private void initialUser(UserInfo user){
		this.userInfoRepository.save(user);
		this.userStatService.get(user.getName());
		this.userStatisticsService.get(user.getName());
	}
	
	public void delete(String name){
		UserInfo user = this.userInfoRepository.findByName(name)
			.orElseThrow(() -> new NonExistUserException("존재하지 않는 유저에 대한 삭제 요청입니다.", "유저 이름을 확인해주세요", "/user/"+name));
		this.portfolioCardRepository.deleteByName(name);
		this.userStatService.delete(name);
		this.encodedProfileImageRepository.deleteByName(name);
		this.userStatisticsService.delete(name);
		this.personalAccessTokenRepository.deleteByName(name);
		this.userInfoRepository.deleteByName(name);
		return;
	}
	
	public UserInfo edit(UserInfo user){
		UserInfo exist = this.userInfoRepository.findById(user.getId())
			.orElseThrow(()->new NonExistUserException("존재하지 않는 유저에 대한 수정 요청입니다.", "유저 아이디를 확인해주세요", "/user/"+user.getId()));
		
		try{
			doEdit(exist, user);
		}catch(IllegalAccessException IAE){
			throw new EditException("유저 정보를 수정하는데 실패했습니다.", "요청 JSON을 확인해주세요", "/user/"+user.getId());
		}
		
		return this.get(exist.getName());
	}
	
	private void doEdit(UserInfo oldUserInfo, UserInfo newUserInfo) throws IllegalAccessException{
		Field[] oldFields = oldUserInfo.getClass().getDeclaredFields();
		Field[] newFields = newUserInfo.getClass().getDeclaredFields();
			
		for(Field oldField : oldFields){
			oldField.setAccessible(true);
			Object oldFieldValue = oldField.get(oldUserInfo);
				
			if(!isFieldEditAble(oldField) || oldFieldValue == null) continue;
				
			for(Field newField : newFields){
				newField.setAccessible(true);
				Object newFieldValue = newField.get(newUserInfo);
				
				if(!isFieldEditAble(newField) || newFieldValue == null) continue;
				
				if(oldField.getName().equals(newField.getName())) oldField.set(oldUserInfo, newFieldValue);
			}
		}
	}
	
	private boolean isFieldEditAble(Field field){
		if(field.getName().equals("id")) return false;
		return true;
	}
	
	private boolean isNewUser(UserInfo user){
		return (user.getName() == null) ? true : false;
	}
	
	@Autowired
	public UserInfoService(UserInfoRepository userInfoRepository,
						  UserStatService userStatService,
						  UserStatisticsService userStatisticsService,
						  PortfolioCardRepository portfolioCardRepository,
						  EncodedProfileImageRepository encodedProfileImageRepository,
						  PersonalAccessTokenRepository personalAccessTokenRepository){
		this.userInfoRepository = userInfoRepository;
		this.userStatService = userStatService;
		this.userStatisticsService = userStatisticsService;
		this.portfolioCardRepository = portfolioCardRepository;
		this.encodedProfileImageRepository = encodedProfileImageRepository;
		this.personalAccessTokenRepository = personalAccessTokenRepository;
	}
	
}