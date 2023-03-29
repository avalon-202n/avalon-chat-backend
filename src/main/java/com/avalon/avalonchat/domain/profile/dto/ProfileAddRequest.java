package com.avalon.avalonchat.domain.profile.dto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import com.avalon.avalonchat.domain.profile.domain.Image;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;

import lombok.Getter;

@Getter
public class ProfileAddRequest {
	private final LocalDate birthDate;
	private final String nickname;
	private final String bio;
	private Image image;
	private Image backgroundImage;

	public ProfileAddRequest(
		LocalDate birthDate,
		String nickname,
		String bio,
		MultipartFile image,
		MultipartFile backgroundImage) {
		this.birthDate = birthDate;
		this.nickname = nickname;
		this.bio = bio;
		setImage(image);
		setBackgroundImage(backgroundImage);
	}

	private void setImage(MultipartFile image) {
		InputStream inputStream = getInputStream(image);
		String originalFileName = getOriginalFileName(image);
		this.image = new Image(Image.Type.PROFILE, inputStream, originalFileName);
	}

	public void setBackgroundImage(MultipartFile backgroundImage) {
		InputStream inputStream = getInputStream(backgroundImage);
		String originalFileName = getOriginalFileName(backgroundImage);
		this.backgroundImage = new Image(Image.Type.BACKGROUND, inputStream, originalFileName);
	}

	private InputStream getInputStream(MultipartFile file) {
		ByteArrayInputStream byteArrayInputStream;
		try {
			byte[] byteArray = file.getBytes();
			byteArrayInputStream = new ByteArrayInputStream(byteArray);
		} catch (IOException e) {
			throw new AvalonChatRuntimeException("이미지 변환 중 예외 발생", e);
		}
		return byteArrayInputStream;
	}

	private String getOriginalFileName(MultipartFile file) {
		return file.getOriginalFilename();
	}

	public Profile toEntity(User user) {
		return new Profile(user, this.bio, this.birthDate, this.nickname);
	}
}
