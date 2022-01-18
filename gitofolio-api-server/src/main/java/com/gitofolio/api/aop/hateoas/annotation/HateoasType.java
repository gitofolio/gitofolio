package com.gitofolio.api.aop.hateoas.annotation;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.gitofolio.api.service.factory.hateoas.Hateoas;

public enum HateoasType {
	
	USERINFOHATEOAS, 
	USERSTATHATEOAS,
	USERSTATISTICSHATEOAS,
	PORTFOLIOCARDHATEOAS,
	ENDPOINTHATEOAS;
	
	private Hateoas hateoas = null;
	
	public Hateoas getHateoasInstance(){
		return this.hateoas;
	}
	
	@Component
	private static class HateoasTypeConsistutor{
		
		@Autowired
		public HateoasTypeConsistutor(@Qualifier("userInfoHateoas") Hateoas userInfoHateoas,
									 @Qualifier("userStatHateoas") Hateoas userStatHateoas,
									 @Qualifier("userStatisticsHateoas") Hateoas userStatisticsHateoas,
									 @Qualifier("portfolioCardHateoas") Hateoas portfolioCardHateoas,
									 @Qualifier("endPointHateoas") Hateoas endPointHateoas){
			USERINFOHATEOAS.hateoas = userInfoHateoas;
			USERSTATHATEOAS.hateoas = userStatHateoas;
			USERSTATISTICSHATEOAS.hateoas = userStatisticsHateoas;
			PORTFOLIOCARDHATEOAS.hateoas = portfolioCardHateoas;
			ENDPOINTHATEOAS.hateoas = endPointHateoas;
		}
		
	}
	
}