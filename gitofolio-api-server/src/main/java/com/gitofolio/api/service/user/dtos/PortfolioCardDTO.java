package com.gitofolio.api.service.user.dtos;

import com.gitofolio.api.domain.user.PortfolioCard;

public class PortfolioCardDTO{
	
	private Long id = 0L;
	private String portfolioCardArticle;
	private Integer portfolioCardStars;
	private String portfolioUrl;

	// setter
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
	
	// getter
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
	
	// constructor
	private PortfolioCardDTO(){
		
	}
	
	public PortfolioCardDTO(PortfolioCardDTO.Builder builder){
		this.id = builder.id;
		this.portfolioCardArticle = builder.portfolioCardArticle;
		this.portfolioCardStars = builder.portfolioCardStars;
		this.portfolioUrl = builder.portfolioUrl;
	}
	
	public static class Builder{
		
		private Long id;
		private String portfolioCardArticle;
		private Integer portfolioCardStars;
		private String portfolioUrl;
		
		public PortfolioCardDTO.Builder id(Long id){
			this.id = id;
			return this;
		}
		
		public PortfolioCardDTO.Builder portfolioCardArticle(String portfolioCardArticle){
			this.portfolioCardArticle = portfolioCardArticle;
			return this;
		}
		
		public PortfolioCardDTO.Builder portfolioCardStars(Integer portfolioCardStars){
			this.portfolioCardStars = portfolioCardStars;
			return this;
		}
		
		public PortfolioCardDTO.Builder portfolioUrl(String portfolioUrl){
			this.portfolioUrl = portfolioUrl;
			return this;
		}
		
		public PortfolioCardDTO.Builder portfolioCard(PortfolioCard portfolioCard){
			this.id = portfolioCard.getId();
			this.portfolioCardArticle = portfolioCard.getPortfolioCardArticle();
			this.portfolioCardStars = portfolioCard.getPortfolioCardStars();
			this.portfolioUrl = portfolioCard.getPortfolioUrl();
			return this;
		}
		
		public PortfolioCardDTO build(){
			return new PortfolioCardDTO(this);
		}
		
	}
	
}