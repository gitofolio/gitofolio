package com.gitofolio.api.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GitofolioController{
	
	@RequestMapping(path="/docs", method=RequestMethod.GET)
	public String docs(){
		return "docs/restdocs.html";
	}
	
}