package com.gitofolio.api.service.user.factory;

import com.gitofolio.api.service.user.factory.parameter.ParameterAble;

public interface Factory<V, T extends ParameterAble>{
	V get(T parameter);
}