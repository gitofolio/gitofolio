package com.gitofolio.api.aop.cache.setter;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class Minutes10CacheSetter implements CacheSetter{
	
	@Override
	public HttpServletResponse setCache(HttpServletResponse response){
		response.setHeader("Cache-Control", "max-age=600");
		return response;
	}
	
}