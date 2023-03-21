package com.avalon.avalonchat.infra.image.upload;

import com.avalon.avalonchat.domain.profile.domain.Image;
import com.avalon.avalonchat.domain.profile.domain.ImageUploader;
import com.avalon.avalonchat.domain.profile.exception.ImageUploadException;
import com.avalon.avalonchat.global.configuration.GcpStorageProperties;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class GCPImageUploader implements ImageUploader {
	private final Storage storage;
	private final String BUCKET_NAME;
	private final String URL;

	public GCPImageUploader(Storage storage, GcpStorageProperties gcpStorageProperties) {
		this.storage = storage;
		this.BUCKET_NAME = gcpStorageProperties.getBucket();
		this.URL = gcpStorageProperties.getUrl();
	}

	@Override
	public String upload(Image image) {
		String fileName = UUID.randomUUID().toString();
		return fileWrite(image, fileName);
	}

	private String fileWrite(Image image, String fileName) {
		Path path = new File(fileName + "." + image.getExtension()).toPath();
		try {
			storage.createFrom(
				BlobInfo.newBuilder(BUCKET_NAME, fileName)
					.setContentType(Files.probeContentType(path))
					.build(),
				image.getInputStream()
			);
		} catch (IOException e) {
			throw new ImageUploadException("이미지 업로드 예외 발생", e);
		}
		return String.join("/", URL, BUCKET_NAME, fileName);
	}
}
