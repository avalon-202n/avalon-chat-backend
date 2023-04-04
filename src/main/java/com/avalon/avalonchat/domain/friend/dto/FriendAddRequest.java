package com.avalon.avalonchat.domain.friend.dto;

import lombok.Getter;

@Getter
public class FriendAddRequest {
	private final String[] phoneNumbers;

	//TODO: 프론트쪽과 연락처 리스트를 어떠한 형태로 보내줄 것인지 request 체크 후 리팩토링 필요
	public FriendAddRequest(String[] phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
}
