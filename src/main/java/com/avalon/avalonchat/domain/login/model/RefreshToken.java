package com.avalon.avalonchat.domain.login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refreshToken")
public class RefreshToken {
	@Id
	private String userId;
	private String refreshToken;

	@TimeToLive
	private long expiredTime;
}
