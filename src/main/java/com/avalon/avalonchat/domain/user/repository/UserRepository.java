package com.avalon.avalonchat.domain.user.repository;

import com.avalon.avalonchat.domain.user.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import com.avalon.avalonchat.domain.user.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(Email email);
}
