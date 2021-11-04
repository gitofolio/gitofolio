package com.gitofolio.api.domain.user;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
@Table(name="REFFERING_SITE")
public class RefferingSite{
	
	@Id
	@Column(name="REFFERING_SITE_NAME")
	private String refferingSiteName;
	
	@Column(name="REFFERING_DATE")
	private LocalDate refferingDate;
	
	@ManyToOne
	@JoinColumn(name="REFFERING_SITE")
	private UserStatistics userStatistics;
	
	{
		refferingDate = LocalDate.now();
	}
	
	protected RefferingSite(){}
	
	public RefferingSite(String refferingSiteName){
		this.refferingSiteName = refferingSiteName;
	}
	
	
	//getter
	public LocalDate getRefferingDate(){
		return this.refferingDate;
	}
	
	public String getRefferingSiteName(){
		return this.refferingSiteName;
	}
}