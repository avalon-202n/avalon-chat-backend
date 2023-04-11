package com.avalon.avalonchat.domain.profile.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.avalon.avalonchat.domain.model.BaseAuditingEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage extends BaseAuditingEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id")
	@Setter
	private Profile profile;

	@Column
	private String url;

	public ProfileImage(Profile profile, String url) {
		this.profile = profile;
		this.url = url;
	}
}
