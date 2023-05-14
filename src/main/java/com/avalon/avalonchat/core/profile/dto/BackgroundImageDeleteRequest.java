package com.avalon.avalonchat.core.profile.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BackgroundImageDeleteRequest {

	@Schema(description = "삭제할 배경 이미지 url 리스트", example = "url1, url2, ...")
	private List<String> deletedBackgroundImageUrls;
}
