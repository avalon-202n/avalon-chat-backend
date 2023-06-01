package com.avalon.avalonchat.core.friend.dto;

import java.util.List;

import com.avalon.avalonchat.core.profile.domain.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendAddRequest {
	@Schema(description = "핸드폰 번호 리스트", example = "[010-1234-5678, 010-1212-3434]")
	private List<PhoneNumber> phoneNumbers;
}
