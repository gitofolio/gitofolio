package com.gitofolio.api.domain.user;

import javax.persistence.*;

import java.util.*;

import java.time.LocalDate;

@Entity
@Table(name="USER_STATISTICS")
public class UserStatistics{
	
	@Id
	@GeneratedValue
	@Column(name="USER_STATISTICS_ID")
	private Long id;
	
	@OneToMany(mappedBy="userStatistics" ,fetch=FetchType.LAZY, orphanRemoval=true, cascade=CascadeType.ALL)
	@OrderBy("visitDate asc")
	private List<VisitorStatistics> visitorStatistics = new ArrayList<VisitorStatistics>();
	
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
	
	
	public void setRefferingSite(String refferingSite){
		while(refferingSites.size() > 30) refferingSites.remove(0);
		for(RefferingSite rfs : refferingSites){
			if(rfs.getRefferingSiteName().equals(refferingSite)) return;
		}
		this.refferingSites.add(new RefferingSite(refferingSite, this));
	}
	
	public void addVisitorStatistics(){
		Collections.sort(this.visitorStatistics);
		VisitorStatistics todayVisitorStatistics = this.getTodayVisitorStatistics();
		todayVisitorStatistics.addVisitorCount();
	}
	
	private VisitorStatistics getTodayVisitorStatistics(){
		if(this.visitorStatistics.isEmpty()) return this.createVisitorStatistics();
		if(this.visitorStatistics.size()==7){
			if(this.isVisitorStatisticsUpdated()) return this.visitorStatistics.get(this.getLastIndexOfVisitorStatistics());
			this.updateOldestVisitorStatistics();
			Collections.sort(this.visitorStatistics);
			return this.visitorStatistics.get(this.getLastIndexOfVisitorStatistics());
		}
		if(this.isVisitorStatisticsUpdated()) return this.visitorStatistics.get(this.getLastIndexOfVisitorStatistics());
		return this.createVisitorStatistics();
	}
	
	private VisitorStatistics createVisitorStatistics(){
		VisitorStatistics newVs = new VisitorStatistics(this);
		this.visitorStatistics.add(newVs);
		Collections.sort(this.visitorStatistics);
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