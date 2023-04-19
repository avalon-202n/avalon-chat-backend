package com.avalon.avalonchat.domain.login.service;

import com.avalon.avalonchat.domain.login.dto.EmailFindRequest;
import com.avalon.avalonchat.domain.login.dto.EmailFindResponse;
import com.avalon.avalonchat.domain.login.dto.LoginRequest;
import com.avalon.avalonchat.domain.login.dto.LoginResponse;
import com.avalon.avalonchat.domain.login.dto.PasswordFindRequest;
import com.avalon.avalonchat.domain.login.dto.PasswordFindResponse;

public interface LoginService {

	LoginResponse login(LoginRequest request);

	EmailFindResponse findEmailByPhoneNumber(EmailFindRequest request);

	PasswordFindResponse findPasswordByEmail(PasswordFindRequest request);
}
