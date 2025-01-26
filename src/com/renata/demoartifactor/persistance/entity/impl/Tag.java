package com.renata.demoartifactor.persistance.entity.impl;

import com.renata.demoartifactor.persistance.entity.Entity;
import java.util.UUID;

public class Tag extends Entity implements Comparable<Tag> {

    public String name;
    public String slug;

    public Tag(UUID id, String name, String slug) {
        super(id);
        this.name = name;
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    // add validation maybe?
    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public int compareTo(Tag o) {
        return this.name.compareTo(o.name);
    }
}
