package com.gitofolio.api.service.user.svg.portfoliocard;

import com.gitofolio.api.service.user.svg.color.colors.Color;

public class PortfolioCardSvgDTO{
	
	private String viewBoxWidth = "353";
	private String viewBoxHeight = "102";
	private String rectWidth = "345"; // viewBoxWidth-8
	private String rectHeight = "94"; // viewBoxHeight-8
	private String arrowY = "-112";
	private String name;
	private String starNum;
	private String profileUrl;
	private String article;
	private String base64EncodedImage;
	private Color color;
	
	public String getBase64EncodedImage(){
		return this.base64EncodedImage;
	}
	
	public String getViewBoxHeight(){
		return this.viewBoxHeight;
	}
	
	public String getViewBoxWidth(){
		return this.viewBoxWidth;
	}
	
	public String getRectWidth(){
		return this.rectWidth;
	}
	
	public String getRectHeight(){
		return this.rectHeight;
	}
	
	public String getArrowY(){
		return this.arrowY;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getStarNum(){
		return this.starNum;
	}
	
	public String getProfileUrl(){
		return this.profileUrl;
	}
	
	public String getArticle(){
		return this.article;
	}
	
	public Color getColor(){
		return this.color;
	}
	
	// constructor
	public PortfolioCardSvgDTO(Builder builder){
		this.viewBoxHeight = builder.viewBoxHeight;
		this.viewBoxWidth = builder.viewBoxWidth;
		this.rectWidth = builder.rectWidth;
		this.rectHeight = builder.rectHeight;
		this.arrowY = builder.arrowY;
		this.name = builder.name;
		this.starNum = builder.starNum;
		this.profileUrl = builder.profileUrl;
		this.article = builder.article;
		this.color = builder.color;
	}
	
	public static class Builder{
		
		private String viewBoxWidth = "353";
		private String viewBoxHeight = "102";
		private String rectWidth = "345";
		private String rectHeight = "94";
		private String arrowY = "-112";
		private String name;
		private String starNum;
		private String profileUrl;
		private String article;
		private String base64EncodedImage;
		private Color color;
		
		public Builder viewBoxWidth(String viewBoxWidth){
			this.viewBoxWidth = viewBoxWidth;
			return this;
		}
		
		public Builder name(String name){
			this.name = name;
			return this;
		}
		
		public Builder starNum(String starNum){
			this.starNum = starNum;
			return this;
		}
		
		public Builder profileUrl(String profileUrl){
			this.profileUrl = profileUrl;
			return this;
		}
		
		public Builder article(String article){
			int textY = article.lastIndexOf("<text");
			String height = "";
			for(int i = textY; article.charAt(i) != '>'; i++)
				if((int)article.charAt(i)-(int)'0' >= 0 && (int)article.charAt(i)-(int)'0' <= 9) height += article.charAt(i);
			
			int resultHeight = Integer.parseInt(height);
			
			this.viewBoxHeight = String.valueOf(resultHeight+118);
			this.rectHeight = String.valueOf(resultHeight+110);
			this.arrowY = String.valueOf(resultHeight-96);
			this.article = article;
			return this;
		}
		
		public Builder base64EncodedImage(String base64EncodedImage){
			this.base64EncodedImage = base64EncodedImage;
			return this;
		}
		
		public Builder color(Color color){
			this.color = color;
			return this;
		}
		
		public PortfolioCardSvgDTO build(){
			return new PortfolioCardSvgDTO(this);
		}
		
	}
	
}