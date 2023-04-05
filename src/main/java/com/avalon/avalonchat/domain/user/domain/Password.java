package com.avalon.avalonchat.domain.user.domain;

import com.avalon.avalonchat.global.util.Preconditions;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static com.avalon.avalonchat.global.util.Preconditions.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class Password {
	private static final int MAXIMUM_LENGTH = 16;
	private static final int MINIMUM_LENGTH = 7;

	@Column(name = "password", nullable = false)
	private String value;

	private Password(String value) {
		String message = "비밀번호의 길이는 " + MINIMUM_LENGTH + "이상 " + MAXIMUM_LENGTH + "이하 입니다.";
		checkLength(MAXIMUM_LENGTH, MINIMUM_LENGTH, value, message);
		this.value = new BCryptPasswordEncoder().encode(value);
	}

	public static Password of(String value) {
		return new Password(value);
	}
}
