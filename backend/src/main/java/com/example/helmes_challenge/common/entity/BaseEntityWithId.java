package com.example.helmes_challenge.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
public abstract class BaseEntityWithId extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;
}
