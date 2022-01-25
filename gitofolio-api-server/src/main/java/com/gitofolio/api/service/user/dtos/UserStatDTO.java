package com.gitofolio.api.service.user.dtos;

import com.gitofolio.api.domain.user.UserStat;

public class UserStatDTO{
	private Integer totalVisitors;
	
	public void setTotalVisitors(Integer totalVisitors){
		this.totalVisitors = totalVisitors;
	}
	
	public Integer getTotalVisitors(){
		return this.totalVisitors;
	}
	
	public UserStatDTO(){
		
	}
	
	public UserStatDTO(UserStatDTO.Builder builder){
		this.totalVisitors = builder.totalVisitors;
	}
	
	public static class Builder{
		
		private Integer totalVisitors;
		
		public UserStatDTO.Builder totalVisitors(Integer totalVisitors){
			this.totalVisitors = totalVisitors;
			return this;
		}
		
		public UserStatDTO.Builder userStat(UserStat userStat){
			this.totalVisitors = userStat.getTotalVisitors();
			return this;
		}
		
		public UserStatDTO build(){
			return new UserStatDTO(this);
		}
		
	}
}