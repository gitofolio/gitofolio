package com.gitofolio.api.service.factory;

import com.gitofolio.api.service.proxy.CrudProxy;

public interface CrudFactory<V> {
	
	CrudProxy<V> get();
	
}