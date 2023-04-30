package com.avalon.avalonchat.domain.login.service;

import com.avalon.avalonchat.domain.model.RefreshToken;

public interface RefreshTokenService {
	void save(RefreshToken refreshToken);

	RefreshToken findById(String refreshToken);

	void remove(RefreshToken refreshToken);
}
