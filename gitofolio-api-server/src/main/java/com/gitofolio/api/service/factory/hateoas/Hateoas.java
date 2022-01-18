package com.gitofolio.api.service.factory.hateoas;

import org.springframework.stereotype.Component;

import com.gitofolio.api.service.user.dtos.UserDTO;

import java.util.List;

@Component
public abstract class Hateoas{
	
	public static class Link{
		private String rel;
		private String method;
		private String href;
		private String parameter;
		
		public Link(String rel, String method, String href){
			this.rel = rel;
			this.method = method;
			this.href = href;
		}
		
		public Link(String rel, String method, String href, String parameter){
			this.rel = rel;
			this.method = method;
			this.href = href;
			this.parameter = parameter;
		}
		
		public String getRel(){
			return this.rel;
		}
		
		public String getMethod(){
			return this.method;
		}
		
		public String getHref(){
			return this.href;
		}
		
		public String getParameter(){
			return this.parameter;
		}
		
	}
	
	protected List<Link> links;
	
	public void setHateoas(HateoasAble target){
		target.setLinks(this.links);
	}
	
	public abstract void initLinks();
	
	public Hateoas getHateoas(){
		return this;
	}
	
	public List<Link> getLinks(){
		return this.links;
	}
	
}