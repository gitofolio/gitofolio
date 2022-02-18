package com.gitofolio.api.aop.cache.setter;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class Minutes10CacheSetter implements CacheSetter{
	
	@Override
	public HttpServletResponse setCache(HttpServletResponse response){
		response.setHeader("Cache-Control", "no-store, max-age=600");
		return response;
	}
	
}