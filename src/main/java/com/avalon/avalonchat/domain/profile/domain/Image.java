package com.avalon.avalonchat.domain.profile.domain;

import static com.avalon.avalonchat.global.error.Preconditions.*;

import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;

import lombok.Getter;

@Getter
public class Image extends InputStreamResource {

	private final Type type;
	private String path;

	public Image(InputStream inputStream, Type imageType) {
		super(inputStream, imageType.name());
		this.type = imageType;
	}

	public void upload(ImageUploadService service) {
		this.path = service.upload(this);
	}

	public String getPath() {
		checkNotNull(path, "Image.getPath invoked before upload image");
		return path;
	}

	public enum Type {
		PROFILE,
		BACKGROUND
	}
}
