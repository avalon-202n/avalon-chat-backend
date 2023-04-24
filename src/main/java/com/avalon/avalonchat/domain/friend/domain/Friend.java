package com.avalon.avalonchat.domain.friend.domain;

import static com.avalon.avalonchat.global.util.Preconditions.*;
import static javax.persistence.EnumType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.avalon.avalonchat.domain.model.BaseAuditingEntity;
import com.avalon.avalonchat.domain.profile.domain.Profile;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 친구 (친구 관계)
 * - 현재 사용자 (myProfile) 과 친구 사용자 (friendProfile) 의 관계를 의미한다.
 * - A -> B 관계가 B -> A 관계를 의미하지 않는다.
 */
@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Friend extends BaseAuditingEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "myProfileId")
	private Profile myProfile;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "friendProfileId")
	private Profile friendProfile;

	@Enumerated(STRING)
	private Status status;

	public Friend(Profile myProfile, Profile friendProfile) {
		checkNotNull(myProfile, "Friend.myProfile cannot be null");
		checkNotNull(myProfile, "Friend.friendProfile cannot be null");

		this.myProfile = myProfile;
		this.friendProfile = friendProfile;
		this.status = Status.NORMAL;
	}

	public void updateStatus(Status status) {
		this.status = status;
	}

	/* 친구 상태*/
	public enum Status {
		NORMAL,
		FAVORITES,
		BLOCKED,
		HIDDEN
	}
}
