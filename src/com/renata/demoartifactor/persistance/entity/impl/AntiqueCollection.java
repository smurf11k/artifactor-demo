package com.renata.demoartifactor.persistance.entity.impl;

import com.renata.demoartifactor.persistance.entity.Entity;
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

    public String getDescription() {
        return description;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public User getOwner() {
        return owner;
    }

    public List<Item> getItems() {
        return items;
    }

    // add setters

    @Override
    public int compareTo(AntiqueCollection o) {
        return this.name.compareTo(o.name);
    }
}
