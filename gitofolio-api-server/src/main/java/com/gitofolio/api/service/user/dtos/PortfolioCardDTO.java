package com.gitofolio.api.service.user.dtos;

import com.gitofolio.api.domain.user.PortfolioCard;
import com.gitofolio.api.service.user.exception.*;

public class PortfolioCardDTO{
	
	private Long id;
	private String portfolioCardArticle;
	private Integer portfolioCardWatched;
	private String portfolioUrl;
	
	public void setId(Long id){
		this.id = id;
	}
	
	public void setPortfolioCardArticle(String portfolioCardArticle){
		if(this.isPortfolioCardArticleNull(portfolioCardArticle)) this.portfolioCardArticle = " ";
		else this.portfolioCardArticle = portfolioCardArticle;
	}
	
	private boolean isPortfolioCardArticleNull(String portfolioCardArticle){
		return portfolioCardArticle == null ? true : false;
	}
	
	public void setPortfolioCardWatched(Integer portfolioCardWatched){
		this.portfolioCardWatched = portfolioCardWatched;
	}
	
	public void setPortfolioUrl(String portfolioUrl){
		if(this.isPortfolioUrlNull(portfolioUrl)) throw new IllegalParameterException("잘못된 파라미터가 전달되었습니다.", "portfolioCard내부의 portfolio url이 비어있습니다.");
		this.portfolioUrl = portfolioUrl;
	}
	
	private boolean isPortfolioUrlNull(String portfolioUrl){
		return portfolioUrl == null ? true : false;
	}
	
	public Long getId(){
		return this.id;
	}
	
	public String getPortfolioCardArticle(){
		return this.portfolioCardArticle;
	}
	
	public Integer getPortfolioCardWatched(){
		return this.portfolioCardWatched;
	}
	
	public String getPortfolioUrl(){
		return this.portfolioUrl;
	}
	
	private PortfolioCardDTO(){
		
	}
	
	public PortfolioCardDTO(PortfolioCardDTO.Builder builder){
		this.id = builder.id;
		this.portfolioCardArticle = builder.portfolioCardArticle;
		this.portfolioCardWatched = builder.portfolioCardWatched;
		this.portfolioUrl = builder.portfolioUrl;
	}
	
	public static class Builder{
		
		private Long id;
		private String portfolioCardArticle;
		private Integer portfolioCardWatched;
		private String portfolioUrl;
		
		public PortfolioCardDTO.Builder id(Long id){
			this.id = id;
			return this;
		}
		
		public PortfolioCardDTO.Builder portfolioCardArticle(String portfolioCardArticle){
			this.portfolioCardArticle = portfolioCardArticle;
			return this;
		}
		
		public PortfolioCardDTO.Builder portfolioCardWatched(Integer portfolioCardWatched){
			this.portfolioCardWatched = portfolioCardWatched;
			return this;
		}
		
		public PortfolioCardDTO.Builder portfolioUrl(String portfolioUrl){
			this.portfolioUrl = portfolioUrl;
			return this;
		}
		
		public PortfolioCardDTO.Builder portfolioCard(PortfolioCard portfolioCard){
			this.id = portfolioCard.getId();
			this.portfolioCardArticle = portfolioCard.getPortfolioCardArticle();
			this.portfolioCardWatched = portfolioCard.getPortfolioCardWatched();
			this.portfolioUrl = portfolioCard.getPortfolioUrl();
			return this;
		}
		
		public PortfolioCardDTO build(){
			return new PortfolioCardDTO(this);
		}
		
	}
	
}