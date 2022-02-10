package com.gitofolio.api.service.factory;

public interface Factory<V, T>{
	V get(T parameter);
}
