package com.renata.demoartifactor.persistance.entity.impl;

import com.renata.demoartifactor.persistance.entity.Entity;
import com.renata.demoartifactor.persistance.entity.ErrorTemplates;
import java.time.LocalDate;
import java.util.UUID;

public class AntiqueCollection extends Entity implements Comparable<AntiqueCollection> {

    public String name;
    public String description;
    public LocalDate createdDate;
    public User owner;

    public AntiqueCollection(UUID id, String name, String description,
        LocalDate createdDate,
        User owner) {
        super(id);
        this.name = name;
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

    public User getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "AntiqueCollection{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", createdDate=" + createdDate +
            ", owner=" + (owner != null ? owner.toString() : "No owner") +
            '}';
    }


    @Override
    public int compareTo(AntiqueCollection o) {
        return this.name.compareTo(o.name);
    }
}
