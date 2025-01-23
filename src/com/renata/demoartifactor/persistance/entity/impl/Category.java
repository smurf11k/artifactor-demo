package com.renata.demoartifactor.persistance.entity.impl;

import com.renata.demoartifactor.persistance.entity.Entity;
import java.util.UUID;

public class Category extends Entity implements Comparable<Category> {

    public String name; // coin, vase, plate, teapot
    public String description;

    public Category(UUID id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    // add validation
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(Category o) {
        return this.name.compareTo(o.name);
    }
}
