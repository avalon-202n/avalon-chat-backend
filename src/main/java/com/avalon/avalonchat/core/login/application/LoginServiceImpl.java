package com.avalon.avalonchat.core.login.application;

import org.apache.commons.lang3.RandomStringUtils;
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
import com.avalon.avalonchat.core.profile.domain.Profile;
import com.avalon.avalonchat.core.profile.domain.ProfileRepository;
import com.avalon.avalonchat.core.user.application.PhoneNumberAuthCodeStore;
import com.avalon.avalonchat.core.user.application.SmsMessageService;
import com.avalon.avalonchat.core.user.application.keyvalue.AuthCodeValue;
import com.avalon.avalonchat.core.user.application.keyvalue.PhoneNumberKey;
import com.avalon.avalonchat.core.user.domain.Password;
import com.avalon.avalonchat.core.user.domain.User;
import com.avalon.avalonchat.core.user.domain.UserRepository;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationCheckRequest;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationCheckResponse;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationSendRequest;
import com.avalon.avalonchat.global.error.exception.BadRequestException;
import com.avalon.avalonchat.global.error.exception.NotFoundException;
import com.avalon.avalonchat.global.model.RefreshToken;
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
	private final JwtTokenService tokenService;
	private final RefreshTokenService refreshTokenService;
	private final PhoneNumberAuthCodeStore phoneNumberAuthCodeStore;
	private final SmsMessageService smsMessageService;

	@Override
	public LoginResponse login(LoginRequest request) {
		// 1. check user exists
		User findUser = userRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new BadRequestException("login-failed.email.notfound", request.getEmail()));

		// 2. verify password
		if (!request.getPassword().matches(findUser.getPassword())) {
			throw new BadRequestException("login-failed.password.mismatch");
		}

		// 3. jwt token create
		long profileId = profileRepository.findProfileIdByUserId(findUser.getId())
			.orElseThrow(() -> new NotFoundException("profile", " userId = " + findUser.getId()));
		String accessToken = tokenService.createAccessToken(findUser, profileId);
		String refreshToken = tokenService.createRefreshToken();

		// 4.refreshToken save
		refreshTokenService.save(refreshToken, findUser.getId());
		return new LoginResponse(findUser.getEmail(), accessToken, refreshToken);
	}

	@Override
	public EmailFindResponse findEmailByPhoneNumber(String phoneNumber) {
		// 1. check authenticate phoneNumber
		PhoneNumberKey phoneNumberKey = PhoneNumberKey.ofPurpose(PhoneNumberKey.Purpose.EMAIL_FIND, phoneNumber);
		if (!phoneNumberAuthCodeStore.isAuthenticated(phoneNumberKey)) {
			throw new BadRequestException("phonenumber.no-auth", phoneNumber);
		}

		// 2. find email
		Profile findProfile = profileRepository.findByPhoneNumber(phoneNumber)
			.orElseThrow(() -> new BadRequestException("email-find.phonenumber.notfound", phoneNumber));
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
			.orElseThrow(() -> new BadRequestException("token-reissue-failed.refresh-token.notfound"));

		// refresh token 의 user 조회
		User findUser = userRepository.findById(findRefreshToken.getUserId())
			.orElseThrow(
				() -> new BadRequestException("token-reissue-failed.userid.notfound", findRefreshToken.getUserId()));

		//profile 조회
		long profileId = profileRepository.findProfileIdByUserId(findUser.getId())
			.orElseThrow(() -> new NotFoundException("profile", " userId = " + findUser.getId()));

		//access token & refresh token reissue
		String accessToken = tokenService.createAccessToken(findUser, profileId);
		String refreshToken = tokenService.createRefreshToken();

		// 기존 refresh token 삭제 & 새로운 refresh token 등록
		refreshTokenService.deleteById(findRefreshToken.getRefreshToken());
		refreshTokenService.save(refreshToken, findUser.getId());
		return new TokenReissueResponse(accessToken, refreshToken);
	}

	@Override
	public void sendFindEmailPhoneNumberAuthentication(PhoneNumberAuthenticationSendRequest request) {
		// 1. get phone number and certification code
		String phoneNumber = request.getPhoneNumber().replaceAll("-", "").trim();
		String certificationCode = RandomStringUtils.randomNumeric(6);

		// 2. send certification code
		smsMessageService.sendAuthenticationCode(phoneNumber, certificationCode);

		// 3. put it to key-value store
		phoneNumberAuthCodeStore.put(
			PhoneNumberKey.ofPurpose(PhoneNumberKey.Purpose.EMAIL_FIND, phoneNumber),
			AuthCodeValue.ofUnauthenticated(certificationCode)
		);
	}

	@Override
	public PhoneNumberAuthenticationCheckResponse checkFindEmailPhoneNumberAuthentication(
		PhoneNumberAuthenticationCheckRequest request) {
		// 1. get phone number
		String phoneNumber = request.getPhoneNumber().replaceAll("-", "").trim();

		// 2. check authenticated
		boolean authenticated = phoneNumberAuthCodeStore.checkKeyValueMatches(
			PhoneNumberKey.ofPurpose(PhoneNumberKey.Purpose.EMAIL_FIND, phoneNumber),
			request.getCertificationCode()
		);

		// 3. return
		return new PhoneNumberAuthenticationCheckResponse(authenticated);
	}
}
