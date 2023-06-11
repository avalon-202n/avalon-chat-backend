package com.avalon.avalonchat.core.user.domain;

import static com.avalon.avalonchat.global.util.Preconditions.*;
import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.*;

import javax.persistence.*;

import com.avalon.avalonchat.global.model.BaseAuditingEntity;
import com.avalon.avalonchat.global.util.BooleanToYnConverter;

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

	@Enumerated(STRING)
	private Status status;

	public User(Email email, Password password) {
		checkNotNull(email, "User.email cannot be null");
		checkNotNull(password, "User.password cannot be null");

		this.email = email;
		this.password = password;
		this.status = Status.NO_PROFILE;
	}

	public void activeStatus() {
		this.status = Status.ACTIVE;
	}

	public enum Status {
		NO_PROFILE,
		ACTIVE,
		INACTIVE
	}
}
