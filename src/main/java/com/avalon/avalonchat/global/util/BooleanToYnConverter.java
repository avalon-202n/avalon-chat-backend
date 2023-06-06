package com.avalon.avalonchat.global.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanToYnConverter implements AttributeConverter<Boolean, String> {

	public String convertToDatabaseColumn(Boolean attribute) {
		return (attribute != null && attribute) ? "Y" : "N";
	}

	public Boolean convertToEntityAttribute(String str) {
		return "Y".equals(str);
	}
}
