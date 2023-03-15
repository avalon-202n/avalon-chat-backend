package com.avalon.avalonchat.infra.image.upload;

import com.avalon.avalonchat.domain.profile.domain.image.Image;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageUploaderImpl implements ImageUploader {

	private static final String URL_PREFIX = "https://storage.googleapis.com";
	private final Storage storage;
	@Value("${spring.cloud.gcp.storage.bucket}")
	private String BUCKET_NAME;

	@Override
	public String upload(Image image) {
		String fileName = UUID.randomUUID().toString();
		return fileWrite(image, fileName);
	}

	private String fileWrite(Image image, String fileName) {
		storage.create(
			BlobInfo.newBuilder(BUCKET_NAME, fileName)
				.setContentType(image.getExtension())
				.build(),
			image.getInputStream()
		);
		String imageUrl = String.join("/", URL_PREFIX, BUCKET_NAME, fileName);
		return imageUrl;
	}
}
