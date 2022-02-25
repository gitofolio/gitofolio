package com.gitofolio.api.service.user.dtos;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

import com.gitofolio.api.domain.user.UserStatistics;
import com.gitofolio.api.domain.user.VisitorStatistics;
import com.gitofolio.api.domain.user.RefferingSite;

import org.springframework.format.annotation.DateTimeFormat;

public class UserStatisticsDTO{
	
	public static class RefferingSiteDTO{
		private String refferingSiteName;
		private LocalDate refferingDate;
		
		public void setRefferingSiteName(String refferingSiteName){
			this.refferingSiteName = refferingSiteName;
		}
		
		public void setRefferingDate(LocalDate refferingDate){
			this.refferingDate = refferingDate;
		}
		
		public String getRefferingSiteName(){
			return this.refferingSiteName;
		}
		
		public LocalDate getRefferingDate(){
			return this.refferingDate;
		}
		
		public RefferingSiteDTO(){
			
		}
		
		private RefferingSiteDTO(String refferingSiteName, LocalDate refferingDate){
			this.refferingSiteName = refferingSiteName;
			this.refferingDate = refferingDate;
		}
	}
	
	public static class VisitorStatisticsDTO{
		private LocalDate visitDate;	
		private int visitorCount;
		
		public void setVisitDate(LocalDate visitDate){
			this.visitDate = visitDate;
		}
		
		public void setVisitorCount(int visitorCount){
			this.visitorCount = visitorCount;
		}
		
		public LocalDate getVisitDate(){
			return this.visitDate;
		}
		
		public int getVisitorCount(){
			return this.visitorCount;
		}
		
		public VisitorStatisticsDTO(){
			
		}
		
		private VisitorStatisticsDTO(LocalDate visitDate, int visitorCount){
			this.visitDate = visitDate;
			this.visitorCount = visitorCount;
		}
	}
	
	private List<VisitorStatisticsDTO> visitorStatistics;
	private List<RefferingSiteDTO> refferingSites;
	
	public void setVisitorStatistics(List<VisitorStatisticsDTO> visitorStatistics){
		this.visitorStatistics = visitorStatistics;
	}
	
	public void setRefferingSites(List<RefferingSiteDTO> refferingSites){
		this.refferingSites = refferingSites;
	}
	
	public List<VisitorStatisticsDTO> getVisitorStatistics(){
		return this.visitorStatistics;
	}
	
	public List<RefferingSiteDTO> getRefferingSites(){
		return this.refferingSites;
	}
	
	public UserStatisticsDTO(){
		
	}
	
	public UserStatisticsDTO(UserStatisticsDTO.Builder builder){
		this.visitorStatistics = builder.visitorStatistics;
		this.refferingSites = builder.refferingSites;
	}
	
	public static class Builder{
		
		private List<VisitorStatisticsDTO> visitorStatistics;
		private List<RefferingSiteDTO> refferingSites;
		
		{
			visitorStatistics = new ArrayList<VisitorStatisticsDTO>();
			refferingSites = new ArrayList<RefferingSiteDTO>();
		}
		
		public UserStatisticsDTO.Builder visitorStatistics(VisitorStatistics visitorStatistics){
			this.visitorStatistics.add(
				new VisitorStatisticsDTO(visitorStatistics.getVisitDate(), visitorStatistics.getVisitorCount())
			);
			return this;
		}
		
		public UserStatisticsDTO.Builder refferingSites(RefferingSite refferingSite){
			this.refferingSites.add(
				new RefferingSiteDTO(refferingSite.getRefferingSiteName(), refferingSite.getRefferingDate())
			);
			return this;
		}
		
		public UserStatisticsDTO.Builder userStatistics(UserStatistics userStatistics){
			List<VisitorStatistics> visitorStatisticsEntity = userStatistics.getVisitorStatistics();
			List<RefferingSite> refferingSitesEntity = userStatistics.getRefferingSites();
			for(VisitorStatistics iter : visitorStatisticsEntity) this.visitorStatistics(iter);
			for(RefferingSite iter : refferingSitesEntity) this.refferingSites(iter);
			return this;
		}
		
		public UserStatisticsDTO build(){
			return new UserStatisticsDTO(this);
		}
		
	}
}