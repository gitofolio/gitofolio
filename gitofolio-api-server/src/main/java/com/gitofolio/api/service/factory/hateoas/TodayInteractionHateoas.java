package com.gitofolio.api.service.factory.hateoas;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class TodayInteractionHateoas extends Hateoas{

	public TodayInteractionHateoas(){
		this.initLinks();
	}

	@Override
	public void initLinks(){
		this.links = new ArrayList<Link>();
		links.add(new Link("getThis", "GET", "https://api.gitofolio.com/todayinteraction"));
	}

}
