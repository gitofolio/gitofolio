package com.gitofolio.api.controller.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.gitofolio.api.aop.hateoas.annotation.HateoasSetter;
import com.gitofolio.api.aop.hateoas.annotation.HateoasType;
import com.gitofolio.api.service.common.*;

import java.util.*;

@Controller
public class EndPointController{
	
	private final TodayInteraction todayInteraction;

	@RequestMapping(path = "/restdocs", method = RequestMethod.GET)
	public String docs(){
		return "restdocs";
	}

	@ResponseBody
	@HateoasSetter(hateoasType = HateoasType.TODAYINTERACTIONHATEOAS)
	@RequestMapping(path = "/todayinteraction", method = RequestMethod.GET)
	public HateoasDTO todayInteraction(){
		Map<String, Object> todayInteractionMap = new HashMap<String, Object>();
		todayInteractionMap.put("interact", this.todayInteraction.getInteractCount());
		return new HateoasDTO(todayInteractionMap);
	}

	@ResponseBody
	@HateoasSetter(hateoasType = HateoasType.ENDPOINTHATEOAS)
	@RequestMapping(path = "", method = RequestMethod.GET)
	public HateoasDTO endPoint(){
		return new HateoasDTO();
	}
	
	@Autowired
	public EndPointController(TodayInteraction todayInteraction){
		this.todayInteraction = todayInteraction;
	}
	
}
