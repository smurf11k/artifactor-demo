package com.renata.demoartifactor.domain.dto;

import com.renata.demoartifactor.persistance.entity.Entity;
import com.renata.demoartifactor.persistance.entity.impl.User;
import java.time.LocalDate;
import java.util.UUID;

public final class AntiqueCollectionAddDto extends Entity {

    private final String name;
    private final String description;
    private final LocalDate createdDate;
    private final User owner;

    public AntiqueCollectionAddDto(UUID id, String name, String description,
        LocalDate createdDate, User owner) {
        super(id);
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
        this.owner = owner;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public LocalDate createdDate() {
        return createdDate;
    }

    public User owner() {
        return owner;
    }
}
