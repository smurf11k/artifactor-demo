package com.renata.demoartifactor.domain.dto;

import com.renata.demoartifactor.persistance.entity.Entity;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.Item.ItemType;
import com.renata.demoartifactor.persistance.exception.EntityArgumentException;
import java.time.LocalDate;
import java.util.UUID;

public final class ItemAddDto extends Entity {

    private final String name;
    private final ItemType itemType;
    private final AntiqueCollection collection;
    private final double value;
    private final String createdDate;
    private final LocalDate dateAquired;
    private final String description;

    public ItemAddDto(UUID id, String name, ItemType itemType, AntiqueCollection collection,
        double value, String createdDate, LocalDate dateAquired, String description) {
        super(id);
        this.name = name;
        this.itemType = itemType;
        this.collection = collection;
        this.value = value;
        this.createdDate = createdDate;
        this.dateAquired = dateAquired;
        this.description = description;

        if (!this.isValid()) {
            throw new EntityArgumentException(errors);
        }
    }

    public String name() {
        return name;
    }

    public ItemType itemType() {
        return itemType;
    }

    public AntiqueCollection antiqueCollection() {
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
}
