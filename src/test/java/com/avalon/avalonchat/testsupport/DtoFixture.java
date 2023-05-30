package com.avalon.avalonchat.testsupport;

import java.time.LocalDate;
import java.util.List;

import com.avalon.avalonchat.core.friend.dto.FriendAddRequest;
import com.avalon.avalonchat.core.friend.dto.FriendPhoneNumberAddRequest;
import com.avalon.avalonchat.core.login.dto.LoginRequest;
import com.avalon.avalonchat.core.profile.dto.BackgroundImageDeleteRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileImageDeleteRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileUpdateRequest;
import com.avalon.avalonchat.core.user.domain.Email;
import com.avalon.avalonchat.core.user.domain.Password;
import com.avalon.avalonchat.core.user.dto.EmailAuthenticationCheckRequest;
import com.avalon.avalonchat.core.user.dto.EmailAuthenticationCheckResponse;
import com.avalon.avalonchat.core.user.dto.EmailAuthenticationSendRequest;
import com.avalon.avalonchat.core.user.dto.EmailDuplicatedCheckRequest;
import com.avalon.avalonchat.core.user.dto.EmailDuplicatedCheckResponse;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationCheckRequest;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationCheckResponse;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationSendRequest;
import com.avalon.avalonchat.core.user.dto.SignUpRequest;
import com.avalon.avalonchat.core.user.dto.SignUpResponse;

public final class DtoFixture {

	/* User Package Dto Begin */
	public static SignUpRequest signUpRequest(String email, String password) {
		return new SignUpRequest(
			Email.of(email),
			Password.of(password)
		);
	}

	public static SignUpResponse expectedResponseOf(SignUpRequest request) {
		return new SignUpResponse(request.getEmail());
	}

	public static EmailDuplicatedCheckRequest emailDuplicatedCheckRequest(String email) {
		return new EmailDuplicatedCheckRequest(Email.of(email));
	}

	public static EmailDuplicatedCheckResponse emailDuplicatedCheckResponse(boolean duplicated) {
		return new EmailDuplicatedCheckResponse(duplicated);
	}

	public static EmailAuthenticationSendRequest emailAuthenticationSendRequest(String email) {
		return new EmailAuthenticationSendRequest(Email.of(email));
	}

	public static EmailAuthenticationCheckRequest emailAuthenticationCheckRequest(
		String email,
		String certificationCode
	) {
		return new EmailAuthenticationCheckRequest(Email.of(email), certificationCode);
	}

	public static EmailAuthenticationCheckResponse emailAuthenticationCheckResponse(boolean authenticated) {
		return new EmailAuthenticationCheckResponse(authenticated);
	}

	public static PhoneNumberAuthenticationSendRequest phoneNumberAuthenticationSendRequest(String phoneNumber) {
		return new PhoneNumberAuthenticationSendRequest(phoneNumber);
	}

	public static PhoneNumberAuthenticationCheckRequest phoneNumberAuthenticationCheckRequest(
		String phoneNumber,
		String certificationCode
	) {
		return new PhoneNumberAuthenticationCheckRequest(phoneNumber, certificationCode);
	}

	public static PhoneNumberAuthenticationCheckResponse phoneNumberAuthenticationCheckResponse(boolean authenticated) {
		return new PhoneNumberAuthenticationCheckResponse(authenticated);
	}
	/* User Package DTO End */

	public static LoginRequest loginRequest(String email, String password) {
		return new LoginRequest(
			Email.of(email),
			Password.of(password)
		);
	}
	/* Login Package DTO End */

	public static FriendAddRequest friendAddRequest(List<String> phoneNumbers) {
		return new FriendAddRequest(phoneNumbers);
	}

	/* Profile Package DTO Start */
	public static ProfileUpdateRequest profileUpdateRequest(
		LocalDate birthDate,
		String nickName,
		String bio,
		String profileImageUrl,
		String backgroundImageUrl,
		String phoneNumber
	) {
		return new ProfileUpdateRequest(
			birthDate, nickName, bio, profileImageUrl, backgroundImageUrl, phoneNumber
		);
	}

	public static ProfileImageDeleteRequest profileImageDeleteRequest(
		List<String> deletedProfileImageUrls
	) {
		return new ProfileImageDeleteRequest(deletedProfileImageUrls);
	}

	public static BackgroundImageDeleteRequest backgroundImageDeleteRequest(
		List<String> deletedBackgroundImageUrls
	) {
		return new BackgroundImageDeleteRequest(deletedBackgroundImageUrls);
	}
	/* Profile Package DTO End */

	/* Friend Package DTO Start */
	public static FriendPhoneNumberAddRequest friendPhoneNumberAddRequest(String phoneNumber, String displayName) {
		return new FriendPhoneNumberAddRequest(phoneNumber, displayName);
	}
	/* Friend Package DTO End */
}
