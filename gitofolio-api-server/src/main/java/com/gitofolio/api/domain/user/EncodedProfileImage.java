package com.gitofolio.api.domain.user;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Lob;
import javax.persistence.GeneratedValue;

@Entity
@Table(name="ENCODED_PROFILE_IMAGE")
public class EncodedProfileImage{
	
	@Id
	@GeneratedValue
	@Column(name="ENCODED_PROFILE_IMAGE_ID")
	private Long id;
	
	@Column(name="PROFILE_URL", unique=true)
	private String profileUrl;
	
	@Lob
	@Column(name="ENCODED_PROFILE_URL")
	private String encodedProfileUrl;
	
	@OneToOne
	@JoinColumn(name="USER_INFO_ID")
	private UserInfo userInfo;
	
	public Long getId(){
		return this.id;
	}
	
	public String getProfileUrl(){
		return this.profileUrl;
	}
	
	public String getEncodedProfileUrl(){
		return this.encodedProfileUrl;
	}
	
	public UserInfo getUserInfo(){
		return this.userInfo;
	}
	
	public void setProfileUrl(String profileUrl){
		this.profileUrl = profileUrl;
	}
	
	public void setEncodedProfileUrl(String encodedProfileUrl){
		this.encodedProfileUrl = encodedProfileUrl;
	}
	
	public void setUserInfo(UserInfo userInfo){
		this.userInfo = userInfo;
	}
	
}