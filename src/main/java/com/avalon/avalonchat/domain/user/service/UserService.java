package com.avalon.avalonchat.domain.user.service;

import com.avalon.avalonchat.domain.user.dto.EmailDuplicatedCheckRequest;
import com.avalon.avalonchat.domain.user.dto.EmailDuplicatedCheckResponse;
import com.avalon.avalonchat.domain.user.dto.PhoneNumberAuthenticationCheckRequest;
import com.avalon.avalonchat.domain.user.dto.PhoneNumberAuthenticationCheckResponse;
import com.avalon.avalonchat.domain.user.dto.PhoneNumberAuthenticationSendRequest;
import com.avalon.avalonchat.domain.user.dto.SignUpRequest;
import com.avalon.avalonchat.domain.user.dto.SignUpResponse;

public interface UserService {

	SignUpResponse signUp(SignUpRequest signUpRequest);

	EmailDuplicatedCheckResponse checkEmailDuplicated(EmailDuplicatedCheckRequest request);

	void sendPhoneNumberAuthentication(PhoneNumberAuthenticationSendRequest request);

	PhoneNumberAuthenticationCheckResponse checkPhoneNumberAuthentication(
		PhoneNumberAuthenticationCheckRequest request);
}
