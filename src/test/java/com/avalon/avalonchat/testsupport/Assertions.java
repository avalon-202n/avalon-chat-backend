package com.avalon.avalonchat.testsupport;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.ThrowableTypeAssert;

import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;

public final class Assertions {

	public ThrowableTypeAssert<AvalonChatRuntimeException> assertThatAvalonExceptionIsThrownBy() {
		return assertThatExceptionOfType(AvalonChatRuntimeException.class);
	}
}
