package com.avalon.avalonchat.core.login.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.core.login.dto.EmailFindResponse;
import com.avalon.avalonchat.core.login.dto.LoginRequest;
import com.avalon.avalonchat.core.login.dto.LoginResponse;
import com.avalon.avalonchat.core.profile.domain.Profile;
import com.avalon.avalonchat.core.profile.domain.ProfileRepository;
import com.avalon.avalonchat.core.user.application.PhoneNumberAuthCodeStore;
import com.avalon.avalonchat.core.user.application.UserServiceImpl;
import com.avalon.avalonchat.core.user.application.enums.PhoneNumberKeyPurpose;
import com.avalon.avalonchat.core.user.application.keyvalue.AuthCodeValue;
import com.avalon.avalonchat.core.user.application.keyvalue.PhoneNumberKey;
import com.avalon.avalonchat.core.user.domain.User;
import com.avalon.avalonchat.core.user.domain.UserRepository;
import com.avalon.avalonchat.core.user.dto.SignUpRequest;
import com.avalon.avalonchat.global.error.exception.BadRequestException;
import com.avalon.avalonchat.testsupport.DtoFixture;
import com.avalon.avalonchat.testsupport.Fixture;

@Transactional
@SpringBootTest
public class LoginServiceImplTest {
	@Autowired
	PhoneNumberAuthCodeStore phoneNumberKeyValueStore;
	@Autowired
	private LoginServiceImpl sut;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProfileRepository profileRepository;

	@Disabled("회원가입 프로세스에 대한 정리 필요")
	@Test
	void 로그인_성공() {
		//given
		User user = Fixture.createUser();
		userRepository.save(user);

		//when
		LoginRequest loginRequest = DtoFixture.loginRequest("avalon@e.com", "passw0rd");
		LoginResponse loginResponse = sut.login(loginRequest);

		//then
		assertThat(loginResponse.getEmail().getValue()).isEqualTo(loginRequest.getEmail().getValue());
		assertThat(loginResponse.getAccessToken()).isNotNull();
	}

	@CsvSource({
		"wrong@e.com, passw0rd",
		"test@e.com, wrongPassword"
	})
	@ParameterizedTest
	void 잘못된_이메일_혹은_비밀번호를_사용한_로그인_실패(String email, String password) {
		// given
		SignUpRequest request = DtoFixture.signUpRequest("test@e.com", "passw0rd");
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
		User user = Fixture.createUser();
		userRepository.save(user);

		Profile profile = Fixture.createProfile(user);
		profileRepository.save(profile);

		String authCode = "cert-code";
		PhoneNumberKey key = PhoneNumberKey.ofPurpose(PhoneNumberKeyPurpose.EMAIL_FIND, profile.getPhoneNumber());
		AuthCodeValue authCodeValue = AuthCodeValue.fromString(authCode);

		phoneNumberKeyValueStore.put(key, authCodeValue);
		phoneNumberKeyValueStore.checkKeyValueMatches(key, authCode);

		//when
		EmailFindResponse emailFindResponse = sut.findEmailByPhoneNumber(profile.getPhoneNumber());

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
		PhoneNumberKey key = PhoneNumberKey.ofPurpose(PhoneNumberKeyPurpose.EMAIL_FIND, profile.getPhoneNumber());
		AuthCodeValue authCodeValue = AuthCodeValue.fromString(authCode);

		phoneNumberKeyValueStore.put(key, authCodeValue);

		//when & then
		assertThatExceptionOfType(RuntimeException.class)
			.isThrownBy(() -> sut.findEmailByPhoneNumber(profile.getPhoneNumber()));
	}
}
