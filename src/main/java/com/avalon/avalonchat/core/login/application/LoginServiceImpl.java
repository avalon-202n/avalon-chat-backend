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
import com.avalon.avalonchat.core.profile.domain.PhoneNumber;
import com.avalon.avalonchat.core.profile.domain.Profile;
import com.avalon.avalonchat.core.profile.domain.ProfileRepository;
import com.avalon.avalonchat.core.user.application.PhoneNumberAuthCodeStore;
import com.avalon.avalonchat.core.user.application.SmsMessageService;
import com.avalon.avalonchat.core.user.application.enums.PhoneNumberKeyPurpose;
import com.avalon.avalonchat.core.user.application.keyvalue.AuthCodeValue;
import com.avalon.avalonchat.core.user.application.keyvalue.PhoneNumberKey;
import com.avalon.avalonchat.core.user.domain.Password;
import com.avalon.avalonchat.core.user.domain.User;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationCheckRequest;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationCheckResponse;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationSendRequest;
import com.avalon.avalonchat.global.error.exception.BadRequestException;
import com.avalon.avalonchat.global.model.RefreshToken;
import com.avalon.avalonchat.infra.jwt.JwtTokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LoginServiceImpl implements LoginService {

	private final ProfileRepository profileRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtTokenService tokenService;
	private final RefreshTokenService refreshTokenService;
	private final PhoneNumberAuthCodeStore phoneNumberAuthCodeStore;
	private final SmsMessageService smsMessageService;

	@Override
	public LoginResponse login(LoginRequest request) {
		// 1. check user exists
		Profile profile = profileRepository.findByEmailWithUser(request.getEmail())
			.orElseThrow(() -> new BadRequestException("login-failed.email.notfound", request.getEmail()));
		User findUser = profile.getUser();

		// 2. verify password
		if (!request.getPassword().matches(findUser.getPassword())) {
			throw new BadRequestException("login-failed.password.mismatch");
		}

		// 3. jwt token create
		String accessToken = tokenService.createAccessToken(profile);
		String refreshToken = tokenService.createRefreshToken();

		// 4.refreshToken save
		refreshTokenService.save(refreshToken, findUser.getId());
		return new LoginResponse(
			findUser.getEmail(),
			accessToken,
			refreshToken,
			findUser.getStatus()
		);
	}

	@Override
	public EmailFindResponse findEmailByPhoneNumber(String phoneNumberStr) {
		PhoneNumber phoneNumber = PhoneNumber.of(phoneNumberStr);
		// 1. check authenticate phoneNumber
		PhoneNumberKey phoneNumberKey = PhoneNumberKey.ofPurpose(PhoneNumberKeyPurpose.EMAIL_FIND,
			phoneNumber.getValue());
		boolean authenticated = phoneNumberAuthCodeStore.isAuthenticated(phoneNumberKey);
		if (!authenticated) {
			throw new BadRequestException("phonenumber.no-auth", phoneNumber.toString());
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
		Profile profile = profileRepository.findByUserIdWithUser(findRefreshToken.getUserId())
			.orElseThrow(
				() -> new BadRequestException("token-reissue-failed.userid.notfound", findRefreshToken.getUserId()));
		User findUser = profile.getUser();

		//access token & refresh token reissue
		String accessToken = tokenService.createAccessToken(profile);
		String refreshToken = tokenService.createRefreshToken();

		// 기존 refresh token 삭제 & 새로운 refresh token 등록
		refreshTokenService.deleteById(findRefreshToken.getRefreshToken());
		refreshTokenService.save(refreshToken, findUser.getId());
		return new TokenReissueResponse(accessToken, refreshToken);
	}

	@Override
	public void sendFindEmailPhoneNumberAuthentication(PhoneNumberAuthenticationSendRequest request) {
		// 1. get phone number and certification code
		PhoneNumber phoneNumber = PhoneNumber.of(request.getPhoneNumber().getValue());
		String certificationCode = RandomStringUtils.randomNumeric(6);

		// 2. send certification code
		smsMessageService.sendAuthenticationCode(phoneNumber.getValue(), certificationCode);

		// 3. put it to key-value store
		phoneNumberAuthCodeStore.save(
			PhoneNumberKey.ofPurpose(PhoneNumberKeyPurpose.EMAIL_FIND, phoneNumber.getValue()),
			AuthCodeValue.ofUnauthenticated(certificationCode)
		);
	}

	@Override
	public PhoneNumberAuthenticationCheckResponse checkFindEmailPhoneNumberAuthentication(
		PhoneNumberAuthenticationCheckRequest request) {
		// 1. get phone number
		PhoneNumber phoneNumber = PhoneNumber.of(request.getPhoneNumber().getValue());

		// 2. check authenticated
		boolean authenticated = phoneNumberAuthCodeStore.checkKeyValueMatches(
			PhoneNumberKey.ofPurpose(PhoneNumberKeyPurpose.EMAIL_FIND, phoneNumber.getValue()),
			AuthCodeValue.ofUnauthenticated(request.getCertificationCode())
		);

		// 3. return
		return new PhoneNumberAuthenticationCheckResponse(authenticated);
	}
}
