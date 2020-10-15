/**
 * 
 */
package com.postang.aspect;

import java.io.Serializable;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Aspect
@Component
@Log4j2
public class LoggingAspect implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3824608490425886974L;

	@Before("execution(* com.postang.controller.*.*(..)) || execution(* com.postang.dao.service.impl.*.*(..)) || execution(* com.postang.util.*.*(..)) || execution(* com.postang.service.impl.*.*(..))")
	public void methodStartAdvice(JoinPoint joinPoint) throws JsonProcessingException {
		if (joinPoint.getArgs() == null) {
			log.info(joinPoint.getSignature() + " starts:");
		} else {
			log.info(joinPoint.getSignature() + " starts with :"
					+ new ObjectMapper().writeValueAsString(joinPoint.getArgs()));
		}
	}

	@AfterThrowing(pointcut = "execution(* com.postang.controller.*.*(..)) || execution(* com.postang.dao.service.impl.*.*(..)) || execution(* com.postang.util.*.*(..)) || execution(* com.postang.service.impl.*.*(..))", throwing = "ex")
	public void methodExceptionAdvice(JoinPoint joinPoint, RuntimeException ex) {
		log.error(joinPoint.getSignature() + " throws: " + ex.getMessage());
		log.error(ex.getStackTrace());
	}

}
