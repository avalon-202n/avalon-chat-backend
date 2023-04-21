package com.avalon.avalonchat.domain.profile.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.avalon.avalonchat.domain.model.BaseAuditingEntity;
import com.avalon.avalonchat.domain.user.domain.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile extends BaseAuditingEntity {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id", unique = true)
	private User user;

	@Column
	private String bio;

	@Column
	private LocalDate birthDate;

	@Column
	private String nickname;

	@Setter
	@Column(nullable = false, unique = true)
	private String phoneNumber;

	@OneToMany(mappedBy = "profile", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<ProfileImage> profileImages = new ArrayList<>();

	@OneToMany(mappedBy = "profile", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<BackgroundImage> backgroundImages = new ArrayList<>();

	public Profile(User user, String bio, LocalDate birthDate, String nickname, String phoneNumber) {
		this.user = user;
		this.bio = bio;
		this.birthDate = birthDate;
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
	}

	public void addProfileImage(ProfileImage profileImage) {
		profileImages.add(profileImage);
		profileImage.setProfile(this);
	}

	public void addBackgroundImage(BackgroundImage backgroundImage) {
		backgroundImages.add(backgroundImage);
		backgroundImage.setProfile(this);
	}
}
