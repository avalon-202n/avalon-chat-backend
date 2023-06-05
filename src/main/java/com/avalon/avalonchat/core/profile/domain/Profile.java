package com.avalon.avalonchat.core.profile.domain;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.avalon.avalonchat.core.user.domain.User;
import com.avalon.avalonchat.global.model.BaseAuditingEntity;

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
		updateLatestProfileImageUrl(profileImageUrl);
		ProfileImage profileImage = new ProfileImage(this, profileImageUrl);
		profileImages.add(profileImage);
	}

	public void addBackgroundImage(String backgroundImageUrl) {
		BackgroundImage backgroundImage = new BackgroundImage(this, backgroundImageUrl);
		backgroundImages.add(backgroundImage);
	}

	public void updateLatestProfileImageUrl(String latestProfileImageUrl) {
		this.latestProfileImageUrl = latestProfileImageUrl;
	}

	public void deleteProfileImage(String deletedProfileImageUrl) {
		List<ProfileImage> deleteImages = profileImages.stream()
			.filter(profileImage -> profileImage.getUrl().equals(deletedProfileImageUrl))
			.collect(Collectors.toList());

		profileImages.removeAll(deleteImages);
	}

	public void deleteBackgroundImage(String deleteBackgroundImageUrl) {
		List<BackgroundImage> deleteImages = backgroundImages.stream()
			.filter(backgroundImage -> backgroundImage.getUrl().equals(deleteBackgroundImageUrl))
			.collect(Collectors.toList());

		backgroundImages.removeAll(deleteImages);
	}

	public void update(String bio, LocalDate birthDate, String nickname, String phoneNumber) {
		this.bio = bio;
		this.birthDate = birthDate;
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
	}
}
