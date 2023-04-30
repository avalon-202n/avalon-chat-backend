package com.avalon.avalonchat.domain.login.service;

import org.springframework.stereotype.Service;

import com.avalon.avalonchat.domain.login.repository.RefreshTokenRepository;
import com.avalon.avalonchat.domain.model.RefreshToken;
import com.avalon.avalonchat.global.configuration.jwt.JwtConfigProperties;
import com.avalon.avalonchat.global.error.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtConfigProperties jwtConfigProperties;

	@Override
	public void save(String refreshTokenStr, long userId) {
		RefreshToken refreshToken = new RefreshToken(
			refreshTokenStr,
			userId
		);

		refreshTokenRepository.save(refreshToken);
	}

	@Override
	public RefreshToken findById(String refreshToken) {
		return refreshTokenRepository.findById(refreshToken)
			.orElseThrow(() -> new BadRequestException("refresh-token.notfound"));
	}

	@Override
	public void deleteById(String refreshToken) {
		refreshTokenRepository.deleteById(refreshToken);
	}
}
