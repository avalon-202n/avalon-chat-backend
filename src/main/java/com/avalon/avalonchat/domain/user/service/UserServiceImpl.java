package com.avalon.avalonchat.domain.user.service;

import org.springframework.stereotype.Service;

import com.avalon.avalonchat.domain.user.dto.SignUpRequest;
import com.avalon.avalonchat.domain.user.dto.SignUpResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	@Override
	public SignUpResponse signUp(SignUpRequest signUpRequest) {
		return null;
	}
}
