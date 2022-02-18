package com.gitofolio.api.aop.cache.annotation;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.*;

import com.gitofolio.api.aop.cache.setter.CacheSetter;

import javax.servlet.http.HttpServletResponse;

public enum CacheType{
	
	CLEAR,
	MINUTES_10;
	
	private CacheSetter cacheSetter;
	
	public HttpServletResponse setCache(HttpServletResponse response){
		return this.cacheSetter.setCache(response);
	}
	
	@Component
	private static class CacheTypeConsistutor{
		
		@Autowired
		public CacheTypeConsistutor(@Qualifier("clearCacheSetter") CacheSetter clearCacheSetter,
								   @Qualifier("minutes10CacheSetter") CacheSetter minutes10CacheSetter){
			CLEAR.cacheSetter = clearCacheSetter;
			MINUTES_10.cacheSetter = minutes10CacheSetter;
		}
		
	}
	
}