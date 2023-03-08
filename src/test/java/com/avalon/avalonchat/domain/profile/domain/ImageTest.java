package com.avalon.avalonchat.domain.profile.domain;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ImageTest {

	@Test
	void Image_생성_성공() throws IOException {
		//given
		byte[] byteArray = "test-image-byte".getBytes();
		String originalFileName = "profile.png";
		InputStream inputStream = new ByteArrayInputStream(byteArray);
		Image.Type type = Image.Type.PROFILE;

		//when
		Image image = new Image(type, inputStream, originalFileName);

		//then
		assertThat(image.getType()).isEqualTo(type);
		assertThat(image.getInputStream().readAllBytes()).contains(byteArray);
	}

	@ParameterizedTest
	@ValueSource(strings = {"illegal_file_name.pdf", "file_with_no_extension", ".gitignore"})
	void 허용되지_않는_확장자를_가진_Image_생성실패(String illegalFileNames) {
		//given
		InputStream inputStream = new ByteArrayInputStream("test-image-byte".getBytes());
		Image.Type type = Image.Type.PROFILE;

		//when then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> new Image(type, inputStream, illegalFileNames));
	}
}
