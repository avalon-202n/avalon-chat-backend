package com.avalon.avalonchat.domain.friend.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class FriendAddRequest {
	@Schema(description = "핸드폰 번호 리스트", example = "[010-1234-5678, 010-1212-3434]")
	private final List<String> phoneNumbers;

	public FriendAddRequest(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
}
