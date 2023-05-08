package com.avalon.avalonchat.domain.login.repository;

import org.springframework.data.repository.CrudRepository;

import com.avalon.avalonchat.domain.model.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
