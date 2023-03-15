package com.avalon.avalonchat.infra.image.upload;

import com.avalon.avalonchat.domain.profile.domain.image.BackgroundImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackgroundImageRepository extends JpaRepository<BackgroundImage, Long> {
}
