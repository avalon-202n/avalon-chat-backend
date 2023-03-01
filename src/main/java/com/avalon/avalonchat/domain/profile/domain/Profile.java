package com.avalon.avalonchat.domain.profile.domain;

import com.avalon.avalonchat.domain.model.BaseDateTimeEntity;
import com.avalon.avalonchat.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile extends BaseDateTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id")
	private User user;

	@Column
	private String bio;

	@Column(name = "birth_date")
	private LocalDate birthDate;

	@Column
	private String nickname;

	@Column
	private String phoneNumber;
}
