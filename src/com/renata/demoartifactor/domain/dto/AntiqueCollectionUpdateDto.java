package com.renata.demoartifactor.domain.dto;

import com.renata.demoartifactor.persistance.entity.Entity;
import java.util.UUID;

public final class AntiqueCollectionUpdateDto extends Entity {

    private final String name;
    private final String description;

    public AntiqueCollectionUpdateDto(UUID id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public UUID id() {
        return super.getId();
    }
}

