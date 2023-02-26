package com.avalon.avalonchat.domain.profile.domain;

import com.avalon.avalonchat.domain.embed.EntityDate;
import com.avalon.avalonchat.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "profiles")
public class Profile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;

	@OneToOne
	@JoinColumn(name = "users_id")
	private User user;

	@Column
	private String bio;

	@Column(name = "birth_date")
	private LocalDateTime birthDate;

	@Column
	private String nickname;

	@Column
	private String phone;

	@Embedded
	private EntityDate entityDate;
}
