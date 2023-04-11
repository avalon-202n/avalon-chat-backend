package com.avalon.avalonchat.domain.user.controller;

import java.io.IOException;

import com.avalon.avalonchat.domain.user.domain.Password;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class PasswordSerializer extends JsonSerializer<Password> {

	@Override
	public void serialize(Password value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeString(value.getValue());
	}
}
