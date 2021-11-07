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
	
	@Column(name="VISITOR_STATISTICS")
	@OneToMany(mappedBy="userStatistics", fetch=FetchType.LAZY, orphanRemoval=true, cascade=CascadeType.ALL)
	private List<VisitorStatistics> visitorStatistics = new ArrayList<VisitorStatistics>();
	
	@Column(name="REFFERING_SITE")
	@OneToMany(mappedBy="userStatistics", fetch=FetchType.LAZY, orphanRemoval=true, cascade=CascadeType.ALL)
	private List<RefferingSite> refferingSites = new ArrayList<RefferingSite>();
	
	@OneToOne
	@JoinColumn(name="USER_INFO_ID")
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
			newVs = new VisitorStatistics();
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
			newVs = new VisitorStatistics();
			this.visitorStatistics.add(newVs);
		}
		newVs.setVisitorCount(visitorCount);
	}
	
	public void addVisitorStatistics(){
		this.setVisitorStatistics();
	}
	
	public void setRefferingSite(String refferingSite){
		for(RefferingSite rfs : refferingSites){
			if(rfs.getRefferingSiteName().equals(refferingSite)) return;
		}
		this.refferingSites.add(new RefferingSite(refferingSite));
	}
	
	public void setRefferingSite(String refferingSite, LocalDate visitDate){
		for(RefferingSite rfs : refferingSites){
			if(rfs.getRefferingSiteName().equals(refferingSite)) return;
		}
		this.refferingSites.add(new RefferingSite(refferingSite));
	}
	
	public void setUserInfo(UserInfo userInfo){
		if(userInfo == null) this.userInfo = null;
		// if(this.userInfo != null) this.userInfo.setUserStatistics(null);
		this.userInfo = userInfo;
		// if(userInfo.getUserStatistics() != this) userInfo.setUserStatistics(this);
	}
	
}