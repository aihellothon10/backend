package com.example.memoservice.domain.media.domain.repository;

import com.example.memoservice.domain.media.domain.entity.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {

    Optional<MediaFile> findByUuid(String uuid);

}
