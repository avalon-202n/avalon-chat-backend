package com.avalon.avalonchat.domain.user.dto;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EmailDuplicatedCheckResponse {

	@NotNull
	@Schema(description = "이메일 중복 여부")
	private final boolean duplicated;
}
