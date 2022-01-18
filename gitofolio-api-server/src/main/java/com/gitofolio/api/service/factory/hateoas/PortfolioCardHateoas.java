package com.gitofolio.api.service.factory.hateoas;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PortfolioCardHateoas extends Hateoas{
	
	public PortfolioCardHateoas(){
		this.initLinks();
	}
	
	@Override
	public void initLinks(){
		this.links = new ArrayList<Link>();
		this.links.add(new Link("getThis", "GET", "http://api.gitofolio.com/portfoliocards/{name}"));
		this.links.add(new Link("deleteThis", "DELETE", "http://api.gitofolio.com/portfoliocards/{name}", "id={portfolioCards.[].id}"));
		this.links.add(new Link("postThis", "POST", "http://api.gitofolio.com/portfoliocards/{name}"));
		this.links.add(new Link("putThis", "PUT", "http://api.gitofolio.com/portfoliocards"));
		
		this.links.add(new Link("getPortfoliocardImage", "GET", "http://api.gitofolio.com/portfoliocard/svg/{portfolioCards.[].id}"));
		
		this.links.add(new Link("getPortfolio", "GET", "http://api.gitofolio.com/portfolio/{id}/{portfolioCards.[].id}"));
	}
	
}