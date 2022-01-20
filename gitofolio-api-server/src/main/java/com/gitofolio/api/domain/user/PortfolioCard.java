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
	
	@Lob
	@Column(name="PORTFOLIO_CARD_ARTICLE")
	private String portfolioCardArticle;

	@Column(name="PORTFOLIO_CARD_STARS", nullable=false)
	private Integer portfolioCardStars;
	
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
	
	public Integer getPortfolioCardStars(){
		return this.portfolioCardStars;
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
		this.portfolioCardArticle = portfolioCardArticle;
	}
	
	public void setPortfolioCardStars(Integer portfolioCardStars){
		this.portfolioCardStars = portfolioCardStars;
	}
	
	public void setPortfolioUrl(String portfolioUrl){
		this.portfolioUrl = portfolioUrl;
	}
	
	public void setUserInfo(UserInfo userInfo){
		this.userInfo = userInfo;
	}
}