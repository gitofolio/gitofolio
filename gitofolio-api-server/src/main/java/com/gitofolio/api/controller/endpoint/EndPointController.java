package com.gitofolio.api.controller.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;

import com.gitofolio.api.aop.hateoas.annotation.*;
import com.gitofolio.api.service.common.*;
import com.gitofolio.api.aop.log.datacollector.annotation.RequestDataCollector;
import com.gitofolio.api.service.common.TodayInteractionService;

import java.util.*;

@Controller
public class EndPointController{
	
	private final TodayInteractionService todayInteractionService;
	
	@RequestDataCollector(path="/restdocs")
	@RequestMapping(path = "/restdocs", method = RequestMethod.GET)
	public String docs(){
		return "/restdocs.html";
	}

	@ResponseBody
	@RequestDataCollector(path="/todayinteraction")
	@HateoasSetter(hateoasType = HateoasType.TODAYINTERACTIONHATEOAS)
	@RequestMapping(path = "/todayinteraction", method = RequestMethod.GET)
	public HateoasDTO todayInteraction(){
		Map<String, Object> todayInteractionMap = new HashMap<String, Object>();
		todayInteractionMap.put("interact", this.todayInteractionService.getTodayInteraction());
		return new HateoasDTO(todayInteractionMap);
	}
	
	@ResponseBody
	@RequestDataCollector(path="")
	@HateoasSetter(hateoasType = HateoasType.ENDPOINTHATEOAS)
	@RequestMapping(path = "", method = RequestMethod.GET)
	public HateoasDTO endPoint(){
		return new HateoasDTO();
	}
	
	@Autowired
	public EndPointController(TodayInteractionService todayInteractionService){
		this.todayInteractionService = todayInteractionService;
	}
	
}
