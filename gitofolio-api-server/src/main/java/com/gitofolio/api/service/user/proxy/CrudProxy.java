package com.gitofolio.api.service.user.proxy;

public interface CrudProxy<V extends Object>{
	
	V create(Object ...args);
	
	V read(Object ...args);
	
	V update(Object ...args);
	
	void delete(Object ...args);
	
	void addProxy(CrudProxy<V> crudProxy);
	
}