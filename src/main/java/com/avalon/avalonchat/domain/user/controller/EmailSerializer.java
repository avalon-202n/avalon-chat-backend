package com.avalon.avalonchat.domain.user.controller;

import java.io.IOException;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class EmailSerializer extends JsonSerializer<Email> {

	@Override
	public void serialize(Email value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeString(value.getValue());
	}
}
