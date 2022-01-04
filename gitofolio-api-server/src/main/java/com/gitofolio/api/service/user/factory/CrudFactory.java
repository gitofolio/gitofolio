package com.gitofolio.api.service.user.factory;

import com.gitofolio.api.service.user.proxy.CrudProxy;

public interface CrudFactory<V> {
	
	CrudProxy<V> get();
	
}