package com.avalon.avalonchat.core.login.application;

import java.util.Map;

import com.avalon.avalonchat.core.user.domain.User;

public interface TokenService {
	Map<String, Object> parseClaim(String token);

	String createAccessToken(User user, long profileId);

	String createRefreshToken();

	boolean isExpired(String token);
}
