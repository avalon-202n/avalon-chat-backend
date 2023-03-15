package com.avalon.avalonchat.infra.image.upload;

import com.avalon.avalonchat.domain.profile.domain.image.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
}
