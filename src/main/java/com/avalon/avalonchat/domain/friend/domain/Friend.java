package com.avalon.avalonchat.domain.friend.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.avalon.avalonchat.domain.profile.domain.Profile;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;

	/* insertable, updatable을 false로 설정하여 테이블 중복 매핑 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id", insertable = false, updatable = false)
	private Profile myProfile;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id")
	private Profile friendProfile;

	@Enumerated(EnumType.ORDINAL)
	private FriendStatus friendStatus;

	public Friend(Profile myProfile, Profile friendProfile, FriendStatus friendStatus) {
		this.myProfile = myProfile;
		this.friendProfile = friendProfile;
		this.friendStatus = friendStatus;
	}

	/* 친구상태 종류 */
	public enum FriendStatus {
		NORMAL,
		FAVORITES,
		BLOCKED,
		HIDDEN
	}
}
