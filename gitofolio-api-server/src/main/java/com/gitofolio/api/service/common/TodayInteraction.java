package com.gitofolio.api.service.common;

import org.springframework.stereotype.Service;

import java.math.*;
import java.time.*;

@Service
public class TodayInteraction{

	private BigInteger interactCount;
	private LocalDate date;
	private BigInteger one;
	private Object lockForIncrease;
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
		this.interactCount = new BigInteger("0");
		this.one = new BigInteger("1");
		this.date = LocalDate.now();
		this.lockForIncrease = new Object();
		this.lockForInitDate = new Object();
	}

}
