package com.gitofolio.api.service.factory.hateoas;

import com.gitofolio.api.service.factory.hateoas.Hateoas.Link;

import java.util.List;

public interface HateoasAble{
	
	public void setLinks(List<Link> links);
	
}