package com.avalon.avalonchat.domain.profile.service;

import com.avalon.avalonchat.domain.profile.domain.ImageUploader;
import com.avalon.avalonchat.domain.profile.dto.ProfileRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileResponse;
import com.avalon.avalonchat.domain.profile.repository.ProfileImageRespository;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;
import com.avalon.avalonchat.testsupport.DtoFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

@DataJpaTest
class ProfileServiceImplTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
	private ProfileImageRespository profileImageRespository;
	private ProfileServiceImpl profileService;
	private ImageUploader imageUploader;

	@BeforeEach
	void setUp() {
		imageUploader = mock(ImageUploader.class);
		profileService = new ProfileServiceImpl(profileRepository, profileImageRespository, userRepository, imageUploader);
	}

	@DisplayName("프로필 등록 성공")
	@Test
	@Transactional
	void addProfile() throws IOException {
		//given
		LocalDate birthDate = LocalDate.now();
		String nickname = "nickname";
		String bio = "bio";

		String imageFileName = "test";
		String imageContentType = "png";
		String imageFilePath = "src/test/resources/store/images/" + imageFileName + "." + imageContentType;
		MockMultipartFile image = getMultipartFile(imageFileName, imageContentType, imageFilePath);

		String backgroundImageFileName = "test";
		String backgroundImageContentType = "png";
		String backgroundImageFilePath = "src/test/resources/store/images/" + backgroundImageFileName + "." + backgroundImageContentType;
		MockMultipartFile backgroundImage
			= getMultipartFile(backgroundImageFileName, backgroundImageContentType, backgroundImageFilePath);

		ProfileRequest profileRequest = DtoFixture.profileRequest(
			birthDate,
			nickname,
			bio,
			image,
			backgroundImage
		);

		User user = new User(
			Email.of("avalon@e.com"),
			"passW0rd"
		);
		User savedUser = userRepository.save(user);
		Long userId = savedUser.getId();

		//when
		ResponseEntity<ProfileResponse> responseEntity = profileService.addProfile(userId, profileRequest);

		//then
		assertThat(responseEntity.getStatusCode().value()).isEqualTo(201);

		ProfileResponse profileResponse = responseEntity.getBody();
		assertThat(profileResponse.getBirthDate()).isEqualTo(birthDate);
		assertThat(profileResponse.getNickname()).isEqualTo(nickname);
		assertThat(profileResponse.getBio()).isEqualTo(bio);
		//TODO 프로필 이미지/배경이미지 생성경로 확인 테스트 코드
//		assertThat(profileResponse.getImagePath()).isNotNull();
//		assertThat(profileResponse.getBackgroundImagePath()).isNotNull();
	}

	private MockMultipartFile getMultipartFile(String fileName, String contentType, String filePath) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(new File(filePath));
		return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
	}
}
