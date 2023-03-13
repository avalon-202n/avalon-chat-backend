package com.avalon.avalonchat.domain.profile.dto.image;

import com.avalon.avalonchat.domain.profile.domain.Image;
import com.avalon.avalonchat.domain.profile.domain.ImageUploader;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;
import com.avalon.avalonchat.domain.profile.exception.ImageUploadException;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Getter
public class ImageRequest {
	private Image.Type imageType;
	private InputStream inputStream;
	private String originalFileName;

	public ImageRequest(Image.Type type, MultipartFile file) {
		this.imageType = type;
		this.inputStream = getInputStream(file);
		this.originalFileName = getOriginalFileName(file);
	}

	private InputStream getInputStream(MultipartFile file) {
		ByteArrayInputStream byteArrayInputStream;
		try {
			byte[] byteArray = file.getBytes();
			byteArrayInputStream = new ByteArrayInputStream(byteArray);
		} catch (IOException e) {
			throw new ImageUploadException("파일시스템 이미지 업로드 중 예외 발생", e);
		}
		return byteArrayInputStream;
	}

	private String getOriginalFileName(MultipartFile file) {
		return file.getOriginalFilename();
	}

	public ProfileImage toEntity(Profile profile, ImageUploader imageUploader) {
		String filePath = toImage().uploadBy(imageUploader);
		return new ProfileImage(profile, filePath);
	}

	private Image toImage() {
		return new Image(this.imageType, this.inputStream, this.originalFileName);
	}
}
