package com.limhs.movie_project.domain.mappedSuperClass;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter
public abstract class BaseTimeEntity extends BaseEntity{
    private LocalDateTime createdTime;
}
