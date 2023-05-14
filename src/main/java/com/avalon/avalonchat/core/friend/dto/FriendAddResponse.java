package com.avalon.avalonchat.core.friend.dto;

import com.avalon.avalonchat.core.friend.domain.Friend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendAddResponse {
	@Schema(description = "친구 프로필 ID", example = "1")
	private long friendProfileId;
	@Schema(description = "친구 프로필 닉네임", example = "nickName")
	private String nickname;
	@Schema(description = "친구 프로필 상태메시지", example = "hi there")
	private String bio;
	@Schema(description = "친구 프로필 이미지 주소", example = "http://profile/image/url")
	private String profileImage;
	@Schema(description = "친구 상태", example = "NORMAL")
	private Friend.Status status;
}
