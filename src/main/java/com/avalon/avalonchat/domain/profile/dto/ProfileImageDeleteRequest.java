package com.avalon.avalonchat.domain.profile.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileImageDeleteRequest {

	@Schema(description = "삭제할 프로필 이미지 url 리스트", example = "url1, url2, ...")
	private List<String> deletedProfileImageUrls;
}
