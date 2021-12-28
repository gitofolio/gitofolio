package com.gitofolio.api.controller.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.gitofolio.api.service.user.factory.hateoas.Hateoas;
import com.gitofolio.api.service.user.factory.hateoas.Hateoas.Link;

import java.util.List;

@Controller
public class EndPointController{
	
	@Autowired
	@Qualifier("endPointHateoas")
	private Hateoas hateoas;
	
	@RequestMapping(path="/restdocs", method=RequestMethod.GET)
	public String docs(){
		return "restdocs";
	}
	
	@ResponseBody
	@RequestMapping(path="", method=RequestMethod.GET)
	public EndPointHateoas endPoint(){
		return EndPointHateoas.getInstance(hateoas);
	}
	
	private static class EndPointHateoas{
		
		private List<Link> links;
		private static EndPointHateoas endPointHateoas;
		private static Object sync = new Object();
		
		private EndPointHateoas(){}
		
		private EndPointHateoas(Hateoas hateoas){
			this.links = hateoas.getLinks();
		}
		
		public static EndPointHateoas getInstance(Hateoas hateoas){
			if(endPointHateoas == null){
				synchronized(sync){
					if(endPointHateoas != null) return endPointHateoas;
					endPointHateoas = new EndPointHateoas(hateoas);
				}
			}
			return endPointHateoas;
		}
		
		public List<Link> getLinks(){
			return this.links;
		}
		
	}
	
}