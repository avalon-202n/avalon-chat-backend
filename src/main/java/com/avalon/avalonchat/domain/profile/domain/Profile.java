package com.avalon.avalonchat.domain.profile.domain;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.avalon.avalonchat.domain.model.BaseAuditingEntity;
import com.avalon.avalonchat.domain.user.domain.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Profile extends BaseAuditingEntity {

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "users_id", unique = true)
	private User user;

	@Column
	private String bio;

	@Column
	private LocalDate birthDate;

	@Column
	private String nickname;

	@Column
	private String phoneNumber;

	@Column
	private String latestProfileImageUrl;

	@OneToMany(mappedBy = "profile", cascade = ALL, orphanRemoval = true)
	private List<ProfileImage> profileImages = new ArrayList<>();

	@OneToMany(mappedBy = "profile", cascade = ALL, orphanRemoval = true)
	private List<BackgroundImage> backgroundImages = new ArrayList<>();

	public Profile(User user, String bio, LocalDate birthDate, String nickname, String phoneNumber) {
		this.user = user;
		this.bio = bio;
		this.birthDate = birthDate;
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
	}

	public void addProfileImage(String profileImageUrl) {
		this.latestProfileImageUrl = profileImageUrl;
		ProfileImage profileImage = new ProfileImage(this, profileImageUrl);
		profileImages.add(profileImage);
	}

	public void addBackgroundImage(String backgroundImageUrl) {
		BackgroundImage backgroundImage = new BackgroundImage(this, backgroundImageUrl);
		backgroundImages.add(backgroundImage);
	}

	public void deleteProfileImage(int index) {
		profileImages.remove(index);
	}

	public void deleteBackgroundImage(int index) {
		backgroundImages.remove(index);
	}

	public void update(String bio, LocalDate birthDate, String nickname, String phoneNumber) {
		this.bio = bio;
		this.birthDate = birthDate;
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
	}
}
