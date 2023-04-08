package com.avalon.avalonchat.domain.user.service;

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
		User user = signUpRequest.toEntity();

		//password encode
		String encryptedPassword = bCryptPasswordEncoder.encode(signUpRequest.getPassword().getValue());
		user.setEncryptedPassword(encryptedPassword);

		// save user
		User savedUser = userRepository.save(user);

		// convert to response
		return SignUpResponse.ofEntity(savedUser);
	}
}
