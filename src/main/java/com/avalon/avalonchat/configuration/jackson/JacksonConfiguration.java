package com.avalon.avalonchat.configuration.jackson;

import java.io.IOException;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.Password;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Configuration(proxyBeanMethods = false)
public class JacksonConfiguration {

	private static JsonSerializer<Email> emailSerializer() {
		return new JsonSerializer<>() {
			@Override
			public void serialize(Email value, JsonGenerator gen, SerializerProvider serializers) throws
				IOException {
				gen.writeString(value.getValue());
			}
		};
	}

	private static JsonDeserializer<Email> emailDeserializer() {
		return new JsonDeserializer<>() {
			@Override
			public Email deserialize(JsonParser parser, DeserializationContext context) throws IOException {
				JsonNode node = parser.getCodec().readTree(parser);
				String value = node.asText();
				return Email.of(value);
			}
		};
	}

	private static JsonSerializer<Password> passwordSerializer() {
		return new JsonSerializer<>() {
			@Override
			public void serialize(Password value, JsonGenerator gen, SerializerProvider serializers) throws
				IOException {
				gen.writeString(value.getValue());
			}
		};
	}

	private static JsonDeserializer<Password> passwordDeserializer() {
		return new JsonDeserializer<>() {
			@Override
			public Password deserialize(JsonParser parser, DeserializationContext context) throws IOException {
				JsonNode node = parser.getCodec().readTree(parser);
				String value = node.asText();
				return Password.of(value);
			}
		};
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
		return builder -> builder
			.serializerByType(Email.class, emailSerializer())
			.deserializerByType(Email.class, emailDeserializer())
			.serializerByType(Password.class, passwordSerializer())
			.deserializerByType(Password.class, passwordDeserializer());
	}
}
