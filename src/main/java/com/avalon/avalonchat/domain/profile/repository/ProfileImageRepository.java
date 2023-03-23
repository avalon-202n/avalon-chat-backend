package com.avalon.avalonchat.domain.profile.repository;

import com.avalon.avalonchat.domain.profile.domain.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
}
