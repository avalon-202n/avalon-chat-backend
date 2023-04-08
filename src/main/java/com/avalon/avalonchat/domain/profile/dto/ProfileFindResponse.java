package com.avalon.avalonchat.domain.profile.dto;

import javax.validation.constraints.NotNull;

import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ProfileFindResponse {

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

	public ProfileFindResponse(
		String nickname,
		String bio,
		String profileImageUrl,
		String backgroundImageUrl
	) {
		this.nickname = nickname;
		this.bio = bio;
		this.profileImageUrl = profileImageUrl;
		this.backgroundImageUrl = backgroundImageUrl;
	}

	public static ProfileFindResponse ofEntity(
		Profile profile,
		ProfileImage profileImage,
		BackgroundImage backgroundImage
	) {
		return new ProfileFindResponse(
			profile.getNickname(),
			profile.getBio(),
			profileImage.getUrl(),
			backgroundImage.getUrl()
		);
	}
}
