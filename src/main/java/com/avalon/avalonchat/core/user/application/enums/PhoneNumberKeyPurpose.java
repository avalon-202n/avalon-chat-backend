package com.avalon.avalonchat.core.user.application.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PhoneNumberKeyPurpose {
	EMAIL_FIND("::EMAIL_FIND"),
	SING_UP("::SING_UP");

	private final String name;
}
