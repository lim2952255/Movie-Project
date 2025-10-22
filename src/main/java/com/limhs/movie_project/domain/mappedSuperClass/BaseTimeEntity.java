package com.limhs.movie_project.domain.mappedSuperClass;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
public abstract class BaseTimeEntity extends BaseEntity{
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdTime;

    @CreatedDate
    private LocalDateTime updatedTime;

}
