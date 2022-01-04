package com.gitofolio.api.controller.endpoint;

import com.gitofolio.api.service.user.factory.hateoas.Hateoas;
import com.gitofolio.api.service.user.factory.hateoas.HateoasAble;
import com.gitofolio.api.service.user.factory.hateoas.Hateoas.Link;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EndPointHateoasDTO implements HateoasAble{
		
	private List<Link> links;
	
	public List<Link> getLinks(){
		return this.links;
	}
	
	@Override
	public void setLinks(List<Link> links){
		this.links = links;
	}
	
}
	