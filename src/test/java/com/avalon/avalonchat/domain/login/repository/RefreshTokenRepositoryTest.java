package com.avalon.avalonchat.domain.login.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avalon.avalonchat.domain.model.RefreshToken;
import com.avalon.avalonchat.testsupport.base.BaseTestContainerTest;

@SpringBootTest
public class RefreshTokenRepositoryTest extends BaseTestContainerTest {
	@Autowired
	private RefreshTokenRepository sut;

	@Test
	void 리프레시_토큰_생성_테스트() {
		String refreshTokenStr = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSZWZyZXNoVG9rZW4iLCJleHAiOjE2ODM1NTE5ODd9.jTotEcZNgS-A2Ji5m8L0EJ0tN-qYHhUwdoUesk4t4ufCrUBnk4h3FaQJoSGQrgQtSg1CIWttLD0I05vqZlcDuA";
		RefreshToken refreshToken = new RefreshToken(refreshTokenStr, 1);
		sut.save(refreshToken);
		RefreshToken savedRefreshToken = sut.findById(refreshTokenStr).get();
		System.out.println("foundChatMessages = " + savedRefreshToken.toString());
	}

	@AfterEach
	void tearDown() {
		sut.deleteAll();
	}
}
