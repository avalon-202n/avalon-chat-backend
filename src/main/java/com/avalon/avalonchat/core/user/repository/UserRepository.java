package com.avalon.avalonchat.core.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avalon.avalonchat.core.user.domain.Email;
import com.avalon.avalonchat.core.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(Email email);

	boolean existsByEmail(Email email);
}
