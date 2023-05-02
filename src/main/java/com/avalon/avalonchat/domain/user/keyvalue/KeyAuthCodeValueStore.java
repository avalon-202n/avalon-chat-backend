package com.avalon.avalonchat.domain.user.keyvalue;

import com.avalon.avalonchat.global.keyvalue.KeyValueStore;

public interface KeyAuthCodeValueStore<K> extends KeyValueStore<K, AuthCodeValue> {

	default void put(K key, String certificationCode) {
		put(key, AuthCodeValue.ofUnauthenticated(certificationCode));
	}

	default boolean isAuthenticated(K key) {
		AuthCodeValue authCodeValue = get(key);
		return authCodeValue != null && authCodeValue.isAuthenticated();
	}

	default boolean getAndPutIfAuthenticated(K key) {
		boolean authenticated = isAuthenticated(key);
		if (authenticated) {
			put(key, AuthCodeValue.ofAuthenticated());
		}
		return authenticated;
	}
}
