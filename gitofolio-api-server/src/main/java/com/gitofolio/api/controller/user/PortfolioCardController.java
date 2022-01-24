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
import com.gitofolio.api.aop.auth.annotation.AuthToken;
import com.gitofolio.api.aop.auth.annotation.TokenType;
import com.gitofolio.api.aop.hateoas.annotation.HateoasSetter;
import com.gitofolio.api.aop.hateoas.annotation.HateoasType;
import com.gitofolio.api.aop.log.time.annotation.ExpectedTime;
import com.gitofolio.api.domain.user.EncodedProfileImage;
import com.gitofolio.api.service.user.svg.portfoliocard.PortfolioCardSvgDTO;
import com.gitofolio.api.service.user.svg.portfoliocard.PortfolioCardSvgFactory;
import com.gitofolio.api.service.factory.parameter.PortfolioCardSvgParameter;
import com.gitofolio.api.service.factory.Factory;

@RestController
@RequestMapping
public class PortfolioCardController{
	
	private final CrudProxy<UserDTO> portfolioCardCrudProxy;
	
	private final CrudProxy<EncodedProfileImage> encodedProfileImageCrudProxy;
	
	private final Factory<PortfolioCardSvgDTO, PortfolioCardSvgParameter> portfolioCardSvgFactory;
	
	@ExpectedTime
	@HateoasSetter(hateoasType = HateoasType.PORTFOLIOCARDHATEOAS)
	@RequestMapping(path = "/portfoliocards/{name}", method = RequestMethod.GET)
	public ResponseEntity<UserDTO> getPortfolioCard(
		@PathVariable("name") String name,
		@RequestParam(value="page", required = false) String page,
		@RequestParam(value="id", required = false) Long id){
		
		UserDTO userDTO = portfolioCardCrudProxy.read(name);
		
		return new ResponseEntity(userDTO, HttpStatus.OK);
	}
	
	@ExpectedTime
	@AuthToken(tokenType = TokenType.JWT)
	@HateoasSetter(hateoasType = HateoasType.PORTFOLIOCARDHATEOAS)
	@RequestMapping(path = "/portfoliocards", method = RequestMethod.POST)
	public ResponseEntity<UserDTO> savePortfolioCard(@RequestBody UserDTO userDTO){
		
		UserDTO result = this.portfolioCardCrudProxy.create(userDTO);
		
		return new ResponseEntity(result, HttpStatus.CREATED);
	}
	
	@ExpectedTime
	@AuthToken(tokenType = TokenType.JWT)
	@RequestMapping(path = "/portfoliocards/{name}", method = RequestMethod.DELETE)
	public ResponseEntity<UserDTO> deletePortfolioCard(@PathVariable("name") String name,
													  @RequestParam(value="id", required=false) Long id){
		
		this.portfolioCardCrudProxy.delete(name, id);
		
		return new ResponseEntity(name, HttpStatus.OK);
	}
	
	@ExpectedTime
	@AuthToken(tokenType = TokenType.JWT)
	@HateoasSetter(hateoasType = HateoasType.PORTFOLIOCARDHATEOAS)
	@RequestMapping(path = "/portfoliocards", method = RequestMethod.PUT)
	public ResponseEntity<UserDTO> putPortfolioCard(@RequestBody UserDTO userDTO){
		
		UserDTO result = this.portfolioCardCrudProxy.update(userDTO);
		
		return new ResponseEntity(result, HttpStatus.OK);
	}
	
	@ExpectedTime(milliSec = 500L)
	@RequestMapping(path = "/portfoliocard/svg/{cardId}", method = RequestMethod.GET)
	public ModelAndView getPortfolioCardSvg(@PathVariable("cardId") Long cardId, 
											@RequestParam(value="color", defaultValue="white") String color){
		
		UserDTO userDTO = this.portfolioCardCrudProxy.read(cardId);
		String encodedImage = this.encodedProfileImageCrudProxy.read(userDTO).getEncodedProfileUrl();
		
		PortfolioCardSvgDTO svgDTO = this.getPortfolioCardSvgDTO(userDTO, encodedImage, color);
		
		return new ModelAndView("portfolioCard", "svgDTO", svgDTO);
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