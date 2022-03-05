package com.gitofolio.api.domain.user;

import javax.persistence.*;

@Entity
@Table(name = "USER_STAT")
public class UserStat{
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="TOTAL_VISITORS", nullable=false)
	private Integer totalVisitors = 0;

	@OneToOne
	@JoinColumn(name="USER_INFO_ID")
	private UserInfo userInfo;
	
	public Long getId(){
		return this.id;
	}
	
	public Integer getTotalVisitors(){
		return this.totalVisitors;
	}
	
	public UserInfo getUserInfo(){
		return this.userInfo;
	}
	
	public void setTotalVisitors(Integer totalVisitors){
		this.totalVisitors = totalVisitors;
	}
	
	public void addTotalVisitors(){
		this.totalVisitors++;
	}
	
	public void setUserInfo(UserInfo userInfo){
		if(userInfo == null) this.userInfo = null;
		this.userInfo = userInfo;
	}
	
}