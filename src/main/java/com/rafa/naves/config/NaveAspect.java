package com.rafa.naves.config;

import java.util.logging.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;

@Aspect
@Component
public class NaveAspect {

		@Around("execution(* com.rafa.naves.controllers.NaveController.buscarNavePorId(..)) && args(id)")
	    public Object logIdNegativo(ProceedingJoinPoint joinPoint, Long id) throws Throwable {
	        if (id < 0) {
	            Logger.getLogger(NaveAspect.class.getName()).info("ID negativo: " + id);
	        }
	        return joinPoint.proceed();
	    }
	

}
