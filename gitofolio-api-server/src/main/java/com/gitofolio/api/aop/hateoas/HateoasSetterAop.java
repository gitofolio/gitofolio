package com.gitofolio.api.aop.hateoas;

import org.springframework.stereotype.Component;
import org.springframework.http.ResponseEntity;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.JoinPoint;

import com.gitofolio.api.aop.hateoas.annotation.HateoasSetter;
import com.gitofolio.api.aop.hateoas.annotation.HateoasType;
import com.gitofolio.api.service.user.factory.hateoas.HateoasAble;
import com.gitofolio.api.service.user.factory.hateoas.Hateoas;

import java.lang.reflect.Method;

@Aspect
@Component
public class HateoasSetterAop{
	
	@AfterReturning(value="@annotation(com.gitofolio.api.aop.hateoas.annotation.HateoasSetter)", returning="aopTarget")
	public Object setHateoas(JoinPoint joinPoint, Object aopTarget) throws Throwable{
		Class targetClass = joinPoint.getTarget().getClass();
		Method[] methods = targetClass.getMethods();
 		String methodName = joinPoint.getSignature().getName();
		
		HateoasSetter hateoasSetter = null;
		for(Method method : methods){
			if(method.getName().equals(methodName)) hateoasSetter = method.getAnnotation(HateoasSetter.class);
		}
		
		Hateoas hateoas = hateoasSetter.hateoasType().getHateoasInstance();
		try{
			if(aopTarget.getClass().equals(ResponseEntity.class)){
				hateoas.setHateoas((HateoasAble)((ResponseEntity)aopTarget).getBody());
			}
			else hateoas.setHateoas((HateoasAble)aopTarget);
		}catch(ClassCastException CCE){
			throw new ClassCastException(aopTarget.getClass().toString()+" HATEOAS를 적용할수 없는 리턴타입임.");
		}
		
		return aopTarget;
	}
	
}