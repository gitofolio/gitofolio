package com.gitofolio.api.aop.cache.setter;

import javax.servlet.http.HttpServletResponse;

public interface CacheSetter{
	
	public HttpServletResponse setCache(HttpServletResponse response);
	
}