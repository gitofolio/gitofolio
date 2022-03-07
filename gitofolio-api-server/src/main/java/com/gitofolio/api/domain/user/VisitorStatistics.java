package com.gitofolio.api.domain.user;

import javax.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="VISITOR_STATISTICS")
public class VisitorStatistics{
	
	@Id
	@GeneratedValue
	@Column(name="VISITOR_STATISTICS_ID")
	private Long id;
	
	@Column(name="VISIT_DATE")
	private LocalDate visitDate;
	
	@Column(name="VISITOR_COUNT")
	private int visitorCount;
	
	@ManyToOne
	@JoinColumn(name="USER_STATISTICS_ID")
	private UserStatistics userStatistics;
	
	public VisitorStatistics(){}
	
	public VisitorStatistics(UserStatistics userStatistics){
		this.visitDate = LocalDate.now();
		this.visitorCount=0;
		this.userStatistics = userStatistics;
	}
	
	public VisitorStatistics(LocalDate visitDate, int visitorCount, UserStatistics userStatistics){
		this.visitDate = visitDate;
		this.visitorCount = visitorCount;
		this.userStatistics = userStatistics;
	}
	
	public boolean isVisitDateToday(){
		return this.visitDate.isEqual(LocalDate.now());
	}
	
	public void updateVisitDate(){
		this.visitDate = LocalDate.now();
		this.visitorCount = 0;
	}
	
	public LocalDate getVisitDate(){
		return this.visitDate;
	}
	
	public void addVisitorCount(){
		this.visitorCount++;
	}
	
	public int getVisitorCount(){
		return this.visitorCount;
	}
	
	public void setVisitorCount(int visitorCount){
		this.visitorCount = visitorCount;
	}
	
	public void setUserStatistics(UserStatistics userStatistics){
		this.userStatistics = userStatistics;
	}
	
}