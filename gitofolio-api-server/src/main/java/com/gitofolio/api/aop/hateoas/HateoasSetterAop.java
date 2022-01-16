package com.gitofolio.api.aop.hateoas;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.JoinPoint;

import com.gitofolio.api.aop.AnnotationExtractor;
import com.gitofolio.api.aop.hateoas.annotation.HateoasSetter;
import com.gitofolio.api.aop.hateoas.annotation.HateoasType;
import com.gitofolio.api.service.user.factory.hateoas.HateoasAble;
import com.gitofolio.api.service.user.factory.hateoas.Hateoas;

import java.lang.reflect.Method;

@Aspect
@Component
public class HateoasSetterAop{
	
	private final AnnotationExtractor<HateoasSetter> annotationExtractor;
	
	@AfterReturning(value="@annotation(com.gitofolio.api.aop.hateoas.annotation.HateoasSetter)", returning="aopTarget")
	public Object setHateoas(JoinPoint joinPoint, Object aopTarget) throws Throwable{
		
		if(!isHateoasAble(aopTarget)) throw new ClassCastException(aopTarget.getClass().toString()+" HATEOAS를 적용할수 없는 리턴타입임.");
		
		HateoasSetter hateoasSetter = this.annotationExtractor.extractAnnotation(joinPoint, HateoasSetter.class);
		Hateoas hateoas = hateoasSetter.hateoasType().getHateoasInstance();
		if(isResponseEntity(aopTarget)){
			hateoas.setHateoas((HateoasAble)((ResponseEntity)aopTarget).getBody());
		}
		else hateoas.setHateoas((HateoasAble)aopTarget);
		return aopTarget;
	}
	
	private boolean isHateoasAble(Object aopTarget){
		try{
			isHateoasAbleRealTask(aopTarget);
		}catch(ClassCastException CCE){
			return false;
		}
		return true;
	}
	
	private void isHateoasAbleRealTask(Object aopTarget) throws ClassCastException{
		if(isResponseEntity(aopTarget)){
			HateoasAble hateoasAble = (HateoasAble)(((ResponseEntity)aopTarget).getBody());
		}
		else {
			HateoasAble hateoasAble = (HateoasAble)aopTarget;
		}
	}
	
	private boolean isResponseEntity(Object aopTarget){
		if(aopTarget.getClass().equals(ResponseEntity.class)) return true;
		return false;
	}
	
	@Autowired
	public HateoasSetterAop(@Qualifier("annotationExtractor") AnnotationExtractor<HateoasSetter> annotationExtractor){
		this.annotationExtractor = annotationExtractor;
	}
	
}