package com.avalon.avalonchat.domain.profile.dto;

import com.avalon.avalonchat.domain.profile.domain.Image;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.dto.image.ImageRequest;
import com.avalon.avalonchat.domain.user.domain.User;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
public class ProfileRequest {
	private LocalDate birthDate;
	private String nickname;
	private String bio;
	private ImageRequest image;
	private ImageRequest backgroundImage;

	public ProfileRequest(
		LocalDate birthDate,
		String nickname,
		String bio,
		MultipartFile image,
		MultipartFile backgroudImage) {
		this.birthDate = birthDate;
		this.nickname = nickname;
		this.bio = bio;
		setImage(image);
		setBackgroundImage(backgroudImage);
	}

	private void setImage(MultipartFile image) {
		ImageRequest imageRequest = new ImageRequest(Image.Type.PROFILE, image);
		this.image = imageRequest;
	}

	public void setBackgroundImage(MultipartFile backgroundImage) {
		ImageRequest backgroundImageRequest = new ImageRequest(Image.Type.BACKGROUND, backgroundImage);
		this.backgroundImage = backgroundImageRequest;
	}

	public Profile toEntity(User user) {
		return new Profile(user, this.bio, this.birthDate, this.nickname);
	}
}
