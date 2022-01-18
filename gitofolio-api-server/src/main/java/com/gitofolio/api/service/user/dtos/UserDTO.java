package com.gitofolio.api.service.user.dtos;

import java.util.List;
import java.util.ArrayList;

import com.gitofolio.api.domain.user.UserInfo;
import com.gitofolio.api.service.factory.hateoas.Hateoas.Link;
import com.gitofolio.api.service.auth.token.TokenAble;
import com.gitofolio.api.service.factory.hateoas.HateoasAble;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDTO implements HateoasAble, TokenAble{
	
	private Long id;
	private String name;
	private String profileUrl;
	private List<PortfolioCardDTO> portfolioCards;
	private UserStatDTO userStat;
	private UserStatisticsDTO userStatistics;
	private List<Link> links;
	
	// setter
	public void setId(Long id){
		this.id = id;
	}
	
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
	
	@Override
	public void setLinks(List<Link> links){
		this.links = links;
	}
	
	@Override
	public String token(){
		return this.name;
	}
	
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
	
	public List<PortfolioCardDTO> getPortfolioCards(){
		return this.portfolioCards;
	}
	
	public UserStatDTO getUserStat(){
		return this.userStat;
	}
	
	@JsonProperty("userDailyStat")
	public UserStatisticsDTO getUserStatistics(){
		return this.userStatistics;
	}
	
	public List<Link> getLinks(){
		return this.links;
	}
	
	// logic
	public boolean compare(UserDTO userDTO){
		if(!this.name.equals(userDTO.getName())) return false;
		if(!this.id.equals(userDTO.getId())) return false;
		if(!this.profileUrl.equals(userDTO.getProfileUrl())) return false;
		return true;
	}
	
	// constructor
	public UserDTO(){
		
	}
	
	public UserDTO(UserDTO.Builder builder){
		this.id = builder.id;
		this.name = builder.name;
		this.profileUrl = builder.profileUrl;
		this.portfolioCards = builder.portfolioCards;
		this.userStat = builder.userStat;
		this.userStatistics = builder.userStatistics;
	}
	
	public static class Builder{
		
		private Long id;
		private String name;
		private String profileUrl;
		private List<PortfolioCardDTO> portfolioCards;
		private UserStatDTO userStat;
		private UserStatisticsDTO userStatistics;
		
		public UserDTO.Builder id(Long id){
			this.id = id;
			return this;
		}
		
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
			this.id = userInfo.getId();
			this.name = userInfo.getName();
			this.profileUrl = userInfo.getProfileUrl();
			return this;
		}
		
		public UserDTO build(){
			return new UserDTO(this);
		}
		
	}
	
}