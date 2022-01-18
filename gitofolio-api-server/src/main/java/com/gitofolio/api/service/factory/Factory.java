package com.gitofolio.api.service.factory;

import com.gitofolio.api.service.factory.parameter.ParameterAble;

public interface Factory<V, T extends ParameterAble>{
	V get(T parameter);
}