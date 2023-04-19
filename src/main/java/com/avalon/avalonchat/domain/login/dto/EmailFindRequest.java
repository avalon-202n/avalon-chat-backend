package com.avalon.avalonchat.domain.login.dto;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailFindRequest {
	@NotNull
	@Schema(description = "전화번호", example = "010-1234-5678")
	private String phoneNumber;
}
