package com.avalon.avalonchat.domain.profile.dto;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProfileSummaryGetRequestTest {
	@Test
	void 친구목록조회_검색어null_생성성공() {
		// when
		ProfileSummaryGetRequest request = new ProfileSummaryGetRequest(null);

		// then
		assertThat(request.getNickname()).isEqualTo("");
	}

	@Test
	void 친구목록조회_검색어notNull_생성성공() {
		// when
		ProfileSummaryGetRequest request = new ProfileSummaryGetRequest("nickname");

		// then
		assertThat(request.getNickname()).isEqualTo("nickname");
	}
}
