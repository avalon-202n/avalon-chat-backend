package com.avalon.avalonchat.core.login.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.core.login.dto.EmailFindResponse;
import com.avalon.avalonchat.core.login.dto.LoginRequest;
import com.avalon.avalonchat.core.login.dto.LoginResponse;
import com.avalon.avalonchat.core.login.dto.PasswordFindRequest;
import com.avalon.avalonchat.core.login.dto.PasswordFindResponse;
import com.avalon.avalonchat.core.login.dto.TokenReissueRequest;
import com.avalon.avalonchat.core.login.dto.TokenReissueResponse;
import com.avalon.avalonchat.core.login.repository.RefreshTokenRepository;
import com.avalon.avalonchat.core.model.RefreshToken;
import com.avalon.avalonchat.core.profile.domain.Profile;
import com.avalon.avalonchat.core.profile.repository.ProfileRepository;
import com.avalon.avalonchat.core.user.domain.Password;
import com.avalon.avalonchat.core.user.domain.User;
import com.avalon.avalonchat.core.user.repository.UserRepository;
import com.avalon.avalonchat.global.error.exception.BadRequestException;
import com.avalon.avalonchat.infra.jwt.JwtTokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LoginServiceImpl implements LoginService {

	private final UserRepository userRepository;
	private final ProfileRepository profileRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final GetProfileIdService getProfileIdService;
	private final JwtTokenService tokenService;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenService refreshTokenService;

	@Override
	public LoginResponse login(LoginRequest request) {
		// 1. check user exists
		User findUser = userRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new BadRequestException("login-failed.email.notfound", request.getEmail()));

		// 2. verify password
		if (!passwordEncoder.matches(request.getPassword(), findUser.getPassword().getValue())) {
			throw new BadRequestException("login-failed.password.mismatch");
		}

		// 3. jwt token create
		long profileId = getProfileIdService.getProfileIdByUserId(findUser.getId());
		String accessToken = tokenService.createAccessToken(findUser, profileId);
		String refreshToken = tokenService.createRefreshToken();

		// 4.refreshToken save
		refreshTokenService.save(refreshToken, findUser.getId());
		return new LoginResponse(findUser.getEmail(), accessToken, refreshToken);
	}

	@Override
	public EmailFindResponse findEmailByPhoneNumber(String phoneNumber) {
		Profile findProfile = profileRepository.findByPhoneNumber(phoneNumber)
			.orElseThrow(() -> new BadRequestException("email-find.phoneNumber.notfound"));
		return new EmailFindResponse(findProfile.getUser().getEmail());
	}

	@Override
	public PasswordFindResponse resetPassword(PasswordFindRequest request) {
		//TODO 이메일 인증 하기
		//TODO 비밀번호 찾기 설계필요 (임시 비밀번호 발급 & 비밀 번호 재설정)
		return new PasswordFindResponse(Password.of("password"));
	}

	@Override
	public TokenReissueResponse reissueToken(TokenReissueRequest request) {
		// refresh token 만료여부
		if (tokenService.isExpired(request.getRefreshToken())) {
			throw new BadRequestException("token-reissue-failed.refresh-token.expire");
		}

		// refresh token 조회
		RefreshToken findRefreshToken = refreshTokenRepository.findById(request.getRefreshToken())
			.orElseThrow(
				() -> new BadRequestException("token-reissue-failed.refresh-token.notfound"));

		// refresh token 의 user 조회
		User findUser = userRepository.findById(findRefreshToken.getUserId())
			.orElseThrow(
				() -> new BadRequestException("token-reissue-failed.userid.notfound", findRefreshToken.getUserId()));

		//profile 조회
		long profileId = getProfileIdService.getProfileIdByUserId(findRefreshToken.getUserId());

		//access token & refresh token reissue
		String accessToken = tokenService.createAccessToken(findUser, profileId);
		String refreshToken = tokenService.createRefreshToken();

		// 기존 refresh token 삭제 & 새로운 refresh token 등록
		refreshTokenService.deleteById(findRefreshToken.getRefreshToken());
		refreshTokenService.save(refreshToken, findUser.getId());
		return new TokenReissueResponse(accessToken, refreshToken);
	}
}
