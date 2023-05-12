package com.avalon.avalonchat.core.profile.domain;

import static com.avalon.avalonchat.global.util.Preconditions.*;
import static lombok.AccessLevel.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.avalon.avalonchat.global.model.BaseAuditingEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class ProfileImage extends BaseAuditingEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id")
	private Profile profile;

	@Column
	private String url;

	ProfileImage(Profile profile, String url) {
		checkNotNull(profile, "ProfileImage.profile cannot be null");
		checkNotNull(profile, "ProfileImage.url cannot be null");

		this.profile = profile;
		this.url = url;
	}
}
