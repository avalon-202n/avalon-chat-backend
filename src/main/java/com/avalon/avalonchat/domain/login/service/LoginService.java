package com.avalon.avalonchat.domain.login.service;

import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;

public interface LoginService {

	LoginResponse login(LoginRequest request);

	/**
	 * jwt access 토큰 생성
	 *
	 * @param email
	 * @return
	 */
	String createAccessTokenByEmail(String email);
}
