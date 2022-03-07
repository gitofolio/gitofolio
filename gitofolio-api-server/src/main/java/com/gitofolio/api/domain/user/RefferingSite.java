package com.gitofolio.api.domain.user;

import javax.persistence.*;

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
	
	public boolean isSameRefferingSiteName(String refferingSiteName){
		return this.refferingSiteName.equals(refferingSiteName);
	}
	
	public RefferingSite(){}
	
	public RefferingSite(String refferingSiteName, UserStatistics userStatistics){
		this.updateRefferingSite(refferingSiteName, LocalDate.now(), userStatistics);
	}
	
	public RefferingSite(String refferingSiteName, LocalDate refferingDate, UserStatistics userStatistics){
		this.updateRefferingSite(refferingSiteName, refferingDate, userStatistics);
	}
	
	public void updateRefferingSite(String refferingSiteName, LocalDate refferingDate, UserStatistics userStatistics){
		this.refferingDate = refferingDate;
		this.refferingSiteName = refferingSiteName;
		this.userStatistics = userStatistics;
	}
	
	public LocalDate getRefferingDate(){
		return this.refferingDate;
	}
	
	public String getRefferingSiteName(){
		return this.refferingSiteName;
	}
	
}