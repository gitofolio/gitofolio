package com.gitofolio.api.domain.common;

import javax.persistence.*;

import java.math.*;
import java.time.*;

@Entity
@Cacheable(true)
public class TodayInteraction{

	@Id
	private String id;
	
	@Column(name="interactCount")
	private BigInteger interactCount;
	
	@Column(name="date")
	private LocalDate date;
	
	@Transient
	private BigInteger one;
	
	@Transient
	private Object lockForIncrease;
	
	@Transient
	private Object lockForInitDate;
	
	public void increaseInteractCount(){
		this.initDate();
		synchronized(this.lockForIncrease){
			this.interactCount = this.interactCount.add(one);
		}
	}
	
	public String getInteractCount(){
		this.initDate();
		return this.interactCount.toString();
	}

	private void initDate(){
		if(!this.isToday()){
			synchronized(this.lockForInitDate){
				if(!this.isToday()){ // 다른 스레드에 의해서 초기화 되었을수도 있기때문에 한번 더 체크해줌
					this.date = LocalDate.now();
					this.initInteractCount();
				}
			}
		}
	}

	private boolean isToday(){
		return (this.date.toString().equals(LocalDate.now().toString())) ? true : false;
	}

	private void initInteractCount(){
		this.interactCount = new BigInteger("0");
	}

	public TodayInteraction(){
		this.id = "t";
		this.interactCount = new BigInteger("0");
		this.one = new BigInteger("1");
		this.date = LocalDate.now();
		this.lockForIncrease = new Object();
		this.lockForInitDate = new Object();
	}

}
