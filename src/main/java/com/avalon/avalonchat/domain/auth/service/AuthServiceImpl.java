package com.avalon.avalonchat.domain.auth.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.avalon.avalonchat.domain.auth.domain.AuthPhoneNumber;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberRequest;
import com.avalon.avalonchat.domain.auth.dto.AuthPhoneNumberResponse;
import com.avalon.avalonchat.domain.auth.repository.AuthPhoneNumberRepository;
import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final MessageService messageService;
	private final AuthPhoneNumberRepository authPhoneNumberRepository;

	@Override
	public AuthPhoneNumberResponse.Get getCode(AuthPhoneNumberRequest.Get request) {
		String code = getCode();
		messageService.sendMessage(request.getPhoneNumber(), code);

		AuthPhoneNumber authPhoneNumber = new AuthPhoneNumber(request.getPhoneNumber(), code);
		AuthPhoneNumber savedAuthPhoneNumber = authPhoneNumberRepository.save(authPhoneNumber);

		return AuthPhoneNumberResponse.Get.ofEntity(savedAuthPhoneNumber);
	}

	@Override
	public AuthPhoneNumberResponse.Compare compareCode(AuthPhoneNumberRequest.Compare request) {
		AuthPhoneNumber authPhoneNumber = authPhoneNumberRepository.findById(request.getPhoneNumber())
			.orElseThrow(() -> new AvalonChatRuntimeException("PhoneNumber Not found"));

		if (authPhoneNumber.getCode().equals(request.getCode())) {
			authPhoneNumber.validate();
			authPhoneNumberRepository.save(authPhoneNumber);
			return AuthPhoneNumberResponse.Compare.ofEntity(true);
		}
		return AuthPhoneNumberResponse.Compare.ofEntity(false);
	}

	private String getCode() {
		StringBuffer code = new StringBuffer();
		Random random = new Random();

		for (int i = 0; i < 6; i++) {
			code.append((random.nextInt(10)));
		}

		return code.toString();
	}
}
