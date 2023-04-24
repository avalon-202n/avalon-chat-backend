package com.avalon.avalonchat.domain.profile.domain;

import static com.avalon.avalonchat.global.util.Preconditions.*;
import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.avalon.avalonchat.domain.model.BaseAuditingEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class BackgroundImage extends BaseAuditingEntity {

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "profile_id")
	private Profile profile;

	@Column
	private String url;

	BackgroundImage(Profile profile, String url) {
		checkNotNull(profile, "BackgroundImage.profile cannot be null");
		checkNotNull(profile, "BackgroundImage.url cannot be null");

		this.profile = profile;
		this.url = url;
	}
}
