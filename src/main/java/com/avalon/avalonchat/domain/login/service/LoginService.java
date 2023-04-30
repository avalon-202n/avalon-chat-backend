package com.avalon.avalonchat.domain.login.service;

import com.avalon.avalonchat.domain.login.dto.EmailFindResponse;
import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;
import com.avalon.avalonchat.domain.login.dto.PasswordFindRequest;
import com.avalon.avalonchat.domain.login.dto.PasswordFindResponse;
import com.avalon.avalonchat.domain.login.dto.TokenReissueRequest;
import com.avalon.avalonchat.domain.login.dto.TokenReissueResponse;

public interface LoginService {

	LoginResponse login(LoginRequest request);

	EmailFindResponse findEmailByPhoneNumber(String request);

	PasswordFindResponse resetPassword(PasswordFindRequest request);

	TokenReissueResponse reissueToken(TokenReissueRequest request);
}
