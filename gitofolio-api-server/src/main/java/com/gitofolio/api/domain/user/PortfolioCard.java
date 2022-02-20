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
import javax.persistence.Lob;

import java.util.List;

@Entity
@Table(name = "PORTFOLIO_CARD")
public class PortfolioCard{

	@Id
	@GeneratedValue
	@Column(name="PORTFOLIO_CARD_ID")
	private Long id;
	
	@Column(name="PORTFOLIO_CARD_ARTICLE", length=1005)
	private String portfolioCardArticle;

	@Column(name="PORTFOLIO_CARD_WATCHED", nullable=false)
	private Integer portfolioCardWatched;
	
	@Column(name="PORTFOLIO_URL", nullable=false)
	private String portfolioUrl;
	
	@ManyToOne
	@JoinColumn(name="USER_INFO_ID")
	private UserInfo userInfo;
	
	public Long getId(){
		return this.id;
	}
	
	public String getPortfolioCardArticle(){
		return this.portfolioCardArticle;
	}
	
	public Integer getPortfolioCardWatched(){
		return this.portfolioCardWatched;
	}
	
	public void increasePortfolioCardWatched(){
		if(this.isPortfoliocardWatchedReachedMax()) return;
		this.portfolioCardWatched++;
	}
	
	private boolean isPortfoliocardWatchedReachedMax(){
		return (this.portfolioCardWatched >= 999999999) ? true : false;
	}
	
	public String getPortfolioUrl(){
		return this.portfolioUrl;
	}
	
	public UserInfo getUserInfo(){
		return this.userInfo;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public void setPortfolioCardArticle(String portfolioCardArticle){
		if(portfolioCardArticle.length()>=1000) portfolioCardArticle = portfolioCardArticle.substring(0,1000);
		this.portfolioCardArticle = portfolioCardArticle;
	}
	
	public void setPortfolioCardWatched(Integer portfolioCardWatched){
		this.portfolioCardWatched = portfolioCardWatched;
	}
	
	public void setPortfolioUrl(String portfolioUrl){
		this.portfolioUrl = portfolioUrl;
	}
	
	public void setUserInfo(UserInfo userInfo){
		this.userInfo = userInfo;
	}
}