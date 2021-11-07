package com.gitofolio.api.service.user.dtos;

import com.gitofolio.api.domain.user.PortfolioCard;

public class PortfolioCardDTO{
	
	private final String portfolioCardArticle;
	private final Integer portfolioCardStars;
	private final String portfolioUrl;

	//getter
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
		throw new AssertionError("Call PortfolioCardDTO()");
	}
	
	public PortfolioCardDTO(PortfolioCardDTO.Builder builder){
		this.portfolioCardArticle = builder.portfolioCardArticle;
		this.portfolioCardStars = builder.portfolioCardStars;
		this.portfolioUrl = builder.portfolioUrl;
	}
	
	public static class Builder{
		
		private String portfolioCardArticle;
		private Integer portfolioCardStars;
		private String portfolioUrl;
		
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