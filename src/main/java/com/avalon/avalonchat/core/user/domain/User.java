package com.avalon.avalonchat.core.user.domain;

import static com.avalon.avalonchat.global.util.Preconditions.*;
import static lombok.AccessLevel.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.avalon.avalonchat.core.model.BaseAuditingEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "users")
@Entity
public class User extends BaseAuditingEntity {

	@Embedded
	private Email email;

	@Embedded
	private Password password;

	public User(Email email, Password password) {
		checkNotNull(email, "User.email cannot be null");
		checkNotNull(password, "User.password cannot be null");

		this.email = email;
		this.password = password;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.password.setEncryptedPassword(encryptedPassword);
	}
}
