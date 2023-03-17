package com.avalon.avalonchat.infra.image.upload;

import com.avalon.avalonchat.domain.profile.domain.Image;
import com.avalon.avalonchat.domain.profile.domain.ImageUploader;
import com.avalon.avalonchat.domain.profile.exception.ImageUploadException;
import com.avalon.avalonchat.global.configuration.GcpStorageProperties;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageUploaderImpl implements ImageUploader {
	private final Storage storage;
	private final GcpStorageProperties gcpStorageProperties;

	@Override
	public String upload(Image image) {
		String fileName = UUID.randomUUID().toString();
		return fileWrite(image, fileName);
	}

	private String fileWrite(Image image, String fileName) {
		Path path = new File(fileName + "." + image.getExtension()).toPath();
		try {
			storage.createFrom(
				BlobInfo.newBuilder(gcpStorageProperties.getBucket(), fileName)
					.setContentType(Files.probeContentType(path))
					.build(),
				image.getInputStream()
			);
		} catch (IOException e) {
			throw new ImageUploadException("이미지 업로드 예외 발생", e);
		}
		return String.join("/", gcpStorageProperties.getUrl(), gcpStorageProperties.getBucket(), fileName);
	}
}
