package com.avalon.avalonchat.domain.login.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.login.dto.EmailFindResponse;
import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;
import com.avalon.avalonchat.domain.login.dto.PasswordFindRequest;
import com.avalon.avalonchat.domain.login.dto.PasswordFindResponse;
import com.avalon.avalonchat.domain.login.dto.TokenReissueRequest;
import com.avalon.avalonchat.domain.login.dto.TokenReissueResponse;
import com.avalon.avalonchat.domain.model.RefreshToken;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.domain.user.domain.Password;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;
import com.avalon.avalonchat.global.configuration.jwt.JwtTokenService;
import com.avalon.avalonchat.global.error.exception.BadRequestException;
import com.avalon.avalonchat.global.error.exception.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

	private final UserRepository userRepository;
	private final ProfileRepository profileRepository;

	private final GetProfileIdService getProfileIdService;
	private final JwtTokenService tokenService;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenService refreshTokenService;

	@Override
	public LoginResponse login(LoginRequest request) {
		// 1. check user exists
		User findUser = userRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new NotFoundException("login-failed.email", request.getEmail()));

		// 2. verify password
		if (!passwordEncoder.matches(request.getPassword(), findUser.getPassword().getValue())) {
			throw new BadRequestException("login-failed.password.mismatch");
		}

		// 3. jwt token create
		//long profileId = getProfileIdService.getProfileIdByUserId(findUser.getId());
		String accessToken = tokenService.createAccessToken(findUser, 4);
		String refreshToken = tokenService.createRefreshToken();

		// 4.refreshToken save
		refreshTokenService.save(new RefreshToken(refreshToken, findUser.getId()));
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
		tokenService.parseClaim(request.getRefreshToken());

		// refresh token 조회
		RefreshToken savedRefreshToken = refreshTokenService.findById(request.getRefreshToken());

		// refresh token 의 user 조회
		User findUser = userRepository.findById(savedRefreshToken.getUserId())
			.orElseThrow(
				() -> new NotFoundException("token-reissue-failed.userid.notfound", savedRefreshToken.getUserId()));

		//profile 조회
		long profileId = getProfileIdService.getProfileIdByUserId(savedRefreshToken.getUserId());

		//access token & refresh token reissue
		String accessToken = tokenService.createAccessToken(findUser, profileId);
		String refreshToken = tokenService.createRefreshToken();

		// 기존 refresh token 삭제 & 새로운 refresh token 등록
		refreshTokenService.remove(savedRefreshToken);
		refreshTokenService.save(new RefreshToken(refreshToken, findUser.getId()));
		return new TokenReissueResponse(accessToken, refreshToken);
	}
}
