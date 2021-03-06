package com.gitofolio.api.service.factory.hateoas;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserStatisticsHateoas extends Hateoas{
	
	public UserStatisticsHateoas(){
		this.initLinks();
	}
	
	@Override
	public void initLinks(){
		this.links = new ArrayList<Link>();
		this.links.add(new Link("getThis", "GET", "https://api.gitofolio.com/user/dailystat/{name}"));
	}
	
}
