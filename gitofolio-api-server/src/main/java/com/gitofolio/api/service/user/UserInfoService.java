package com.gitofolio.api.service.user;

import com.gitofolio.api.service.user.exception.NonExistUserException;
import com.gitofolio.api.service.user.exception.DuplicationUserException;
import com.gitofolio.api.service.user.exception.EditException;
import com.gitofolio.api.repository.user.UserInfoRepository;
import com.gitofolio.api.domain.user.UserInfo;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;

@Service
public class UserInfoService{
	
	private UserInfoRepository userInfoRepository;
	
	public UserInfo get(String name){
		UserInfo user = this.userInfoRepository.findByName(name).orElseThrow(()->new NonExistUserException("존재하지 않는 유저 입니다.", "유저이름을 확인해 주세요.", "/user/"+name));
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
			.orElseThrow(() -> new NonExistUserException("존재하지 않는 유저에 대한 삭제 요청입니다.", "유저 이름을 확인해주세요", "/user/"+name));
		this.userInfoRepository.deleteByName(name);
		return;
	}
	
	public UserInfo edit(UserInfo user){
		UserInfo exist = this.userInfoRepository.findById(user.getId())
			.orElseThrow(()->new NonExistUserException("존재하지 않는 유저에 대한 수정 요청입니다.", "유저 아이디를 확인해주세요", "/user/"+user.getId()));
		
		Field[] oldFields = exist.getClass().getDeclaredFields();
		Field[] newFields = user.getClass().getDeclaredFields();
		
		try{
			for(Field oldField : oldFields){
				oldField.setAccessible(true);
				Object oldFieldValue = oldField.get(exist);
				
				if(oldField.getName().equals("id") || oldFieldValue == null) continue;
				
				for(Field newField : newFields){
					newField.setAccessible(true);
					Object newFieldValue = newField.get(user);
					
					if(newField.getName().equals("id") || newFieldValue == null) continue;
					
					if(oldField.getName().equals(newField.getName())) oldField.set(exist, newFieldValue);
				}
			}
		}catch(IllegalAccessException IAE){
			throw new EditException("유저 정보를 수정하는데 실패했습니다.", "요청 JSON을 확인해주세요", "/user/"+user.getId());
		}
		
		return this.get(exist.getName());
	}
	
	// constructor
	@Autowired
	public UserInfoService(UserInfoRepository userInfoRepository){
		this.userInfoRepository = userInfoRepository;
	}
	
}