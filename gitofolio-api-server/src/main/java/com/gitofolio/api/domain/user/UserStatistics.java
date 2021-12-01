package com.gitofolio.api.domain.user;

import javax.persistence.GeneratedValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;

import java.util.List;
import java.util.ArrayList;

import java.time.LocalDate;

@Entity
@Table(name="USER_STATISTICS")
public class UserStatistics{
	
	@Id
	@GeneratedValue
	@Column(name="USER_STATISTICS_ID")
	private Long id;
	
	// @Column(name="VISITOR_STATISTICS")
	@OneToMany(mappedBy="userStatistics" ,fetch=FetchType.LAZY, orphanRemoval=true, cascade=CascadeType.ALL)
	// @JoinColumn(name="USER_STATISTICS_ID")
	private List<VisitorStatistics> visitorStatistics = new ArrayList<VisitorStatistics>();
	
	// @Column(name="REFFERING_SITE")
	@OneToMany(mappedBy="userStatistics", fetch=FetchType.EAGER, orphanRemoval=true, cascade=CascadeType.ALL)
	// @JoinColumn(name="USER_STATISTICS_ID")
	private List<RefferingSite> refferingSites = new ArrayList<RefferingSite>();
	
	@OneToOne
	@JoinColumn(name="USER_INFO_ID", unique=true)
	private UserInfo userInfo;
	
	//getter
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
	
	//setter
	public void setVisitorStatistics(){
		if(visitorStatistics.size() > 7) visitorStatistics.remove(0);
		VisitorStatistics newVs = null;
		for(VisitorStatistics vs : visitorStatistics){
			if(vs.getVisitDate().toString().equals(LocalDate.now().toString())) newVs = vs;
		}
		if(newVs == null) {
			while(visitorStatistics.size() >= 7) visitorStatistics.remove(0);
			newVs = new VisitorStatistics(this);
			this.visitorStatistics.add(newVs);
		}
		newVs.setVisitorCount(newVs.getVisitorCount()+1);
	}
	
	public void setVisitorStatistics(int visitorCount){
		if(visitorStatistics.size() > 7) visitorStatistics.remove(0);
		VisitorStatistics newVs = null;
		for(VisitorStatistics vs : visitorStatistics){
			if(vs.getVisitDate().toString().equals(LocalDate.now().toString())) newVs = vs;
		}
		if(newVs == null) {
			while(visitorStatistics.size() >= 7) visitorStatistics.remove(0);
			newVs = new VisitorStatistics(this);
			this.visitorStatistics.add(newVs);
		}
		newVs.setVisitorCount(visitorCount);
	}
	
	public void addVisitorStatistics(){
		if(visitorStatistics.size() > 7) visitorStatistics.remove(0);
		VisitorStatistics newVs = null;
		for(VisitorStatistics vs : visitorStatistics){
			if(vs.getVisitDate().toString().equals(LocalDate.now().toString())) newVs = vs;
		}
		if(newVs == null) {
			while(visitorStatistics.size() >= 7) visitorStatistics.remove(0);
			newVs = new VisitorStatistics(this);
			newVs.setVisitorCount(1);
			this.visitorStatistics.add(newVs);
		}
		else newVs.setVisitorCount(newVs.getVisitorCount()+1);
	}
	
	public void setRefferingSite(String refferingSite){
		while(refferingSites.size() > 30) refferingSites.remove(0);
		for(RefferingSite rfs : refferingSites){
			if(rfs.getRefferingSiteName().equals(refferingSite)) return;
		}
		this.refferingSites.add(new RefferingSite(refferingSite, this));
	}
	
	public void setUserInfo(UserInfo userInfo){
		if(userInfo == null) this.userInfo = null;
		this.userInfo = userInfo;
	}
	
}