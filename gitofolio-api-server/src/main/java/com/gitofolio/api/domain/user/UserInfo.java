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
	@GeneratedValue
	@Column(name="USER_INFO_ID")
	private Long id;
	
	@Column(name = "NAME", unique=true, nullable=false)
	private String name;
	
	@Column(name="PROFILE_URL", nullable=false)
	private String profileUrl;
	
	// @Column(name="PORTFOLIOCARDS")
	// @OneToMany(mappedBy="userInfo", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	// private List<PortfolioCard> portfolioCards = new ArrayList<PortfolioCard>();
	
	// @OneToOne(mappedBy="userInfo", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	// private UserStat userStat;
	
	// @OneToOne(mappedBy="userInfo", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	// private UserStatistics userStatistics;
	
	
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
	
	// public List<PortfolioCard> getPortfolioCards(){
	// 	return this.portfolioCards;
	// }
	
	// public UserStat getUserStat(){
	// 	return this.userStat;
	// }
	
	// public UserStatistics getUserStatistics(){
	// 	return this.userStatistics;
	// }
	
	
	//setter
	public void setName(String name){
		this.name = name;
	}
	
	public void setProfileUrl(String profileUrl){
		this.profileUrl = profileUrl;
	}
	
	// public void setPortfolioCards(PortfolioCard portfolioCard){
	// 	if(this.portfolioCards.contains(portfolioCard)) return;
	// 	this.portfolioCards.add(portfolioCard);
	// 	if(portfolioCard != null && portfolioCard.getUserInfo() != this) portfolioCard.setUserInfo(this);
	// }
	
	// public void setUserStat(UserStat userStat){
	// 	if(userStat == null) this.userStat = null;
	// 	if(this.userStat != null) this.userStat.setUserInfo(null);
	// 	this.userStat = userStat;
	// 	if(userStat != null && userStat.getUserInfo() != this) userStat.setUserInfo(this);
	// }
	
	// public void setUserStatistics(UserStatistics userStatistics){
	// 	if(userStatistics == null) this.userStatistics = null;
	// 	if(this.userStatistics != null) this.userStatistics.setUserInfo(null);
	// 	this.userStatistics = userStatistics;
	// 	if(userStatistics != null && userStatistics.getUserInfo() != this) userStatistics.setUserInfo(this);
	// }
	
}