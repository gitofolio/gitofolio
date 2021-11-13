package com.gitofolio.api.domain.user;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "USER_STAT")
public class UserStat{
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="TOTAL_VISITORS", nullable=false)
	private Integer totalVisitors = 0;
	
	@Column(name="TOTAL_STARS", nullable=false)
	private Integer totalStars = 0;

	@OneToOne
	@JoinColumn(name="USER_INFO_ID", unique=true)
	private UserInfo userInfo;
	
	//getter
	public Long getId(){
		return this.id;
	}
	
	public Integer getTotalVisitors(){
		return this.totalVisitors;
	}
	
	public Integer getTotalStars(){
		return this.totalStars;
	}
	
	public UserInfo getUserInfo(){
		return this.userInfo;
	}
	
	// setter
	public void setTotalVisitors(Integer totalVisitors){
		this.totalVisitors = totalVisitors;
	}
	
	public void setTotalStars(Integer totalStars){
		this.totalStars = totalStars;
	}
	
	// logic
	public void addTotalVisitors(){
		this.totalVisitors++;
	}
	
	public void addTotalStars(){
		this.totalStars++;
	}
	
	public void setUserInfo(UserInfo userInfo){
		if(userInfo == null) this.userInfo = null;
		// if(this.userInfo != null) this.userInfo.setUserStat(null);
		this.userInfo = userInfo;
		// if(userInfo.getUserStat() != this) userInfo.setUserStat(this);
	}
	
}