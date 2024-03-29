package com.avalon.avalonchat.core.profile.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileUpdateRequest {

	@NotNull
	@Schema(description = "생년월일", example = "2000.01.01")
	private LocalDate birthDate;

	@NotNull
	@Schema(description = "닉네임", example = "nickName")
	private String nickname;

	@Schema(description = "상태 메시지", example = "hi there")
	private String bio;

	@Schema(description = "프로필 이미지 주소", example = "http://profile/image/url")
	private String profileImageUrl;

	@Schema(description = "배경 이미지 주소", example = "http://background/image/url")
	private String backgroundImageUrl;

}
