package com.avalon.avalonchat.core.model;

import static com.avalon.avalonchat.global.util.Preconditions.*;

import java.util.concurrent.TimeUnit;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.Getter;

@Getter
@RedisHash(value = "refresh_token")
public class RefreshToken {
	@Id
	private String refreshToken;

	private long userId;

	@TimeToLive(unit = TimeUnit.DAYS)
	private Long expiration = 14L;

	public RefreshToken(String refreshToken, long userId) {
		checkNotNull(refreshToken, "RefreshToken.refreshToken cannot be null");

		this.refreshToken = refreshToken;
		this.userId = userId;
	}
}
