package com.avalon.avalonchat.core.friend.dto;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendSynchronizeRequest {
	@Schema(description = "추가할 친구의 핸드폰 번호, 이름 정보 맵", example = "{010-1234-5678 : 홍길동99, ...}")
	private Map<String, String> friendsInfo;
}
