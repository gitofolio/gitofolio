package com.gitofolio.api.domain.user;

import javax.persistence.GeneratedValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;

import java.time.LocalDate;

@Entity
@Table(name="REFFERING_SITE")
public class RefferingSite{
	
	@Id
	@GeneratedValue
	@Column(name="REFFERING_SITE_ID")
	private Long id;
	
	@Column(name="REFFERING_SITE_NAME")
	private String refferingSiteName;
	
	@Column(name="REFFERING_DATE")
	private LocalDate refferingDate;
	
	@ManyToOne
	@JoinColumn(name="USER_STATISTICS_ID")
	private UserStatistics userStatistics;
	
	public RefferingSite(){}
	
	public RefferingSite(String refferingSiteName){
		this.refferingDate = LocalDate.now();
		this.refferingSiteName = refferingSiteName;
	}
	
	public RefferingSite(String refferingSiteName, LocalDate refferingDate){
		this.refferingDate = refferingDate;
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