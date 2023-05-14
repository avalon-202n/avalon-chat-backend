package com.avalon.avalonchat.domain.login.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import com.avalon.avalonchat.domain.user.keyvalue.AuthCodeValue;
import com.avalon.avalonchat.domain.user.keyvalue.PhoneNumberKey;
import com.avalon.avalonchat.infra.redis.RedisPhoneNumberKeyAuthCodeValueStore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.login.dto.EmailFindResponse;
import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.dto.SignUpRequest;
import com.avalon.avalonchat.domain.user.repository.UserRepository;
import com.avalon.avalonchat.domain.user.service.UserServiceImpl;
import com.avalon.avalonchat.global.error.exception.BadRequestException;
import com.avalon.avalonchat.testsupport.DtoFixture;
import com.avalon.avalonchat.testsupport.Fixture;

@Transactional
@SpringBootTest
public class LoginServiceImplTest {
	@Autowired
	private LoginServiceImpl sut;

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private RedisPhoneNumberKeyAuthCodeValueStore redisPhoneNumberKeyAuthCodeValueStore;

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

		PhoneNumberKey key = PhoneNumberKey.fromString(profile.getPhoneNumber());
		String authCode = "cert-code";

		redisPhoneNumberKeyAuthCodeValueStore.put(key, authCode);
		redisPhoneNumberKeyAuthCodeValueStore.checkKeyValueMatches(key, authCode);

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

		PhoneNumberKey key = PhoneNumberKey.fromString(profile.getPhoneNumber());
		String authCode = "cert-code";

		redisPhoneNumberKeyAuthCodeValueStore.put(key, authCode);
		redisPhoneNumberKeyAuthCodeValueStore.checkKeyValueMatches(key, "wrong-cert-code");

		//then
		assertThatExceptionOfType(RuntimeException.class)
			.isThrownBy(() -> sut.findEmailByPhoneNumber(profile.getPhoneNumber()));
	}
}
