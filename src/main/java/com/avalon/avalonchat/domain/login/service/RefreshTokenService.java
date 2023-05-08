package com.avalon.avalonchat.domain.login.service;

public interface RefreshTokenService {
	void save(String refreshTokenStr, long userId);

	void deleteById(String refreshToken);
}
