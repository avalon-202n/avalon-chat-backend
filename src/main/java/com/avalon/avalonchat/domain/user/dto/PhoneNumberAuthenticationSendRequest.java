package com.avalon.avalonchat.domain.user.dto;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhoneNumberAuthenticationSendRequest {

	@NotNull
	@Schema(description = "핸드폰 번호", example = "010-1234-5678")
	private String phoneNumber;
}
