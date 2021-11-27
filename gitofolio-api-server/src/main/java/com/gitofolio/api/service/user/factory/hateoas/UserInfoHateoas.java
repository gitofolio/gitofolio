package com.gitofolio.api.service.user.factory.hateoas;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserInfoHateoas extends Hateoas{
	
	public UserInfoHateoas(){
		this.setHateoas();
	}
	
	@Override
	public void setHateoas(){
		this.links = new ArrayList<Link>();
		this.links.add(new Link("getThis", "GET", "http://api.gitofolio.com/user/{name}"));
		this.links.add(new Link("deleteThis", "DELETE", "http://api.gitofolio.com/user/{name}"));
		this.links.add(new Link("postThis", "POST", "http://api.gitofolio.com/user/{name}"));
		this.links.add(new Link("getStat", "GET", "http://api.gitofolio.com/user/stat/{name}"));
		this.links.add(new Link("getDailyStat", "GET", "http://api.gitofolio.com/user/dailystat/{name}"));
	}
	
}