package com.avalon.avalonchat.domain.profile.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ProfileAddResponse {

	@NotNull
	@Schema(description = "생년월일", example = "2000.01.01")
	private final LocalDate birthDate;

	@NotNull
	@Schema(description = "닉네임", example = "nickName")
	private final String nickname;

	@NotNull
	@Schema(description = "상태 메시지", example = "hi there")
	private final String bio;

	@NotNull
	@Schema(description = "프로필 이미지 주소", example = "http://profile/image/url")
	private final String profileImageUrl;

	@NotNull
	@Schema(description = "배경 이미지 주소", example = "http://background/image/url")
	private final String backgroundImageUrl;

	public ProfileAddResponse(
		LocalDate birthDate,
		String nickname,
		String bio,
		String profileImageUrl,
		String backgroundImageUrl) {
		this.birthDate = birthDate;
		this.nickname = nickname;
		this.bio = bio;
		this.profileImageUrl = profileImageUrl;
		this.backgroundImageUrl = backgroundImageUrl;
	}

	public static ProfileAddResponse ofEntity(Profile profile, ProfileImage profileImage,
		BackgroundImage backgroundImage) {
		return new ProfileAddResponse(
			profile.getBirthDate(),
			profile.getNickname(),
			profile.getBio(),
			profileImage.getUrl(),
			backgroundImage.getUrl()
		);
	}
}
