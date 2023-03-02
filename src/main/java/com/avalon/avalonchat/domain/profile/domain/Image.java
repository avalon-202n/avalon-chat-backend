package com.avalon.avalonchat.domain.profile.domain;

import static com.avalon.avalonchat.global.error.Preconditions.*;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamResource;

import lombok.Getter;

@Getter
public class Image extends InputStreamResource {

	private static final List<String> ACCEPTED_FILE_EXTENSIONS
		= List.of("png", "webp", "jpg", "jpeg", "gif", "bmp", "svg");

	private final Type type;
	private String path;
	private String extension;

	public Image(String originalFileName, InputStream inputStream, Type imageType) {
		super(inputStream, "[" + imageType.name() + "] " + originalFileName);
		this.type = imageType;
		this.extension = checkAndGetExtension(originalFileName);
	}

	private String checkAndGetExtension(String filename) {
		String extension = StringUtils.substringAfterLast(filename, ".");
		checkContains(extension, ACCEPTED_FILE_EXTENSIONS, "illegal file extension provided for image");
		return extension;
	}

	public void uploadBy(ImageUploader service) {
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
