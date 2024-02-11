package com.vitraya.parseuploadimage.repositories;

import com.vitraya.parseuploadimage.dto.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageData, Long> {
}
