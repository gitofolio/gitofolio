package com.gitofolio.api.controller.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.gitofolio.api.aop.hateoas.annotation.HateoasSetter;
import com.gitofolio.api.aop.hateoas.annotation.HateoasType;

import java.util.List;

@Controller
public class EndPointController{
	
	private EndPointHateoasDTO endPointHateoasDTO;
	
	@RequestMapping(path="/restdocs", method=RequestMethod.GET)
	public String docs(){
		return "restdocs";
	}
	
	@ResponseBody
	@HateoasSetter(hateoasType=HateoasType.ENDPOINTHATEOAS)
	@RequestMapping(path="", method=RequestMethod.GET)
	public EndPointHateoasDTO endPoint(){
		return this.endPointHateoasDTO;
	}
	
	@Autowired
	public EndPointController(EndPointHateoasDTO endPointHateoasDTO){
		this.endPointHateoasDTO = endPointHateoasDTO;
	}
	
}