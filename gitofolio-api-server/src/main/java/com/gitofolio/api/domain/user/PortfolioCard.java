package com.gitofolio.api.domain.user;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;

import java.util.List;

@Entity
@Table(name = "PORTFOLIO_CARD")
public class PortfolioCard{

	@Id
	@GeneratedValue
	@Column(name="PORTFOLIO_CARD_ID")
	private Long id;
	
	@Column(name="PORTFOLIO_CARD_ARTICLE")
	private String portfolioCardArticle;

	@Column(name="PORTFOLIO_CARD_STARS", nullable=false)
	private Integer portfolioCardStars;
	
	@Column(name="PORTFOLIO_URL", nullable=false)
	private String portfolioUrl;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="USER_INFO_ID")
	private UserInfo userInfo;
	
	
	//getter
	public Long getId(){
		return this.id;
	}
	
	public String getPortfolioCardArticle(){
		return this.portfolioCardArticle;
	}
	
	public Integer getPortfolioCardStars(){
		return this.portfolioCardStars;
	}
	
	public String getPortfolioUrl(){
		return this.portfolioUrl;
	}
	
	public UserInfo getUserInfo(){
		return this.userInfo;
	}
	
	//setter
	public void setPortfolioCardArticle(String portfolioCardArticle){
		this.portfolioCardArticle = portfolioCardArticle;
	}
	
	public void setPortfolioCardStars(Integer portfolioCardStars){
		this.portfolioCardStars = portfolioCardStars;
	}
	
	public void setPortfolioUrl(String portfolioUrl){
		this.portfolioUrl = portfolioUrl;
	}
	
	public void setUserInfo(UserInfo userInfo){
		if(this.userInfo != null){
			List<PortfolioCard> deallocate = this.userInfo.getPortfolioCards();
			deallocate.remove(this);
		}
		this.userInfo = userInfo;
		userInfo.setPortfolioCards(this);
	}
}