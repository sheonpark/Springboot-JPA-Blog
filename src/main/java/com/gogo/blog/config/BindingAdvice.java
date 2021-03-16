package com.gogo.blog.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.gogo.blog.dto.ResponseDto;


// 메모리에 올리는 어노테이션  @controller, @restcontroller, @Component, @Configuration 

@Component // 메모리 뛰움.. controller가 뜬 뒤 뜸 
@Aspect	//공통기능
public class BindingAdvice {

	
	private static final Logger log = LoggerFactory.getLogger(BindingAdvice.class);

	
	// 함수 앞뒤 @Around
	@After("execution(* com.gogo.blog.controller..*Controller.*(..))")
	public void testCheck2() {
		// 전처리는 무조건 다음 스택을 진행함.
	}
	
	// 함수 앞.. @Before
	@Before("execution(* com.gogo.blog.controller..*Controller.*(..))")
	public void testCheck() {
		// 전처리는 무조건 다음 스택을 진행함.
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
	}
	// 함수 뒤.. 응답만 관리  @After
	@Around("execution(* com.gogo.blog.controller..*Controller.*(..))")
	public Object validCheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		String type = proceedingJoinPoint.getSignature().getDeclaringTypeName();
		String method = proceedingJoinPoint.getSignature().getName();
		
		System.out.println("type : " + type);
		System.out.println("method : " + method);
		
		Object[] args = proceedingJoinPoint.getArgs();
		
		for(Object arg : args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg;
				
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
						
						log.warn(type+"."+method+"() => 필드 :" + error.getField() + ", 메시지" + error.getDefaultMessage());
					}
					
					return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), errorMap);
				}
			}
		}
		return proceedingJoinPoint.proceed(); // 함수 스택 실행
	}
}
