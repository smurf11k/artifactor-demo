package com.renata.demoartifactor.domain.dto;

import com.renata.demoartifactor.persistance.entity.Entity;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import java.time.LocalDate;
import java.util.UUID;

public final class ItemUpdateDto extends Entity {

    private final String name;
    private final AntiqueCollection collection;
    private final double value;
    private final String createdDate;
    private final LocalDate dateAquired;
    private final String description;

    public ItemUpdateDto(UUID id, String name, AntiqueCollection collection,
        double value, String createdDate, LocalDate dateAquired, String description) {
        super(id);
        this.name = name;
        this.collection = collection;
        this.value = value;
        this.createdDate = createdDate;
        this.dateAquired = dateAquired;
        this.description = description;
    }

    public String name() {
        return name;
    }

    public AntiqueCollection getCollection() {
        return collection;
    }

    public double value() {
        return value;
    }

    public String createdDate() {
        return createdDate;
    }

    public LocalDate dateAquired() {
        return dateAquired;
    }

    public String description() {
        return description;
    }

    public AntiqueCollection collection() {
        return collection;
    }

    public UUID id() {
        return super.getId();
    }
}
