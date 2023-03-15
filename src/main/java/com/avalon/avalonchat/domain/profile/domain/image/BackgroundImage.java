package com.avalon.avalonchat.domain.profile.domain.image;

import com.avalon.avalonchat.domain.model.BaseDateTimeEntity;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BackgroundImage extends BaseDateTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id")
	private Profile profile;

	@Column
	private String url;

	public BackgroundImage(Profile profile, String url) {
		this.profile = profile;
		this.url = url;
	}
}
