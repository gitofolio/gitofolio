package com.gitofolio.api.service.user.dtos;

import java.util.List;
import java.util.ArrayList;

import com.gitofolio.api.domain.user.UserInfo;
import com.gitofolio.api.service.user.factory.hateoas.Hateoas.Link;

public class UserDTO{
	
	private String name;
	private String profileUrl;
	private List<PortfolioCardDTO> portfolioCards;
	private UserStatDTO userStat;
	private UserStatisticsDTO userStatistics;
	private List<Link> links;
	
	// setter
	public void setName(String name){
		this.name = name;
	}
	
	public void setProfileUrl(String profileUrl){
		this.profileUrl = profileUrl;
	}
	
	public void setPortfolioCards(List<PortfolioCardDTO> portfolioCards){
		this.portfolioCards = portfolioCards;
	}
	
	public void setUserStat(UserStatDTO userStat){
		this.userStat = userStat;
	}
	
	public void setUserStatistics(UserStatisticsDTO userStatistics){
		this.userStatistics = userStatistics;
	}
	
	public void setLinks(List<Link> links){
		this.links = links;
	}
	
	// getter
	public String getName(){
		return this.name;
	}
	
	public String getProfileUrl(){
		return this.profileUrl;
	}
	
	public List<PortfolioCardDTO> getPortfolioCards(){
		return this.portfolioCards;
	}
	
	public UserStatDTO getUserStat(){
		return this.userStat;
	}
	
	public UserStatisticsDTO getUserStatistics(){
		return this.userStatistics;
	}
	
	public List<Link> getLinks(){
		return this.links;
	}
	
	// constructor
	public UserDTO(){
		
	}
	
	public UserDTO(UserDTO.Builder builder){
		this.name = builder.name;
		this.profileUrl = builder.profileUrl;
		this.portfolioCards = builder.portfolioCards;
		this.userStat = builder.userStat;
		this.userStatistics = builder.userStatistics;
	}
	
	public static class Builder{
		
		private String name;
		private String profileUrl;
		private List<PortfolioCardDTO> portfolioCards;
		private UserStatDTO userStat;
		private UserStatisticsDTO userStatistics;
		
		public UserDTO.Builder name(String name){
			this.name = name;
			return this;
		}
		
		public UserDTO.Builder profileUrl(String profileUrl){
			this.profileUrl = profileUrl;
			return this;
		}
		
		public UserDTO.Builder portfolioCardDTOs(List<PortfolioCardDTO> portfolioCards){
			this.portfolioCards = portfolioCards;
			return this;
		}
		
		public UserDTO.Builder portfolioCardDTO(PortfolioCardDTO portfolioCard){
			if(this.portfolioCards == null) this.portfolioCards = new ArrayList<PortfolioCardDTO>();
			this.portfolioCards.add(portfolioCard);
			return this;
		}
		
		public UserDTO.Builder userStatDTO(UserStatDTO userStat){
			this.userStat = userStat;
			return this;
		}
		
		public UserDTO.Builder userStatisticsDTO(UserStatisticsDTO userStatistics){
			this.userStatistics = userStatistics;
			return this;
		}
		
		public UserDTO.Builder userInfo(UserInfo userInfo){
			this.name = userInfo.getName();
			this.profileUrl = userInfo.getProfileUrl();
			return this;
		}
		
		public UserDTO build(){
			return new UserDTO(this);
		}
		
	}
	
}