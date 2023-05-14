package com.avalon.avalonchat.core.login.repository;

import org.springframework.data.repository.CrudRepository;

import com.avalon.avalonchat.global.model.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
