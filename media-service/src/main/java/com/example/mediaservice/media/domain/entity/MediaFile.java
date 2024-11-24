package com.example.mediaservice.media.domain.entity;

import com.example.commonmodule.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MediaFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

    private String fileName;

    private String savedPath;

    @Builder
    public MediaFile(String uuid, String fileName, String savedPath) {
        this.uuid = uuid;
        this.fileName = fileName;
        this.savedPath = savedPath;
    }

}
