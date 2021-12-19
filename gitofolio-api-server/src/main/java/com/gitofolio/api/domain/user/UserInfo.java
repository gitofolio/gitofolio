package com.gitofolio.api.domain.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name="USER_INFO")
public class UserInfo{
	
	@Id
	@Column(name="USER_INFO_ID")
	private Long id;
	
	@Column(name = "NAME", unique=true, nullable=false)
	private String name;
	
	@Column(name="PROFILE_URL", nullable=false)
	private String profileUrl;
	
	// getter
	public Long getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getProfileUrl(){
		return this.profileUrl;
	}

	//setter
	public void setId(Long id){
		this.id = id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setProfileUrl(String profileUrl){
		this.profileUrl = profileUrl;
	}
	
}