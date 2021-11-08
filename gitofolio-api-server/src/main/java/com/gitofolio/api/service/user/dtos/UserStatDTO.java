package com.gitofolio.api.service.user.dtos;

import com.gitofolio.api.domain.user.UserStat;

public class UserStatDTO{
	private Integer totalVisitors;
	private Integer totalStars;
	
	// setter
	public void setTotalVisitors(Integer totalVisitors){
		this.totalVisitors = totalVisitors;
	}
	
	public void setTotalStars(Integer totalStars){
		this.totalStars = totalStars;
	}
	
	// getter
	public Integer getTotalVisitors(){
		return this.totalVisitors;
	}
	
	public Integer getTotalStars(){
		return this.totalStars;
	}
	
	// constructor
	public UserStatDTO(){
		
	}
	
	public UserStatDTO(UserStatDTO.Builder builder){
		this.totalVisitors = builder.totalVisitors;
		this.totalStars = builder.totalStars;
	}
	
	public static class Builder{
		
		private Integer totalVisitors;
		private Integer totalStars;
		
		public UserStatDTO.Builder totalVisitors(Integer totalVisitors){
			this.totalVisitors = totalVisitors;
			return this;
		}
		
		public UserStatDTO.Builder totalStars(Integer totalStars){
			this.totalStars = totalStars;
			return this;
		}
		
		public UserStatDTO.Builder userStat(UserStat userStat){
			this.totalVisitors = userStat.getTotalVisitors();
			this.totalStars = userStat.getTotalStars();
			return this;
		}
		
		public UserStatDTO build(){
			return new UserStatDTO(this);
		}
		
	}
}