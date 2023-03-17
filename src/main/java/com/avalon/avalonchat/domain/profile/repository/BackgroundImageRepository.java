package com.avalon.avalonchat.domain.profile.repository;

import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackgroundImageRepository extends JpaRepository<BackgroundImage, Long> {
}
