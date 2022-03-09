package com.gitofolio.api.service.common;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.gitofolio.api.domain.common.TodayInteraction;
import com.gitofolio.api.repository.common.TodayInteractionRepository;

import java.util.*;

@Service
@Transactional
public class TodayInteractionService{
	
	private final TodayInteractionRepository todayInteractionRepository;
	private final String interactionId = "t";
	private final Object lock = new Object();
	
	public String getTodayInteraction(){
		TodayInteraction todayInteraction = this.findById();
		return todayInteraction.getInteractCount();
	}
	
	public void increaseInteractCount(){
		TodayInteraction todayInteraction = this.findById();
		todayInteraction.increaseInteractCount();
	}
	
	private TodayInteraction findById(){
		return this.todayInteractionRepository.findById(this.interactionId).orElseGet(()->this.saveTodayInteraction());
	}
	
	private TodayInteraction saveTodayInteraction(){
		synchronized(this.lock){
			Optional<TodayInteraction> finded = this.todayInteractionRepository.findById(this.interactionId);
			if(finded.isPresent()) return finded.get();
			this.todayInteractionRepository.saveAndFlush(new TodayInteraction());
			return this.todayInteractionRepository.findById(this.interactionId).get();
		}
	}
	
	@Autowired
	public TodayInteractionService(TodayInteractionRepository todayInteractionRepository){
		this.todayInteractionRepository = todayInteractionRepository;
	}
	
}