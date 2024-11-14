package com.example.commonmodule.common.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
public abstract class BaseEntity {

    private String createdBy;
    private ZonedDateTime createdAt;

    private String updatedBy;
    private ZonedDateTime updatedAt;
    private String deletedBy;
    private ZonedDateTime deletedAt;

    private boolean deleted;

    public void delete() {
        deleted = true;
    }

    @PrePersist
    public void prePersist() {
        createdAt = ZonedDateTime.now();
        createdBy = getBy();
    }

    @PreUpdate
    public void preUpdate() {
        if (deleted) {
            deletedAt = ZonedDateTime.now();
            deletedBy = getBy();
        } else {
            updatedAt = ZonedDateTime.now();
            updatedBy = getBy();
        }
    }

    private String getBy() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        } else {
            return "ANONYMOUS";
        }
    }

    @PreRemove
    public void preRemove() {
        System.out.println("======== preRemove");
    }

}
