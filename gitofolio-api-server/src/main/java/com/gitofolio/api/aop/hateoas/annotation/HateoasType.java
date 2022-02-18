package com.gitofolio.api.aop.hateoas.annotation;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.gitofolio.api.service.factory.hateoas.Hateoas;
import com.gitofolio.api.service.factory.hateoas.HateoasAble;

public enum HateoasType {
	
	USERINFOHATEOAS, 
	USERSTATHATEOAS,
	USERSTATISTICSHATEOAS,
	PORTFOLIOCARDHATEOAS,
	ENDPOINTHATEOAS,
	TODAYINTERACTIONHATEOAS;

	private Hateoas hateoas = null;
	
	public void setHateoas(HateoasAble hateoasTarget){
		this.hateoas.setHateoas(hateoasTarget);
	}
	
	@Component
	private static class HateoasTypeConsistutor{
		
		@Autowired
		public HateoasTypeConsistutor(@Qualifier("userInfoHateoas") Hateoas userInfoHateoas,
									 @Qualifier("userStatHateoas") Hateoas userStatHateoas,
									 @Qualifier("userStatisticsHateoas") Hateoas userStatisticsHateoas,
									 @Qualifier("portfolioCardHateoas") Hateoas portfolioCardHateoas,
									 @Qualifier("endPointHateoas") Hateoas endPointHateoas,
									 @Qualifier("todayInteractionHateoas") Hateoas todayInteractionHateoas){
			USERINFOHATEOAS.hateoas = userInfoHateoas;
			USERSTATHATEOAS.hateoas = userStatHateoas;
			USERSTATISTICSHATEOAS.hateoas = userStatisticsHateoas;
			PORTFOLIOCARDHATEOAS.hateoas = portfolioCardHateoas;
			ENDPOINTHATEOAS.hateoas = endPointHateoas;
			TODAYINTERACTIONHATEOAS.hateoas = todayInteractionHateoas;
		}
		
	}
	
}
