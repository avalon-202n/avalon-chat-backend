package com.avalon.avalonchat.global.util;

import static com.avalon.avalonchat.global.util.Preconditions.*;

import org.springframework.context.support.MessageSourceAccessor;

public class MessageUtils {

	private static MessageSourceAccessor messageSourceAccessor;

	public static String getMessage(String key) {
		checkNotNull(messageSourceAccessor, "MessageSourceAccessor is not initialized.");
		return messageSourceAccessor.getMessage(key);
	}

	public static String getMessage(String key, Object... params) {
		checkNotNull(messageSourceAccessor, "MessageSourceAccessor is not initialized.");
		return messageSourceAccessor.getMessage(key, params);
	}

	public static void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
		MessageUtils.messageSourceAccessor = messageSourceAccessor;
	}
}
