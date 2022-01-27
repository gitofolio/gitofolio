package com.gitofolio.api.domain.auth;

import javax.persistence.*;

import com.gitofolio.api.service.common.random.RandomKeyGenerator;
import com.gitofolio.api.domain.user.UserInfo;
import com.gitofolio.api.service.auth.token.TokenAble;

import java.time.LocalDate;

@Entity
@Table(name="PERSONAL_ACCESS_TOKEN")
public class PersonalAccessToken implements TokenAble{
	
	@Id
	@GeneratedValue
	private Long tokenKey;
	
	@Column(name="VALUE", length=1000, unique=true)
	private String tokenValue;
	
	@Column(name="LAST_USED_DATE")
	private LocalDate lastUsedDate;
	
	@ManyToOne
	@JoinColumn(name="USER_INFO_ID")
	private UserInfo userInfo;
	
	public Long getTokenKey(){
		return this.tokenKey;
	}
	
	public String getTokenValue(){
		return this.tokenValue;
	}
	
	public LocalDate getLastUsedDate(){
		return this.lastUsedDate;
	}
	
	public UserInfo getUserInfo(){
		return this.userInfo;
	}
	
	public void updateLastUsedDate(){
		this.lastUsedDate = LocalDate.now();
	}
	
	public void setUserInfo(UserInfo userInfo){
		this.userInfo = userInfo;
	}
	
	public void setTokenKey(Long tokenKey){
		this.tokenKey = tokenKey;
	}
	
	public String token(){
		return this.userInfo.getName();
	}
	
	public PersonalAccessToken(){
		this.lastUsedDate = LocalDate.now();
		this.tokenValue = RandomKeyGenerator.generateKey(5);
	}
	
}