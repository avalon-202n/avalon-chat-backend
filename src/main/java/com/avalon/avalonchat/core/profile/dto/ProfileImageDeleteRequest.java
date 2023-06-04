package com.avalon.avalonchat.core.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileImageDeleteRequest {
	@Schema(description = "삭제할 프로필 이미지 url", example = "http://profile/image/url")
	private String deletedProfileImageUrl;
}
