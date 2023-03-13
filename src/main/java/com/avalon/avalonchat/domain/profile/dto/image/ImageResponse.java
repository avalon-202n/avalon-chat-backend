package com.avalon.avalonchat.domain.profile.dto.image;

import com.avalon.avalonchat.domain.profile.domain.ProfileImage;
import lombok.Getter;

@Getter
public class ImageResponse {
	private String filePath;

	public ImageResponse(ProfileImage image) {
		this.filePath = image.getFilePath();
	}

	public static ImageResponse ofEntity(ProfileImage image) {
		return new ImageResponse(image);
	}
}
