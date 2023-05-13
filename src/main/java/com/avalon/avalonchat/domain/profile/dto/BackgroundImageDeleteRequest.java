package com.avalon.avalonchat.domain.profile.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class BackgroundImageDeleteRequest {

	@Schema(description = "삭제할 배경 이미지 인덱스 리스트", example = "[1, 2, ...]")
	private List<Integer> deletedBackgroundImageIndexes;
}
