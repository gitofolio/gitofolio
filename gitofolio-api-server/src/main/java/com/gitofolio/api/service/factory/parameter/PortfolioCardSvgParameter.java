package com.gitofolio.api.service.factory.parameter;

import com.gitofolio.api.aop.svg.SvgBreakAble;

public class PortfolioCardSvgParameter implements ParameterAble, SvgBreakAble{
	
	private String name;
	private String encodedImage;
	private String portfolioUrl; 
	private Integer watchedNum;
	private String article;
	private String colorName;
	
	@Override
	public String breakTarget(){
		return this.article;
	}
	
	@Override
	public void setBreakedString(String string){
		this.article = string;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getEncodedImage(){
		return this.encodedImage;
	}
	
	public String getPortfolioUrl(){
		return this.portfolioUrl;
	}
	
	public Integer getWatchedNum(){
		return this.watchedNum;
	}
	
	public String getArticle(){
		return this.article;
	}
	
	public String getColorName(){
		return this.colorName;
	}
	
	// constructor
	
	public PortfolioCardSvgParameter(Builder builder){
		this.name = builder.name;
		this.encodedImage = builder.encodedImage;
		this.portfolioUrl = builder.portfolioUrl; 
		this.watchedNum = builder.watchedNum;
		this.article = builder.article;
		this.colorName = builder.colorName;
	}
	
	public static class Builder{
		private String name;
		private String encodedImage;
		private String portfolioUrl; 
		private Integer watchedNum;
		private String article;
		private String colorName;
		
		public Builder name(String name){
			this.name = name;
			return this;
		}
		
		public Builder encodedImage(String encodedImage){
			this.encodedImage = encodedImage;
			return this;
		}
		
		public Builder portfolioUrl(String portfolioUrl){
			this.portfolioUrl = portfolioUrl;
			return this;
		}
		
		public Builder watchedNum(Integer watchedNum){
			this.watchedNum = watchedNum;
			return this;
		}
		
		public Builder article(String article){
			this.article = article;
			return this;
		}
		
		public Builder colorName(String colorName){
			this.colorName = colorName;
			return this;
		}
		
		public PortfolioCardSvgParameter build(){
			return new PortfolioCardSvgParameter(this);
		}
	}

}