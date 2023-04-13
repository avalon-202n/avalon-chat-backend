package com.avalon.avalonchat.domain.auth.repository;

import org.springframework.data.repository.CrudRepository;

import com.avalon.avalonchat.domain.auth.domain.AuthPhoneNumber;

public interface AuthPhoneNumberRepository extends CrudRepository<AuthPhoneNumber, String> {
}
