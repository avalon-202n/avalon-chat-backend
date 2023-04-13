package com.avalon.avalonchat.domain.auth.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.avalon.avalonchat.domain.auth.domain.AuthPhoneNumberCode;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberCheckRequest;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberCheckResponse;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberSendRequest;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberSendResponse;
import com.avalon.avalonchat.domain.auth.repository.AuthPhoneNumberRepository;
import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;
import com.avalon.avalonchat.infra.message.MessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final MessageService messageService;
	private final AuthPhoneNumberRepository authPhoneNumberRepository;

	@Override
	public AuthPhoneNumberSendResponse getCode(AuthPhoneNumberSendRequest request) {
		String code = getCode();
		messageService.sendMessage(request.getPhoneNumber(), code);

		AuthPhoneNumberCode authPhoneNumberCode = new AuthPhoneNumberCode(request.getPhoneNumber(), code);
		AuthPhoneNumberCode savedAuthPhoneNumberCode = authPhoneNumberRepository.save(authPhoneNumberCode);

		return AuthPhoneNumberSendResponse.ofEntity(savedAuthPhoneNumberCode);
	}

	@Override
	public AuthPhoneNumberCheckResponse compareCode(AuthPhoneNumberCheckRequest request) {
		AuthPhoneNumberCode authPhoneNumberCode = authPhoneNumberRepository.findById(request.getPhoneNumber())
			.orElseThrow(() -> new AvalonChatRuntimeException("PhoneNumber Not found"));

		if (authPhoneNumberCode.getCode().equals(request.getCode())) {
			authPhoneNumberCode.authenticate();
			authPhoneNumberRepository.save(authPhoneNumberCode);
			return AuthPhoneNumberCheckResponse.ofEntity(authPhoneNumberCode);
		}
		return AuthPhoneNumberCheckResponse.ofEntity(authPhoneNumberCode);
	}

	private String getCode() {
		return RandomStringUtils.randomNumeric(6);
	}
}
