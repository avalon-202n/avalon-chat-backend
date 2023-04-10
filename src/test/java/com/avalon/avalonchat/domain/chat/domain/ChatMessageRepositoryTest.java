package com.avalon.avalonchat.domain.chat.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.avalon.avalonchat.testsupport.extension.RedisExtension;

@ExtendWith(RedisExtension.class)
@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest
class ChatMessageRepositoryTest {

	@Autowired
	private ChatMessageRepository sut;

	@Test
	void 채팅_메시지에대한_Create_Read_테스트() {
		// given
		ChatMessage chatMessage1 = new ChatMessage("안녕하세요 ㅎ.ㅎ", 1L, "고독한 인사방");
		ChatMessage chatMessage2 = new ChatMessage("네 안녕하세요 1L 님", 2L, "고독한 인사방");

		// when
		ChatMessage savedChatMessage1 = sut.save(chatMessage1);
		ChatMessage savedChatMessage2 = sut.save(chatMessage2);
		Iterable<ChatMessage> foundChatMessages = sut.findAll();

		// then
		assertThat(savedChatMessage1.getId()).isNotNull();
		assertThat(savedChatMessage1.getSenderId()).isEqualTo(1L);
		assertThat(savedChatMessage1.getContent()).isEqualTo("안녕하세요 ㅎ.ㅎ");
		assertThat(savedChatMessage1.getChatRoomId()).isEqualTo("고독한 인사방");
		assertThat(savedChatMessage1.getCreatedAt()).isBefore(LocalDateTime.now());

		assertThat(savedChatMessage2.getId()).isNotNull();
		assertThat(savedChatMessage2.getSenderId()).isEqualTo(2L);
		assertThat(savedChatMessage2.getContent()).isEqualTo("네 안녕하세요 1L 님");
		assertThat(savedChatMessage2.getChatRoomId()).isEqualTo("고독한 인사방");
		assertThat(savedChatMessage2.getCreatedAt()).isBefore(LocalDateTime.now());

		assertThat(foundChatMessages).asList().containsExactlyInAnyOrder(chatMessage1, chatMessage2);
	}

	@Test
	void 채팅_메시지에대한_Create_Read_테스트2() {
		// given
		ChatMessage chatMessage1 = new ChatMessage("안녕하세요 ㅎ.ㅎ", 1L, "고독한 인사방");
		ChatMessage chatMessage2 = new ChatMessage("네 안녕하세요 1L 님", 2L, "고독한 인사방");

		// when
		ChatMessage savedChatMessage1 = sut.save(chatMessage1);
		ChatMessage savedChatMessage2 = sut.save(chatMessage2);
		Iterable<ChatMessage> foundChatMessages = sut.findAll();

		// then
		assertThat(savedChatMessage1.getId()).isNotNull();
		assertThat(savedChatMessage1.getSenderId()).isEqualTo(1L);
		assertThat(savedChatMessage1.getContent()).isEqualTo("안녕하세요 ㅎ.ㅎ");
		assertThat(savedChatMessage1.getChatRoomId()).isEqualTo("고독한 인사방");
		assertThat(savedChatMessage1.getCreatedAt()).isBefore(LocalDateTime.now());

		assertThat(savedChatMessage2.getId()).isNotNull();
		assertThat(savedChatMessage2.getSenderId()).isEqualTo(2L);
		assertThat(savedChatMessage2.getContent()).isEqualTo("네 안녕하세요 1L 님");
		assertThat(savedChatMessage2.getChatRoomId()).isEqualTo("고독한 인사방");
		assertThat(savedChatMessage2.getCreatedAt()).isBefore(LocalDateTime.now());

		assertThat(foundChatMessages).asList().containsExactlyInAnyOrder(chatMessage1, chatMessage2);
	}

	@AfterEach
	void tearDown() {
		sut.deleteAll();
	}
}
