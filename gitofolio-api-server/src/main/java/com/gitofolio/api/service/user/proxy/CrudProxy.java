package com.gitofolio.api.service.user.proxy;

public interface CrudProxy<V extends Object>{
	
	V create(Object ...args) throws NullPointerException;
	
	V read(Object ...args) throws NullPointerException;
	
	V update(Object ...args) throws NullPointerException;
	
	void delete(Object ...args) throws NullPointerException;
	
	void addProxy(CrudProxy<V> crudProxy);
	
}