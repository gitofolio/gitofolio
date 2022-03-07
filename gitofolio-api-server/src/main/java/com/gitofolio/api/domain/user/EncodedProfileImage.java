package com.gitofolio.api.domain.user;

import javax.persistence.*;

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
	
	@OneToOne(fetch=FetchType.LAZY)
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