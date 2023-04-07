package com.avalon.avalonchat.domain.friend.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.avalon.avalonchat.domain.model.BaseAuditingEntity;
import com.avalon.avalonchat.domain.profile.domain.Profile;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend extends BaseAuditingEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "myProfileId")
	private Profile myProfile;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "friendProfileId")
	private Profile friendProfile;

	@Enumerated(EnumType.STRING)
	private FriendStatus friendStatus = FriendStatus.NORMAL;

	public Friend(Profile myProfile, Profile friendProfile) {
		this.myProfile = myProfile;
		this.friendProfile = friendProfile;
	}

	/* 친구상태 종류 */
	public enum FriendStatus {
		NORMAL,
		FAVORITES,
		BLOCKED,
		HIDDEN
	}
}
