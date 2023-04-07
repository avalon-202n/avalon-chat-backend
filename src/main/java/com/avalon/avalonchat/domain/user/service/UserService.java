package com.avalon.avalonchat.domain.user.service;

import com.avalon.avalonchat.domain.user.dto.*;

public interface UserService {

	SignUpResponse signUp(SignUpRequest signUpRequest);

	EmailDuplicatedCheckResponse checkEmailDuplicated(EmailDuplicatedCheckRequest request);

	void sendEmailAuthentication(EmailAuthenticationSendRequest request);

	EmailAuthenticationCheckResponse checkEmailAuthentication(EmailAuthenticationCheckRequest request);

	void sendPhoneNumberAuthentication(PhoneNumberAuthenticationSendRequest request);

	PhoneNumberAuthenticationCheckResponse checkPhoneNumberAuthentication(PhoneNumberAuthenticationCheckRequest request);
}
