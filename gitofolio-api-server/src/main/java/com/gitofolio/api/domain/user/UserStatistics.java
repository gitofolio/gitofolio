package com.gitofolio.api.domain.user;

import javax.persistence.*;

import org.hibernate.annotations.BatchSize;
	
import java.util.*;

import java.time.LocalDate;

@Entity
@Table(name="USER_STATISTICS")
public class UserStatistics{
	
	@Id
	@GeneratedValue
	@Column(name="USER_STATISTICS_ID")
	private Long id;
	
	@BatchSize(size=30)
	@OneToMany(mappedBy="userStatistics", fetch=FetchType.LAZY, orphanRemoval=true, cascade=CascadeType.ALL)
	@OrderBy("visitDate asc")
	private List<VisitorStatistics> visitorStatistics = new ArrayList<VisitorStatistics>();
	
	@BatchSize(size=50)
	@OneToMany(mappedBy="userStatistics", fetch=FetchType.EAGER, orphanRemoval=true, cascade=CascadeType.ALL)
	@OrderBy("refferingDate asc")
	private List<RefferingSite> refferingSites = new ArrayList<RefferingSite>();
	
	@OneToOne
	@JoinColumn(name="USER_INFO_ID")
	private UserInfo userInfo;
	
	public Long getId(){
		return this.id;
	}
	
	public List<VisitorStatistics> getVisitorStatistics(){
		return this.visitorStatistics;
	}
	
	public List<RefferingSite> getRefferingSites(){
		return this.refferingSites;
	}
	
	public UserInfo getUserInfo(){
		return this.userInfo;
	}
	
	
	public void setRefferingSite(String refferingSiteName){
		if(this.isRefferingSiteAlreadyExist(refferingSiteName)) return;
		if(this.isRefferingSiteTableFull()){
			RefferingSite refferingSite = this.refferingSites.get(0);
			refferingSite.updateRefferingSite(refferingSiteName, LocalDate.now(), this);
			return;
		}
		this.refferingSites.add(new RefferingSite(refferingSiteName, this));
	}
	
	private boolean isRefferingSiteAlreadyExist(String refferingSiteName){
		for(RefferingSite rfs : this.refferingSites) 
			if(rfs.isSameRefferingSiteName(refferingSiteName)) return true;
		return false;
	}
	
	private boolean isRefferingSiteTableFull(){
		return this.refferingSites.size() >= 30;
	}
	
	public void addVisitorStatistics(){
		VisitorStatistics todayVisitorStatistics = this.getTodayVisitorStatistics();
		todayVisitorStatistics.addVisitorCount();
	}
	
	private VisitorStatistics getTodayVisitorStatistics(){
		if(this.visitorStatistics.isEmpty()) return this.createVisitorStatistics();
		if(this.visitorStatistics.size()>=7){
			if(this.isVisitorStatisticsUpdated()) return this.visitorStatistics.get(this.getLastIndexOfVisitorStatistics());
			this.updateOldestVisitorStatistics();
			return this.visitorStatistics.get(this.getLastIndexOfVisitorStatistics());
		}
		if(this.isVisitorStatisticsUpdated()) return this.visitorStatistics.get(this.getLastIndexOfVisitorStatistics());
		return this.createVisitorStatistics();
	}
	
	private VisitorStatistics createVisitorStatistics(){
		VisitorStatistics newVs = new VisitorStatistics(this);
		this.visitorStatistics.add(newVs);
		return this.visitorStatistics.get(this.getLastIndexOfVisitorStatistics());
	}
	
	private boolean isVisitorStatisticsUpdated(){
		return this.visitorStatistics.get(this.getLastIndexOfVisitorStatistics()).isVisitDateToday();
	}
	
	private int getLastIndexOfVisitorStatistics(){
		return this.visitorStatistics.size()-1;
	}
	
	private void updateOldestVisitorStatistics(){
		this.visitorStatistics.get(0).updateVisitDate();
	}
	
	public void setUserInfo(UserInfo userInfo){
		if(userInfo == null) this.userInfo = null;
		this.userInfo = userInfo;
	}
	
}