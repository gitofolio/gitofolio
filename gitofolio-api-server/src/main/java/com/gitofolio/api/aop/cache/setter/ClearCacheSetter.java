package com.gitofolio.api.aop.cache.setter;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class ClearCacheSetter implements CacheSetter{
	
	@Override
	public HttpServletResponse setCache(HttpServletResponse response){
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		return response;
	}
	
}