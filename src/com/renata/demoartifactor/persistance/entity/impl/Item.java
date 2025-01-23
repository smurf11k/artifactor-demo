package com.renata.demoartifactor.persistance.entity.impl;

import com.renata.demoartifactor.persistance.entity.Entity;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Item extends Entity implements Comparable<Item> {

    public String name;
    public Category category;
    public AntiqueCollection collection;
    public double value; // approximate price
    public String material; // wood, porcelain, metal
    public String origin; // Ireland, Rome, Egypt, Monaco
    public LocalDate createdDate; // year (approximate date)
    public LocalDate dateAquired;
    public String description;
    public List<Tag> tags;

    public Item(UUID id, String name, Category category, AntiqueCollection collection,
        double value, String material, String origin,
        LocalDate createdDate, LocalDate dateAquired, String description, List<Tag> tags) {
        super(id);
        this.name = name;
        this.category = category;
        this.collection = collection;
        this.value = value;
        this.material = material;
        this.origin = origin;
        this.createdDate = createdDate;
        this.dateAquired = dateAquired;
        this.description = description;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public String getMaterial() {
        return material;
    }

    public String getOrigin() {
        return origin;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public LocalDate getDateAquired() {
        return dateAquired;
    }

    public String getDescription() {
        return description;
    }

    public List<Tag> getTags() {
        return tags;
    }

    // add setters

    @Override
    public int compareTo(Item o) {
        return this.name.compareTo(o.name);
    }
}
