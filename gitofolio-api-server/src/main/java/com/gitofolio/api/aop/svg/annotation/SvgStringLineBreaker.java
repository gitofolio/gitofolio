package com.gitofolio.api.aop.svg.annotation;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SvgStringLineBreaker{
	
	int idx() default 0;
	int width() default 260;
	
}