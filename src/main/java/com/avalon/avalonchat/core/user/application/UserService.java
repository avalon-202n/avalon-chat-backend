package com.avalon.avalonchat.core.user.application;

import com.avalon.avalonchat.core.user.dto.EmailDuplicatedCheckRequest;
import com.avalon.avalonchat.core.user.dto.EmailDuplicatedCheckResponse;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationCheckRequest;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationCheckResponse;
import com.avalon.avalonchat.core.user.dto.PhoneNumberAuthenticationSendRequest;
import com.avalon.avalonchat.core.user.dto.SignUpRequest;
import com.avalon.avalonchat.core.user.dto.SignUpResponse;

public interface UserService {

	SignUpResponse signUp(SignUpRequest signUpRequest);

	EmailDuplicatedCheckResponse checkEmailDuplicated(EmailDuplicatedCheckRequest request);

	void sendPhoneNumberAuthentication(PhoneNumberAuthenticationSendRequest request);

	PhoneNumberAuthenticationCheckResponse checkPhoneNumberAuthentication(
		PhoneNumberAuthenticationCheckRequest request);
}
