package com.gitofolio.api.domain.auth;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;

import com.gitofolio.api.service.common.random.RandomKeyGenerator;

import java.time.LocalDate;

@Entity
@Table(name="PERSONAL_ACCESS_TOKEN")
public class PersonalAccessToken{
	
	@Id
	@GeneratedValue
	private Long tokenKey;
	
	@Column(name="VALUE", length=1000, unique=true)
	private String tokenValue;
	
	@Column(name="LAST_USED_DATE")
	private LocalDate lastUsedDate;
	
	public Long getTokenKey(){
		return this.tokenKey;
	}
	
	public String getTokenValue(){
		return this.tokenValue;
	}
	
	public LocalDate getLastUsedDate(){
		return this.lastUsedDate;
	}
	
	public void updateLastUsedDate(){
		this.lastUsedDate = LocalDate.now();
	}
	
	public PersonalAccessToken(){
		this.lastUsedDate = LocalDate.now();
		this.tokenValue = RandomKeyGenerator.generateKey(5);
	}
	
}