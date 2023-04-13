package com.avalon.avalonchat.domain.login.service;

public interface PasswordEncoder {

	/**
	 * password 암호화
	 * @param password
	 * @return encodedPassword
	 */
	public String encode(String password);

	/**
	 * 암호화된 password 일치 여부
	 * @param password
	 * @return
	 */
	public boolean matches(String encodedPassword, String password);
}
