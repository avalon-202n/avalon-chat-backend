package com.avalon.avalonchat.core.login.application;

import java.util.Map;

import com.avalon.avalonchat.core.profile.domain.Profile;

public interface TokenService {
	Map<String, Object> parseClaim(String token);

	String createAccessToken(Profile profile);

	String createRefreshToken();

	boolean isExpired(String token);
}
