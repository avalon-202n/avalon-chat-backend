package com.avalon.avalonchat.domain.user.controller;

import java.io.IOException;

import com.avalon.avalonchat.domain.user.domain.Password;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class PasswordDeserializer extends JsonDeserializer<Password> {

	@Override
	public Password deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		JsonNode node = parser.getCodec().readTree(parser);
		String value = node.asText();
		return Password.of(value);
	}
}
