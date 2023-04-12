package com.avalon.avalonchat.domain.auth.service;

import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberRequest;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberResponse;

public interface AuthService {
	AuthPhoneNumberResponse.Get getCode(AuthPhoneNumberRequest.Get request);

	AuthPhoneNumberResponse.Compare compareCode(AuthPhoneNumberRequest.Compare request);
}
