package com.avalon.avalonchat.domain.profile.domain;

import static com.avalon.avalonchat.global.error.Preconditions.*;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;

/**
 * 프로필 도메인에서 활용하는 이미지
 * 프로필 이미지/ 배경 이미지 두 종류가 있다.
 */
@Getter
public class Image {

	/* 허용되는 파일 확장자 목록 */
	private static final List<String> ACCEPTED_FILE_EXTENSIONS
		= List.of("png", "webp", "jpg", "jpeg", "gif", "bmp", "svg");

	private final Type type;
	private final InputStream inputStream;
	private final String extension;

	public Image(Type imageType, InputStream inputStream, String originalFileName) {
		this.type = imageType;
		this.inputStream = inputStream;
		this.extension = checkAndGetExtension(originalFileName);
	}

	/* 파일명으로 부터 확장자 얻기 및 검증 */
	private String checkAndGetExtension(String filename) {
		String extension = StringUtils.substringAfterLast(filename, ".");
		checkContains(extension, ACCEPTED_FILE_EXTENSIONS, "illegal file extension provided for image");
		return extension;
	}

	/* 도메인 서비스 ImageUploader 를 활용해 이미지 업로드 수행 */
	public void uploadBy(ImageUploader service) {
		service.upload(this);
	}

	/* 이미지의 종류 */
	public enum Type {
		PROFILE,
		BACKGROUND
	}
}
