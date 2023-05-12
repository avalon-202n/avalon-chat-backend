package com.avalon.avalonchat.core.user.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(Email email);

	boolean existsByEmail(Email email);
}
