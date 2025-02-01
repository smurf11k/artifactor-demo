package com.renata.demoartifactor.domain.dto;

import com.renata.demoartifactor.persistance.entity.Entity;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.Item.ItemType;
import java.time.LocalDate;
import java.util.UUID;

public final class ItemAddDto extends Entity {

    public ItemType itemType;
    public String name;
    public AntiqueCollection collection;
    public double value;
    public String createdDate;
    public LocalDate dateAquired;
    public String description;

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
    }

    public String getName() {
        return name;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public AntiqueCollection getCollection() {
        return collection;
    }

    public double getValue() {
        return value;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public LocalDate getDateAquired() {
        return dateAquired;
    }

    public String getDescription() {
        return description;
    }
}
