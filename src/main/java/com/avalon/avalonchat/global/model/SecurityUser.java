package com.avalon.avalonchat.global.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * a principal of authenticated SecurityContext
 */
@Getter
@RequiredArgsConstructor
public class SecurityUser {

	private final long userId;
	private final long profileId;
}
