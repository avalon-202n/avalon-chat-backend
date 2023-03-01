package com.avalon.avalonchat.domain.profile.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.avalon.avalonchat.testsupport.Fixture;

class ImageTest {

	@DisplayName("Image 생성 성공")
	@Test
	void name() throws IOException {
		//given
		byte[] byteArray = "test-image-byte".getBytes();
		InputStream inputStream = new ByteArrayInputStream(byteArray);
		Image.Type type = Image.Type.PROFILE;

		//when
		Image image = new Image(inputStream, type);

		//then
		assertThat(image.getType()).isEqualTo(type);
		assertThat(image.getInputStream().readAllBytes()).contains(byteArray);
		assertThatNullPointerException().isThrownBy(image::getPath);
	}

	@DisplayName("Image.upload 호출 한 뒤에는 경로를 조회할 수 있다.")
	@Test
	void name2() {
		//given
		Image image = Fixture.createProfileImage();
		assertThatNullPointerException().isThrownBy(image::getPath);

		String expectedPath = "path/to/image";
		ImageUploadService imageUploadService = mockImageUploadService(image, expectedPath);

		//when
		image.upload(imageUploadService);

		//then
		assertThat(image.getPath()).isEqualTo(expectedPath);
	}

	private static ImageUploadService mockImageUploadService(Image image, final String expectedPath1) {
		ImageUploadService imageUploadService = mock(ImageUploadService.class);
		when(imageUploadService.upload(image)).thenReturn(expectedPath1);
		return imageUploadService;
	}
}
