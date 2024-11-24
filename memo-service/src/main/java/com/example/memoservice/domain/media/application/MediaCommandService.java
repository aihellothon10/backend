package com.example.memoservice.domain.media.application;

import com.example.memoservice.domain.media.domain.entity.MediaFile;
import com.example.memoservice.domain.media.domain.repository.MediaFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaCommandService {

    private final MediaFileRepository mediaFileRepository;
    private final FileStorageService fileStorageService;


    @Transactional
    public String saveFile(MultipartFile file) {
        String savedPath = storeFile(file);
        MediaFile mediaFile = createMediaFile(file, savedPath);
        MediaFile savedMediaFile = mediaFileRepository.save(mediaFile);
        return savedMediaFile.getUuid();
    }


    @Transactional
    public String saveFileWithName(MultipartFile file, String name) {
        String savedPath = storeFile(file);
        MediaFile mediaFile = createMediaFileWithName(file, savedPath, name);
        MediaFile savedMediaFile = mediaFileRepository.save(mediaFile);
        return savedMediaFile.getUuid();
    }

    private String storeFile(MultipartFile file) {
        try {
            return fileStorageService.storeFile(file);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다.", e);
        }
    }

    private MediaFile createMediaFile(MultipartFile file, String savedPath) {
        return MediaFile.builder()
                .uuid(UUID.randomUUID().toString())
                .fileName(file.getOriginalFilename())
                .savedPath(savedPath)
                .build();
    }

    private MediaFile createMediaFileWithName(MultipartFile file, String savedPath, String name) {
        return MediaFile.builder()
                .uuid(name)
                .fileName(file.getOriginalFilename())
                .savedPath(savedPath)
                .build();
    }

}
