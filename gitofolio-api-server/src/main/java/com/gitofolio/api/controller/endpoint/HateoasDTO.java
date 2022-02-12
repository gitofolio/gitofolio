package com.gitofolio.api.controller.endpoint;

import com.gitofolio.api.service.factory.hateoas.*;
import com.gitofolio.api.service.factory.hateoas.Hateoas.Link;

import java.util.*;

public class HateoasDTO implements HateoasAble{
	
	private Map<String, Object> info;
	private List<Link> links;
	
	public Map<String, Object> getInfo(){
		return this.info;
	}
	
	public List<Link> getLinks(){
		return this.links;
	}
	
	@Override
	public void setLinks(List<Link> links){
		this.links = links;
	}
	
	public void setInfo(Map<String, Object> info){
		this.info = info;
	}
	
	public HateoasDTO(){
		this.links = null;
	}

	public HateoasDTO(Map<String, Object> info){
		this.setInfo(info);
	}

}
	
