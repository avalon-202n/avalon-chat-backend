package com.avalon.avalonchat.domain.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avalon.avalonchat.domain.friend.domain.Friend;

public interface FriendRepository extends JpaRepository<Friend, Long> {
}
