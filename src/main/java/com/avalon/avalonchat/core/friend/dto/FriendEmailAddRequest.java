package com.avalon.avalonchat.core.friend.dto;

import com.avalon.avalonchat.core.user.domain.Email;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendEmailAddRequest {
	@Schema(description = "친구 이메일", example = "email0123@emakil.com")
	private Email email;
}
