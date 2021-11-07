package com.gitofolio.api.domain.user;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;

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
	
	//getter
	
	public VisitorStatistics(){
		this.visitDate = LocalDate.now();
		this.visitorCount=0;
	}
	
	public VisitorStatistics(LocalDate visitDate, int visitorCount){
		this.visitDate = visitDate;
		this.visitorCount = visitorCount;
	}
	
	public LocalDate getVisitDate(){
		return this.visitDate;
	}
	
	public int getVisitorCount(){
		return this.visitorCount;
	}
	
	//setter
	public void setVisitorCount(int visitorCount){
		this.visitorCount = visitorCount;
	}
	
}