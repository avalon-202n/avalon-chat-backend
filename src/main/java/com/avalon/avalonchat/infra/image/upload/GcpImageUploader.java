package com.avalon.avalonchat.infra.image.upload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.avalon.avalonchat.domain.profile.domain.Image;
import com.avalon.avalonchat.domain.profile.domain.ImageUploader;
import com.avalon.avalonchat.domain.profile.exception.ImageUploadException;
import com.avalon.avalonchat.global.configuration.GcpStorageProperties;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

@Service
public class GcpImageUploader implements ImageUploader {
	private final Storage storage;
	private final String bucketName;
	private final String url;

	public GcpImageUploader(Storage storage, GcpStorageProperties gcpStorageProperties) {
		this.storage = storage;
		this.bucketName = gcpStorageProperties.getBucket();
		this.url = gcpStorageProperties.getUrl();
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
				BlobInfo.newBuilder(bucketName, fileName)
					.setContentType(Files.probeContentType(path))
					.build(),
				image.getInputStream()
			);
		} catch (IOException e) {
			throw new ImageUploadException("이미지 업로드 예외 발생", e);
		}
		return String.join("/", url, bucketName, fileName);
	}
}
