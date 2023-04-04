package com.avalon.avalonchat.domain.user.domain;

import javax.persistence.Column;
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

	@Column(nullable = false)
	private String password;

	public User(Email email, String password) {
		this.email = email;
		this.password = password;
	}
}
