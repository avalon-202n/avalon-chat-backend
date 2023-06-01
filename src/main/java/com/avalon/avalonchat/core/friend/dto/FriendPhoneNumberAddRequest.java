package com.avalon.avalonchat.core.friend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendPhoneNumberAddRequest {
	@Schema(description = "핸드폰 번호", example = "010-1234-5678")
	private String phoneNumber;
	@Schema(description = "친구 프로필 닉네임", example = "홍길동")
	private String friendProfileNickname;
}
