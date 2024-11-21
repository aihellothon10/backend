package com.example.mediaservice.media.application;

import com.example.mediaservice.media.domain.entity.MediaFile;
import com.example.mediaservice.media.domain.repository.MediaFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MediaQueryService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    private final MediaFileRepository mediaFileRepository;


    public Resource getResource(String uuid) {

        MediaFile mediaFile = mediaFileRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("File Not Found"));

        Path filePath = Paths.get(mediaFile.getSavedPath()).normalize();
        try {
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }
}
