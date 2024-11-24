package com.example.mediaservice.media.representation;

import com.example.mediaservice.media.application.MediaCommandService;
import com.example.mediaservice.media.application.MediaQueryService;
import com.example.mediaservice.media.domain.entity.MediaFile;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/media")
public class MediaController {

    private final MediaCommandService mediaCommandService;
    private final MediaQueryService mediaQueryService;


    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        MediaFile mediaFile = mediaCommandService.saveFile(file);
        return mediaFile.getUuid();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable String id) {
        Resource resource = mediaQueryService.getResource(id);

        // 파일 반환
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
