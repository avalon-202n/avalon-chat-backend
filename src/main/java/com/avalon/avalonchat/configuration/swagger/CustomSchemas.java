package com.avalon.avalonchat.configuration.swagger;

import io.swagger.v3.oas.models.media.EmailSchema;
import io.swagger.v3.oas.models.media.PasswordSchema;
import io.swagger.v3.oas.models.media.Schema;
import lombok.NoArgsConstructor;

/**
 * OpenAPI 의 Schema (Data Model) 를 저장하는 클래스
 * *
 * VO 를 활용하는 경우 자동 생성되는 Schema 외에 커스텀 Schema 가 필요한 경우가 있다.
 * 그러한 경우에 이곳에 정의해서 설정시 사용한다.
 * e.g.) Email
 */
@SuppressWarnings("unchecked")
@NoArgsConstructor
public final class CustomSchemas {

	public static final Schema<String> EMAIL_SCHEMA = new EmailSchema()
		.description("이메일 - validated by RFC 5322")
		.maxLength(64)
		.example("hello@gmail.com")
		.nullable(false);

	public static final Schema<String> PASSWORD_SCHEMA = new PasswordSchema()
		.description("비밀번호")
		.maxLength(32)
		.example("password")
		.nullable(false);

}
