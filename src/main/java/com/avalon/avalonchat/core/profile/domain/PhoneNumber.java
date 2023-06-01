package com.avalon.avalonchat.core.profile.domain;

import com.avalon.avalonchat.core.user.domain.Email;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.util.regex.Pattern;

import static com.avalon.avalonchat.global.util.Preconditions.checkPatternMatches;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode
@Embeddable
public class PhoneNumber {

	private static final Pattern PHONENUMBER_PATTERN = Pattern.compile("^\\d{3}-\\d{3,4}-\\d{4}$");

	@Column(name = "phoneNumber", nullable = false)
	private String value;

	private PhoneNumber(String value) {
		checkPatternMatches(PHONENUMBER_PATTERN, value);
		this.value = value.replaceAll("-", "").trim();
	}

	public static PhoneNumber of(String value) {
		return new PhoneNumber(value);
	}

}
