package com.avalon.avalonchat.domain.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;

public interface BackgroundImageRepository extends JpaRepository<BackgroundImage, Long> {
}
