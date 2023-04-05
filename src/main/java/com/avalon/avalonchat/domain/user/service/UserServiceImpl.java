package com.avalon.avalonchat.domain.user.service;

import com.avalon.avalonchat.domain.user.domain.Password;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.dto.SignUpRequest;
import com.avalon.avalonchat.domain.user.dto.SignUpResponse;
import com.avalon.avalonchat.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public SignUpResponse signUp(SignUpRequest signUpRequest) {
		// create user from request
		String encryptedPassword = bCryptPasswordEncoder.encode(signUpRequest.getPassword().getValue());

		User user = signUpRequest.toEntity();
		//user.encryptPassword(Password.of(encryptedPassword));
		// TODO - password vo 도입 및 encoding 적용

		// save user
		User savedUser = userRepository.save(user);

		// convert to response
		return SignUpResponse.ofEntity(savedUser);
	}
}
