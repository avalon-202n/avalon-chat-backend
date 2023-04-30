package com.avalon.avalonchat.domain.model;

import static com.avalon.avalonchat.global.util.Preconditions.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.Getter;
import lombok.ToString;

@Getter
@RedisHash(value = "refresh_token")
@ToString
public class RefreshToken {
	@Id
	private String refreshToken;

	private long userId;

	@TimeToLive
	private long expiration;

	public RefreshToken(String refreshToken, long userId) {
		checkNotNull(refreshToken, "RefreshToken.refreshToken cannot be null");
		checkNotNull(userId, "RefreshToken.userId cannot be null");

		this.refreshToken = refreshToken;
		this.userId = userId;
	}
}
