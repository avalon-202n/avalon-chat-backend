package com.avalon.avalonchat.domain.login.service;

import org.springframework.stereotype.Service;

import com.avalon.avalonchat.domain.login.repository.RefreshTokenRepository;
import com.avalon.avalonchat.domain.model.RefreshToken;

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
