package com.avalon.avalonchat.domain.user.controller;

import java.io.IOException;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class EmailDeserializer extends JsonDeserializer<Email> {

	@Override
	public Email deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		JsonNode node = parser.getCodec().readTree(parser);
		String value = node.asText();
		return Email.of(value);
	}
}
