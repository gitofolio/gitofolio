package com.gitofolio.api.controller.user;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.proxy.CrudProxy;
import com.gitofolio.api.service.factory.CrudFactory;
import com.gitofolio.api.aop.auth.annotation.*;
import com.gitofolio.api.aop.hateoas.annotation.*;
import com.gitofolio.api.aop.cache.annotation.*;
import com.gitofolio.api.aop.log.datacollector.annotation.RequestDataCollector;
import com.gitofolio.api.domain.user.EncodedProfileImage;
import com.gitofolio.api.service.user.svg.portfoliocard.*;
import com.gitofolio.api.service.factory.parameter.PortfolioCardSvgParameter;
import com.gitofolio.api.service.factory.Factory;

@RestController
@RequestMapping
public class PortfolioCardController{
	
	private final CrudProxy<UserDTO> portfolioCardCrudProxy;
	
	private final CrudProxy<EncodedProfileImage> encodedProfileImageCrudProxy;
	
	private final Factory<PortfolioCardSvgDTO, PortfolioCardSvgParameter> portfolioCardSvgFactory;
	
	@RequestDataCollector(path="/portfoliocards/{name}")
	@HateoasSetter(hateoasType = HateoasType.PORTFOLIOCARDHATEOAS)
	@RequestMapping(path = "/portfoliocards/{name}", method = RequestMethod.GET)
	public ResponseEntity<UserDTO> getPortfolioCard(@PathVariable("name") String name,
													@RequestParam(value="id", required=false) Long id){
		
		UserDTO userDTO = null;
		if(!isCardIdNull(id)) userDTO = portfolioCardCrudProxy.read(name, id);
		else userDTO = portfolioCardCrudProxy.read(name);
		
		return new ResponseEntity(userDTO, HttpStatus.OK);
	}
	
	private boolean isCardIdNull(Long id){
		return id == null ? true : false;
	}
	
	@RequestDataCollector(path="/portfoliocards")
	@AuthToken(tokenType = TokenType.AUTO)
	@HateoasSetter(hateoasType = HateoasType.PORTFOLIOCARDHATEOAS)
	@RequestMapping(path = "/portfoliocards", method = RequestMethod.POST)
	public ResponseEntity<UserDTO> savePortfolioCard(@RequestBody UserDTO userDTO){
		
		UserDTO result = this.portfolioCardCrudProxy.create(userDTO);
		
		return new ResponseEntity(result, HttpStatus.CREATED);
	}
	
	@RequestDataCollector(path="/portfoliocards/{name}")
	@AuthToken(tokenType = TokenType.AUTO)
	@RequestMapping(path = "/portfoliocards/{name}", method = RequestMethod.DELETE)
	public ResponseEntity<UserDTO> deletePortfolioCard(@PathVariable("name") String name,
													  @RequestParam(value="id", required=false) Long id){
		
		this.portfolioCardCrudProxy.delete(name, id);
		
		return new ResponseEntity(name, HttpStatus.OK);
	}
	
	@RequestDataCollector(path="/portfoliocards")
	@AuthToken(tokenType = TokenType.AUTO)
	@HateoasSetter(hateoasType = HateoasType.PORTFOLIOCARDHATEOAS)
	@RequestMapping(path = "/portfoliocards", method = RequestMethod.PUT)
	public ResponseEntity<UserDTO> putPortfolioCard(@RequestBody UserDTO userDTO){
		
		UserDTO result = this.portfolioCardCrudProxy.update(userDTO);
		
		return new ResponseEntity(result, HttpStatus.OK);
	}
	
	@RequestDataCollector(path="/portfoliocards/svg/{cardId}")
	@WebCache(cacheType = CacheType.MINUTES_10)
	@RequestMapping(path = "/portfoliocard/svg/{cardId}", method = RequestMethod.GET)
	public ModelAndView getPortfolioCardSvg(@PathVariable("cardId") Long cardId, 
											@RequestParam(value="color", defaultValue="white") String color){
		
		UserDTO userDTO = this.portfolioCardCrudProxy.read(cardId);
		String encodedImage = this.encodedProfileImageCrudProxy.read(userDTO).getEncodedProfileUrl();
		
		PortfolioCardSvgDTO svgDTO = this.getPortfolioCardSvgDTO(userDTO, encodedImage, color);
		
		return new ModelAndView("/WEB-INF/portfolioCard.jsp", "svgDTO", svgDTO);
	}
	
	private PortfolioCardSvgDTO getPortfolioCardSvgDTO(UserDTO userDTO, String encodedImage, String color){
		PortfolioCardSvgParameter portfolioCardSvgParameter = new PortfolioCardSvgParameter.Builder()
			.name(userDTO.getName())
			.encodedImage(encodedImage)
			.portfolioUrl(userDTO.getPortfolioCards().get(0).getPortfolioUrl())
			.watchedNum(userDTO.getPortfolioCards().get(0).getPortfolioCardWatched())
			.article(userDTO.getPortfolioCards().get(0).getPortfolioCardArticle())
			.colorName(color)
			.build();
			
		return this.portfolioCardSvgFactory.get(portfolioCardSvgParameter);
	}
	
	@Autowired
	public PortfolioCardController(@Qualifier("portfolioCardCrudFactory") CrudFactory<UserDTO> portfolioCardCrudFactory,
								   @Qualifier("encodedProfileImageCrudFactory") CrudFactory<EncodedProfileImage> encodedProfileImageCrudFactory,
								   @Qualifier("portfolioCardSvgFactory") Factory<PortfolioCardSvgDTO, PortfolioCardSvgParameter> portfolioCardSvgFactory){
		this.portfolioCardCrudProxy = portfolioCardCrudFactory.get();
		this.encodedProfileImageCrudProxy = encodedProfileImageCrudFactory.get();
		this.portfolioCardSvgFactory = portfolioCardSvgFactory;
	}
	
}