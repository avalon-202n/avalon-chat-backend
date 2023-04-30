package com.avalon.avalonchat.domain.friend.dto;

import com.avalon.avalonchat.domain.friend.domain.Friend.Status;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendStatusUpdateRequest {
	@Schema(description = "변경할 친구 상태", example = "FAVORITES")
	private Status status;
}
