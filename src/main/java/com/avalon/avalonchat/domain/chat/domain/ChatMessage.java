package com.avalon.avalonchat.domain.chat.domain;

import static com.avalon.avalonchat.global.util.Preconditions.*;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Id;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.redis.core.RedisHash;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "id")
@RedisHash("chat_message")
public class ChatMessage {

	@Id
	private String id;

	private long senderId;

	private String chatRoomId;

	private String content;

	private LocalDateTime createdAt;

	public ChatMessage(String content, long senderId, String chatRoomId) {
		checkNotNull(content, "chatMessage.content cannot be null");

		this.id = UUID.randomUUID().toString();
		this.senderId = senderId;
		this.chatRoomId = chatRoomId;
		this.content = content;
		this.createdAt = LocalDateTime.now();
	}

	@PersistenceCreator
	public ChatMessage() {
	}
}
