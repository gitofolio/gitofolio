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
		
		this.links.add(new Link("getThis", "GET", "http://api.gitofolio.com"));
		this.links.add(new Link("getUser", "GET", "http://api.giotofolio.com/user/{name}"));
		this.links.add(new Link("deleteUser", "DELETE", "http://api.gitofolio.com/user/{name}"));
		
		this.links.add(new Link("getLoginedUser", "GET", "http://api.gitofolio.com/user"));
		
		this.links.add(new Link("getPortfoliocardImage", "GET", "http://api.gitofolio.com/portfoliocard/svg/{portfolioCards.[].id}"));
		
		this.links.add(new Link("getPortfolio", "GET", "http://api.gitofolio.com/portfolio/{id}/{portfolioCards.[].id}"));
		
		this.links.add(new Link("getPortfoliocards", "GET", "http://api.gitofolio.com/portfoliocards/{name}"));
		this.links.add(new Link("deletePortfolicards", "DELETE", "http://api.gitofolio.com/portfoliocards/{name}", "id={portfolioCards.[].id}"));
		this.links.add(new Link("postPortfoliocards", "POST", "http://api.gitofolio.com/portfoliocards"));
		this.links.add(new Link("putPortfoliocards", "PUT", "http://api.gitofolio.com/portfoliocards"));
	}
	
}