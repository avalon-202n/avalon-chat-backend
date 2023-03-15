package com.avalon.avalonchat.infra.image.upload;

import com.avalon.avalonchat.domain.profile.domain.image.Image;

public interface ImageUploader {

	/**
	 * @param image
	 * @return uploaded image path
	 * @throws com.avalon.avalonchat.domain.profile.exception.ImageUploadException
	 */
	String upload(Image image);
}
