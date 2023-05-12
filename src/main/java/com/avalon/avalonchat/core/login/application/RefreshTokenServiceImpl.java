package com.avalon.avalonchat.core.login.application;

import org.springframework.stereotype.Service;

import com.avalon.avalonchat.core.login.repository.RefreshTokenRepository;
import com.avalon.avalonchat.core.model.RefreshToken;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public void save(String refreshTokenStr, long userId) {
		RefreshToken refreshToken = new RefreshToken(
			refreshTokenStr,
			userId
		);

		refreshTokenRepository.save(refreshToken);
	}

	@Override
	public void deleteById(String refreshToken) {
		refreshTokenRepository.deleteById(refreshToken);
	}
}
