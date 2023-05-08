package com.avalon.avalonchat.domain.login.service;

import java.util.Map;

import com.avalon.avalonchat.domain.user.domain.User;

public interface TokenService {
	Map<String, Object> parseClaim(String token);

	String createAccessToken(User user, long profileId);

	String createRefreshToken();

	boolean isExpired(String token);
}
