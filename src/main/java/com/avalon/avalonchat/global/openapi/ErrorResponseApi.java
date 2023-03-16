package com.avalon.avalonchat.global.openapi;

import static com.avalon.avalonchat.global.error.ErrorResponseWithMessages.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import com.avalon.avalonchat.global.error.ErrorResponseWithMessages;

/**
 * Controller Method 에 추가하여 해당 메서드에서 발생할 수 있는 예외를 문서화 하기위해 사용
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ErrorResponseApis.class)
public @interface ErrorResponseApi {

	@AliasFor("messages")
	ErrorResponseWithMessages value() default BAD_REQUEST;

	@AliasFor("value")
	ErrorResponseWithMessages messages() default BAD_REQUEST;

	String[] args() default {};
}
