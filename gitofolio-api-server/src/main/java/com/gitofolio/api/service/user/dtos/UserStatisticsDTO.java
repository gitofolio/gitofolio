package com.gitofolio.api.service.user.dtos;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

import com.gitofolio.api.domain.user.UserStatistics;
import com.gitofolio.api.domain.user.VisitorStatistics;
import com.gitofolio.api.domain.user.RefferingSite;

public class UserStatisticsDTO{
	
	public static class RefferingSiteDTO{
		private final String refferingSiteName;
		private final LocalDate refferingDate;
		
		// getter
		public String getRefferingSiteName(){
			return this.refferingSiteName;
		}
		
		public LocalDate getRefferingDate(){
			return this.refferingDate;
		}
		
		//constructor
		private RefferingSiteDTO(String refferingSiteName, LocalDate refferingDate){
			this.refferingSiteName = refferingSiteName;
			this.refferingDate = refferingDate;
		}
	}
	
	public static class VisitorStatisticsDTO{
		private final LocalDate visitDate;	
		private final int visitorCount;
		
		// getter
		public LocalDate getVisitDate(){
			return this.visitDate;
		}
		
		public int getVisitorCount(){
			return this.visitorCount;
		}
		
		//constructor
		private VisitorStatisticsDTO(LocalDate visitDate, int visitorCount){
			this.visitDate = visitDate;
			this.visitorCount = visitorCount;
		}
	}
	
	private final List<VisitorStatisticsDTO> visitorStatistics;
	private final List<RefferingSiteDTO> refferingSites;
	
	// getter
	public List<VisitorStatisticsDTO> getVisitorStatisticsDTOs(){
		return this.visitorStatistics;
	}
	
	public List<RefferingSiteDTO> getRefferingSiteDTOs(){
		return this.refferingSites;
	}
	
	// constructor
	private UserStatisticsDTO(){
		throw new AssertionError("Call UserStatisticsDTO()");
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