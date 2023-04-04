package com.avalon.avalonchat.domain.profile.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.avalon.avalonchat.domain.model.BaseAuditingEntity;
import com.avalon.avalonchat.domain.user.domain.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile extends BaseAuditingEntity {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id")
	private User user;

	@Column
	private String bio;

	@Column
	private LocalDate birthDate;

	@Column
	private String nickname;

	@Column
	private String phoneNumber;

	public Profile(User user, String bio, LocalDate birthDate, String nickname) {
		this.user = user;
		this.bio = bio;
		this.birthDate = birthDate;
		this.nickname = nickname;
	}
}
