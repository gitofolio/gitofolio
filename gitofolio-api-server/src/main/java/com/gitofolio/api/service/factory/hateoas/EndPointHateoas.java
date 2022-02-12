package com.gitofolio.api.service.factory.hateoas;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class EndPointHateoas extends Hateoas{
	
	public EndPointHateoas(){
		this.initLinks();
	}
	
	@Override
	public void initLinks(){
		this.links = new ArrayList<Link>();
		
		this.links.add(new Link("getThis", "GET", "https://api.gitofolio.com"));
		this.links.add(new Link("getUser", "GET", "https://api.giotofolio.com/user/{name}"));
		this.links.add(new Link("deleteUser", "DELETE", "https://api.gitofolio.com/user/{name}"));
		
		this.links.add(new Link("getLoginedUser", "GET", "https://api.gitofolio.com/user"));
			
		this.links.add(new Link("getPortfoliocardImage", "GET", "https://api.gitofolio.com/portfoliocard/svg/{portfolioCards.[].id}"));
		
		this.links.add(new Link("getPortfolio", "GET", "https://api.gitofolio.com/portfolio/{id}/{portfolioCards.[].id}"));
		
		this.links.add(new Link("getPortfoliocards", "GET", "https://api.gitofolio.com/portfoliocards/{name}"));
		this.links.add(new Link("deletePortfolicards", "DELETE", "https://api.gitofolio.com/portfoliocards/{name}", "id={portfolioCards.[].id}"));
		this.links.add(new Link("postPortfoliocards", "POST", "https://api.gitofolio.com/portfoliocards"));
		this.links.add(new Link("putPortfoliocards", "PUT", "https://api.gitofolio.com/portfoliocards"));
		
		this.links.add(new Link("getTodayInteractCount", "GET", "https://api.gitofolio.com/todayinteraction"));
	}
	
}
