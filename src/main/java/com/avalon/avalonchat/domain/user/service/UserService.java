package com.avalon.avalonchat.domain.user.service;

import com.avalon.avalonchat.domain.user.dto.SignUpRequest;
import com.avalon.avalonchat.domain.user.dto.SignUpResponse;

public interface UserService {

	SignUpResponse signUp(SignUpRequest signUpRequest);
}
