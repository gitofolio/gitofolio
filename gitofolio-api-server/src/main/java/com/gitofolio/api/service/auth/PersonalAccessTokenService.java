package com.gitofolio.api.service.auth;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import com.gitofolio.api.domain.auth.PersonalAccessToken;
import com.gitofolio.api.repository.auth.PersonalAccessTokenRepository;
import com.gitofolio.api.service.auth.exception.AuthenticateException;
import com.gitofolio.api.domain.user.UserInfo;

import java.time.LocalDate;

@Service
public class PersonalAccessTokenService{
	
	private final PersonalAccessTokenRepository personalAccessTokenRepository;
	private final DeletePerMonthRealTask deletePerMonthRealTask;
	
	@Transactional
	public PersonalAccessToken get(Long id){
		PersonalAccessToken personalAccessToken = this.personalAccessTokenRepository.findByTokenKey(id)
			.orElseThrow(()->new AuthenticateException("PersonalAccessToken 오류", "존재하지않는 PersonalAccessToken 입니다."));
		personalAccessToken.updateLastUsedDate();
		return personalAccessToken;
	}
	
	@Transactional
	public PersonalAccessToken get(String value){
		PersonalAccessToken personalAccessToken = this.personalAccessTokenRepository.findByTokenValue(value)
			.orElseThrow(()->new AuthenticateException("PersonalAccessToken 오류", "존재하지않는 PersonalAccessToken 입니다."));
		personalAccessToken.updateLastUsedDate();
		return personalAccessToken;
	}
	
	@Transactional
	public PersonalAccessToken save(UserInfo userInfo){
		PersonalAccessToken personalAccessToken = new PersonalAccessToken();
		personalAccessToken.setUserInfo(userInfo);
		
		this.personalAccessTokenRepository.save(personalAccessToken);
		
		personalAccessToken = this.personalAccessTokenRepository.findByTokenValue(personalAccessToken.getTokenValue())
			.orElseThrow(()->new RuntimeException("PersonalAccessTokenService.save() 생성 오류 Value중복시 발생가능"));
		return personalAccessToken;
	}
	
	@Transactional
	public void delete(String name){
		this.personalAccessTokenRepository.deleteByName(name);
	}
	
	@Async
	@Scheduled(fixedRate=3600000) // 1시간 만료된 AccessToken 삭제. AccessToken은 30일 동안 사용하지않으면 만료됨. 
	public void deletePerMonth(){
		this.deletePerMonthRealTask.task(this.personalAccessTokenRepository);
	}
	
	@Component
	public static class DeletePerMonthRealTask{
		
		@Transactional
		public void task(PersonalAccessTokenRepository personalAccessTokenRepository){
			personalAccessTokenRepository.deleteUnusedToken(LocalDate.now().minusMonths(1));
		}
		
	}
	
	@Autowired
	public PersonalAccessTokenService(PersonalAccessTokenRepository personalAccessTokenRepository,
									 DeletePerMonthRealTask deletePerMonthRealTask){
		this.personalAccessTokenRepository = personalAccessTokenRepository;
		this.deletePerMonthRealTask = deletePerMonthRealTask;
	}
		
}