package com.avalon.avalonchat.core.friend.dto;

import com.avalon.avalonchat.core.friend.domain.Friend;
import com.avalon.avalonchat.core.profile.domain.Profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendPhoneNumberAddResponse {
	@Schema(description = "친구 프로필 ID", example = "1")
	private long friendProfileId;
	@Schema(description = "친구 프로필 이름", example = "홍길동99")
	private String friendName;
	@Schema(description = "친구 프로필 상태메시지", example = "hi there")
	private String bio;
	@Schema(description = "친구 프로필 이미지 주소", example = "http://profile/image/url")
	private String profileImage;
	@Schema(description = "친구 상태", example = "NORMAL")
	private Friend.Status status;

	public static FriendPhoneNumberAddResponse of(Profile profile, Friend friend) {
		return new FriendPhoneNumberAddResponse(
			profile.getId(),
			friend.getFriendName(),
			profile.getBio(),
			profile.getLatestProfileImageUrl(),
			friend.getStatus()
		);
	}
}
