package com.avalon.avalonchat.domain.profile.domain;

public interface ImageUploader {

	/**
	 * @param image
	 * @return uploaded image path
	 * @throws com.avalon.avalonchat.domain.profile.exception.ImageUploadException
	 */
	String upload(Image image);
}
