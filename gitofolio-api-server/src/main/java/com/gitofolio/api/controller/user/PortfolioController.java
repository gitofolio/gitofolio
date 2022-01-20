package com.gitofolio.api.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.http.HttpStatus;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.proxy.CrudProxy;
import com.gitofolio.api.service.factory.CrudFactory;
import com.gitofolio.api.service.user.exception.NonExistUserException;
import com.gitofolio.api.aop.stat.annotation.UserStatGenerator; 

@Controller
public class PortfolioController{
	
	private final CrudProxy<UserDTO> portfolioCardCrudProxy;
	
	@UserStatGenerator
	@RequestMapping(path = "/portfolio/{userId}/{cardId}", method = RequestMethod.GET)
	public String redirectPortfolio(@PathVariable("userId") Long userId,
									@PathVariable("cardId") Long cardId,
								    @RequestParam(value="redirect", required=false) boolean redirect){
		
		UserDTO userDTO = this.portfolioCardCrudProxy.read(cardId);
		if(!userDTO.getId().equals(userId)) throw new NonExistUserException("유저 매칭 에러", "유저id와 포트폴리오카드id가 매칭되지 않습니다.", "/portfoliocard/"+userId+"/"+cardId);
		
		String redirectUrl = userDTO.getPortfolioCards().get(0).getPortfolioUrl();
		
		if(!redirect) return "";
		return "redirect:" + redirectUrl;
	}
	
	@Autowired
	public PortfolioController(@Qualifier("portfolioCardCrudFactory") CrudFactory<UserDTO> portfolioCardCrudFactory){
		this.portfolioCardCrudProxy = portfolioCardCrudFactory.get();
	}
	
}