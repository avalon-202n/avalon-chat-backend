package com.avalon.avalonchat.domain.auth.service;

import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberCheckRequest;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberCheckResponse;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberSendRequest;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberSendResponse;

public interface AuthService {
	AuthPhoneNumberSendResponse getCode(AuthPhoneNumberSendRequest request);

	AuthPhoneNumberCheckResponse compareCode(AuthPhoneNumberCheckRequest request);
}
