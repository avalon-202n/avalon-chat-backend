package com.avalon.avalonchat.testsupport.infra;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import com.avalon.avalonchat.domain.profile.domain.Image;
import com.avalon.avalonchat.domain.profile.domain.ImageUploader;

public class FileSystemImageUploader implements ImageUploader {

	final static String IMAGE_STORE_PATH = "src/test/resources/store/images";

	private static String fileWrite(Image image, String fileName) {
		String imagePath = String.join("/", IMAGE_STORE_PATH, image.getType().name().toLowerCase(), fileName);
		try {
			Path path = Paths.get(imagePath);
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path.toString(), false));
			bos.write(image.getInputStream().readAllBytes());
			bos.flush();
			bos.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return imagePath;
	}

	@Override
	public String upload(Image image) {
		String imageName = UUID.randomUUID().toString();
		String extension = image.getExtension();

		return fileWrite(image, imageName + "." + extension);
	}
}
