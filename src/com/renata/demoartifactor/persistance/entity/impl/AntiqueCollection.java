package com.renata.demoartifactor.persistance.entity.impl;

import com.renata.demoartifactor.persistance.entity.Entity;
import com.renata.demoartifactor.persistance.entity.ErrorTemplates;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class AntiqueCollection extends Entity implements Comparable<AntiqueCollection> {

    public String name;
    public List<Item> items; // items listed in the collection
    public String description;
    public LocalDate createdDate;
    public User owner; // collection owner

    public AntiqueCollection(UUID id, String name, List<Item> items, String description,
        LocalDate createdDate,
        User owner) {
        super(id);
        this.name = name;
        this.items = items;
        this.description = description;
        this.createdDate = createdDate;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        final String templateName = "назви";

        if (name.isBlank()) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted(templateName));
        }

        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Item> getItems() {
        return items;
    }

    //add items to list

    @Override
    public int compareTo(AntiqueCollection o) {
        return this.name.compareTo(o.name);
    }
}
