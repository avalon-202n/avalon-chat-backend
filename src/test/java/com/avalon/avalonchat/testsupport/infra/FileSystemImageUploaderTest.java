package com.avalon.avalonchat.testsupport.infra;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.ClassPathResource;

import com.avalon.avalonchat.domain.profile.domain.Image;

class FileSystemImageUploaderTest {

	private final FileSystemImageUploader sut = new FileSystemImageUploader();

	@ParameterizedTest
	@CsvSource(value = {
		"test.png, PROFILE",
		"test.png, BACKGROUND",
		"test.svg, PROFILE",
		"test.svg, BACKGROUND"
	})
	void name(String fileName, Image.Type type) throws IOException {
		//given
		ClassPathResource testResource = new ClassPathResource("store/images/" + fileName);
		assertThat(testResource.exists()).isTrue();

		Image testImage = new Image(
			testResource.getFilename(),
			testResource.getInputStream(),
			type
		);

		//when
		String imagePath = sut.upload(testImage);

		//then
		File file = Paths.get(imagePath).toFile();
		assertThat(file.exists()).isTrue();

		//clean up
		file.delete();
	}
}
