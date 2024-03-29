package com.avalon.avalonchat.core.login.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.core.login.dto.EmailFindResponse;
import com.avalon.avalonchat.core.login.dto.LoginRequest;
import com.avalon.avalonchat.core.login.dto.LoginResponse;
import com.avalon.avalonchat.core.profile.domain.PhoneNumber;
import com.avalon.avalonchat.core.profile.domain.Profile;
import com.avalon.avalonchat.core.profile.domain.ProfileRepository;
import com.avalon.avalonchat.core.user.application.PhoneNumberAuthCodeStore;
import com.avalon.avalonchat.core.user.application.UserServiceImpl;
import com.avalon.avalonchat.core.user.application.enums.PhoneNumberKeyPurpose;
import com.avalon.avalonchat.core.user.application.keyvalue.AuthCodeValue;
import com.avalon.avalonchat.core.user.application.keyvalue.PhoneNumberKey;
import com.avalon.avalonchat.core.user.domain.User;
import com.avalon.avalonchat.core.user.domain.UserRepository;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationCheckRequest;
import com.avalon.avalonchat.core.user.dto.SignUpRequest;
import com.avalon.avalonchat.global.error.exception.BadRequestException;
import com.avalon.avalonchat.testsupport.DtoFixture;
import com.avalon.avalonchat.testsupport.Fixture;

@Transactional
@SpringBootTest
public class LoginServiceImplTest {
	@Autowired
	private PhoneNumberAuthCodeStore phoneNumberAuthKeyValueStore;
	@Autowired
	private LoginServiceImpl sut;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProfileRepository profileRepository;

	@Test
	void 로그인_성공() {
		//given
		User user = Fixture.createUser("avalon@e.com", "passw0rd");
		Profile profile = Fixture.createProfile(user, "bio", LocalDate.of(1997, 8, 21), "haha", "010-1234-5678");
		userRepository.save(user);
		profileRepository.save(profile);

		//when
		LoginRequest loginRequest = DtoFixture.loginRequest("avalon@e.com", "passw0rd");
		LoginResponse loginResponse = sut.login(loginRequest);

		//then
		assertThat(loginResponse.getEmail().getValue()).isEqualTo(loginRequest.getEmail().getValue());
		assertThat(loginResponse.getAccessToken()).isNotNull();
		assertThat(loginResponse.getUserStatus()).isNotNull();
	}

	@Test
	void 프로필_생성로그인_성공_() {

	}

	@CsvSource({
		"wrong@e.com, passw0rd",
		"test@e.com, wrongPassword"
	})
	@ParameterizedTest
	void 잘못된_이메일_혹은_비밀번호를_사용한_로그인_실패(String email, String password) {
		// given
		String certificationCode = RandomStringUtils.randomNumeric(6);
		String toPhoneNumber = "010-5511-0625";
		PhoneNumber phoneNumber = PhoneNumber.of(toPhoneNumber);

		phoneNumberAuthKeyValueStore.save(
			PhoneNumberKey.fromString(phoneNumber.getValue()),
			AuthCodeValue.ofUnauthenticated(certificationCode)
		);
		userServiceImpl.checkPhoneNumberAuthentication(
			new PhoneNumberAuthenticationCheckRequest(phoneNumber, certificationCode)
		);

		SignUpRequest request = DtoFixture.signUpRequest("test@e.com", "passw0rd", toPhoneNumber);
		userServiceImpl.signUp(request);

		LoginRequest loginRequest = DtoFixture.loginRequest(email, password);

		// when then
		assertThatExceptionOfType(BadRequestException.class)
			.isThrownBy(() -> sut.login(loginRequest));
	}

	@Test
	void 이메일로_비밀번호_찾기_성공() {
	}

	@Test
	void 전화번호로_이메일_찾기_성공() {
		//given
		String phoneNumber = "010-1234-5678";

		User user = Fixture.createUser();
		userRepository.save(user);

		Profile profile = Fixture.createProfile(user, "bio", LocalDate.of(1997, 8, 21), "haha", phoneNumber);
		profileRepository.save(profile);

		String authCode = "cert-code";
		PhoneNumberKey key = PhoneNumberKey.ofPurpose(PhoneNumberKeyPurpose.EMAIL_FIND,
			profile.getPhoneNumber().getValue());
		AuthCodeValue authCodeValue = AuthCodeValue.fromString(authCode);

		phoneNumberAuthKeyValueStore.save(key, authCodeValue);
		phoneNumberAuthKeyValueStore.checkKeyValueMatches(key, authCodeValue);

		//when
		EmailFindResponse emailFindResponse = sut.findEmailByPhoneNumber(phoneNumber);

		//then
		assertThat(emailFindResponse.getEmail().getValue()).isEqualTo("avalon@e.com");
	}

	@Test
	void 전화번호로_이메일_찾기_실패() {
		//given
		User user = Fixture.createUser();
		userRepository.save(user);

		Profile profile = Fixture.createProfile(user);
		profileRepository.save(profile);

		String authCode = "cert-code";
		PhoneNumberKey key = PhoneNumberKey.ofPurpose(PhoneNumberKeyPurpose.EMAIL_FIND,
			profile.getPhoneNumber().getValue());
		AuthCodeValue authCodeValue = AuthCodeValue.fromString(authCode);

		phoneNumberAuthKeyValueStore.save(key, authCodeValue);

		//when & then
		assertThatExceptionOfType(RuntimeException.class)
			.isThrownBy(() -> sut.findEmailByPhoneNumber(profile.getPhoneNumber().getValue()));
	}
}
