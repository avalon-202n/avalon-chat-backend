package com.avalon.avalonchat.domain.user.repository;

import org.springframework.data.repository.CrudRepository;

import com.avalon.avalonchat.domain.user.domain.PhoneNumberAuthenticationCode;

public interface PhoneNumberAuthenticationRepository extends CrudRepository<PhoneNumberAuthenticationCode, String> {
}
