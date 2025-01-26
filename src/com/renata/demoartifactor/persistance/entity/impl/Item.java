package com.renata.demoartifactor.persistance.entity.impl;

import com.renata.demoartifactor.persistance.entity.Entity;
import com.renata.demoartifactor.persistance.entity.ErrorTemplates;
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
        this.category = category; // choose/enter, maybe aad an enum
        // and a menu option for categories where the user could add new categories
        // and then just choose from them as the collections
        this.collection = collection; // choose
        this.value = value;
        this.material = material;
        this.origin = origin;
        this.createdDate = createdDate;
        this.dateAquired = dateAquired; // maybe skip
        this.description = description;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    // is this validation needed?
    public void setName(String name) {
        final String templateName = "назви";

        if (name.isBlank()) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted(templateName));
        }

        this.name = name;
    }

    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }

    public AntiqueCollection getCollection() {
        return collection;
    }

    public void setCollection(AntiqueCollection collection) {
        this.collection = collection;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getDateAquired() {
        return dateAquired;
    }

    public void setDateAquired(LocalDate dateAquired) {
        this.dateAquired = dateAquired;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public int compareTo(Item o) {
        return this.name.compareTo(o.name);
    }
}
