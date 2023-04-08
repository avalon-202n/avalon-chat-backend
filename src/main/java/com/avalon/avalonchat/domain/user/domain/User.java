package com.avalon.avalonchat.domain.user.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.avalon.avalonchat.domain.model.BaseAuditingEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseAuditingEntity {

	@Embedded
	private Email email;

	@Embedded
	private Password password;

	public User(Email email, Password password) {
		this.email = email;
		this.password = password;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.password.setEncryptedPassword(encryptedPassword);
	}
}
