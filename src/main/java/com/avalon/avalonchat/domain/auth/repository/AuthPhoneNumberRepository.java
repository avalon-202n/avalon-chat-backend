package com.avalon.avalonchat.domain.auth.repository;

import org.springframework.data.repository.CrudRepository;

import com.avalon.avalonchat.domain.auth.domain.AuthPhoneNumberCode;

public interface AuthPhoneNumberRepository extends CrudRepository<AuthPhoneNumberCode, String> {
}
