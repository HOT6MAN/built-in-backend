package com.example.hotsix.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@SuperBuilder
@NoArgsConstructor
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Column(nullable = false, name = "DEL_YN")
    private Boolean deleted;

    @CreatedDate
    @Column(updatable = false, nullable = false, name = "REG_DTTM")
    private LocalDateTime createdDate;

    @Column(nullable = false, name = "REG_USER_SEQ")
    private Integer regUserSeq;

    @LastModifiedDate
    @Column(nullable = false, name = "MOD_DTTM")
    private LocalDateTime modifiedDate;

    @Column(nullable = false, name = "MOD_USER_SEQ")
    private Integer modUserSeq;

    @PrePersist
    public void prePersist() {
        this.deleted = false;
        this.regUserSeq = 1;
        this.modUserSeq = 1;
    }
}
