package com.avalon.avalonchat.core.login.application;

public interface RefreshTokenService {
	void save(String refreshTokenStr, long userId);

	void deleteById(String refreshToken);
}
