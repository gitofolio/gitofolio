package com.gitofolio.api.domain.user;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
@Table(name="VISITOR_STATISTICS")
public class VisitorStatistics{
	
	@Id
	@Column(name="VISIT_DATE", unique=true)
	private LocalDate visitDate;
	
	@Column(name="VISITOR_COUNT")
	private int visitorCount;
	
	@ManyToOne
	@JoinColumn(name="VISITOR_STATISTICS")
	private UserStatistics userStatistics;
	
	{
		visitDate = LocalDate.now();
		visitorCount=0;
	}
	
	//getter
	
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