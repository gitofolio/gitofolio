package com.gitofolio.api.service.user.factory.hateoas;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PortfolioCardHateoas extends Hateoas{
	
	public PortfolioCardHateoas(){
		this.setHateoas();
	}
	
	@Override
	public void setHateoas(){
		this.links = new ArrayList<Link>();
		this.links.add(new Link("getThis", "GET", "http://api.gitofolio.com/portfoliocards/{name}", "cards={n1},{n2}"));
		this.links.add(new Link("deleteThis", "DELETE", "http://api.gitofolio.com/portfoliocards/{name}", "card={n1}"));
		this.links.add(new Link("postThis", "POST", "http://api.gitofolio.com/portfoliocards/{name}"));
	}
	
}