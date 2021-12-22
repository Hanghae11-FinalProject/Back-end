package com.team11.backend.repository;

import com.team11.backend.model.Image;
import com.team11.backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
