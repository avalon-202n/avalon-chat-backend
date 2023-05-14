package com.avalon.avalonchat.core.login.application;

import com.avalon.avalonchat.core.login.dto.EmailFindResponse;
import com.avalon.avalonchat.core.login.dto.LoginRequest;
import com.avalon.avalonchat.core.login.dto.LoginResponse;
import com.avalon.avalonchat.core.login.dto.PasswordFindRequest;
import com.avalon.avalonchat.core.login.dto.PasswordFindResponse;
import com.avalon.avalonchat.core.login.dto.TokenReissueRequest;
import com.avalon.avalonchat.core.login.dto.TokenReissueResponse;

public interface LoginService {

	LoginResponse login(LoginRequest request);

	EmailFindResponse findEmailByPhoneNumber(String request);

	PasswordFindResponse resetPassword(PasswordFindRequest request);

	TokenReissueResponse reissueToken(TokenReissueRequest request);
}
