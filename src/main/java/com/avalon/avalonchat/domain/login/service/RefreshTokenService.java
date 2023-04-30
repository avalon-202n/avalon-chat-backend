package com.avalon.avalonchat.domain.login.service;

import com.avalon.avalonchat.domain.model.RefreshToken;

public interface RefreshTokenService {
	void save(String refreshTokenStr, long userId);

	RefreshToken findById(String refreshToken);

	void deleteById(String refreshToken);
}
