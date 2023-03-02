package com.avalon.avalonchat.domain.profile.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.avalon.avalonchat.testsupport.Fixture;

class ImageTest {

	private static ImageUploader mockImageUploadService(Image image, final String expectedPath1) {
		ImageUploader imageUploader = mock(ImageUploader.class);
		when(imageUploader.upload(image)).thenReturn(expectedPath1);
		return imageUploader;
	}

	@DisplayName("Image 생성 성공")
	@Test
	void name() throws IOException {
		//given
		byte[] byteArray = "test-image-byte".getBytes();
		String originalFileName = "profile.png";
		InputStream inputStream = new ByteArrayInputStream(byteArray);
		Image.Type type = Image.Type.PROFILE;

		//when
		Image image = new Image(originalFileName, inputStream, type);

		//then
		assertThat(image.getType()).isEqualTo(type);
		assertThat(image.getInputStream().readAllBytes()).contains(byteArray);
		assertThatNullPointerException().isThrownBy(image::getPath);
	}

	@DisplayName("허용되지 않는 확장자를 가진 Image 생성 실패")
	@ParameterizedTest
	@ValueSource(strings = {"illegal_file_name.pdf", "file_with_no_extension", ".gitignore"})
	void name2(String illegalFileNames) {
		//given
		InputStream inputStream = new ByteArrayInputStream("test-image-byte".getBytes());
		Image.Type type = Image.Type.PROFILE;

		//when then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> new Image(illegalFileNames, inputStream, type));
	}

	@DisplayName("Image.upload 호출 한 뒤에는 경로를 조회할 수 있다.")
	@Test
	void name3() {
		//given
		Image image = Fixture.createProfileImage();
		assertThatNullPointerException().isThrownBy(image::getPath);

		String expectedPath = "path/to/image";
		ImageUploader imageUploader = mockImageUploadService(image, expectedPath);

		//when
		image.uploadBy(imageUploader);

		//then
		assertThat(image.getPath()).isEqualTo(expectedPath);
	}
}
