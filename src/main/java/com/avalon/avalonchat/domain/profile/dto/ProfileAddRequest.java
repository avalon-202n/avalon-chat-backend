package com.avalon.avalonchat.domain.profile.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ProfileAddRequest {

	@NotNull
	@Schema(description = "생년월일", example = "2000.01.01")
	private final LocalDate birthDate;

	@NotNull
	@Schema(description = "닉네임", example = "nickName")
	private final String nickname;

	@Schema(description = "상태 메시지", example = "hi there")
	private final String bio;

	@Schema(description = "프로필 이미지 주소", example = "http://profile/image/url")
	private final String profileImageUrl;

	@Schema(description = "배경 이미지 주소", example = "http://background/image/url")
	private final String backgroundImageUrl;

	@Schema(description = "핸드폰 번호", example = "010-1234-5678")
	private final String phoneNumber;

	public ProfileAddRequest(
		LocalDate birthDate,
		String nickname,
		String bio,
		String profileImageUrl,
		String backgroundImageUrl,
		String phoneNumber
	) {
		this.birthDate = birthDate;
		this.nickname = nickname;
		this.bio = bio;
		this.profileImageUrl = profileImageUrl;
		this.backgroundImageUrl = backgroundImageUrl;
		this.phoneNumber = phoneNumber;
	}
}
