package com.avalon.avalonchat.core.user.application.keyvalue;

/**
 * 이메일 찾기 위한 핸드폰 전화번호 인증 할떄 사용하는 키 객체
 */
public class EmailFindKey extends PhoneNumberKey {
	private static final String SUFFIX = "::EMAILFIND";

	private EmailFindKey(String key) {
		super(key);
	}

	public static EmailFindKey fromString(String key) {
		return new EmailFindKey(key + SUFFIX);
	}
}
