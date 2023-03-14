package com.avalon.avalonchat.testsupport;

import com.avalon.avalonchat.domain.profile.dto.ProfileRequest;
import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.dto.SignUpRequest;
import com.avalon.avalonchat.domain.user.dto.SignUpResponse;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public final class DtoFixture {

	public static SignUpRequest signUpRequest(String email, String password) {
		return new SignUpRequest(
			Email.of(email),
			password
		);
	}

	public static SignUpResponse expectedResponseOf(SignUpRequest request) {
		return new SignUpResponse(request.getEmail());
	}

	public static ProfileRequest profileRequest(
		LocalDate birthDate,
		String nickname,
		String bio,
		MultipartFile image,
		MultipartFile backgroudImage) {
		return new ProfileRequest(
			birthDate,
			nickname,
			bio,
			image,
			backgroudImage
		);
	}
}
