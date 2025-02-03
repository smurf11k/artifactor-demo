package com.renata.demoartifactor.persistance.entity.impl;

import com.renata.demoartifactor.persistance.entity.Entity;
import com.renata.demoartifactor.persistance.entity.ErrorTemplates;
import com.renata.demoartifactor.persistance.exception.EntityArgumentException;
import java.time.LocalDate;
import java.util.UUID;

public class Item extends Entity implements Comparable<Item> {

    public String name;
    public ItemType itemType;
    public AntiqueCollection collection;
    public double value;
    public String createdDate;
    public LocalDate dateAquired;
    public String description;

    public Item(UUID id, String name, ItemType itemType, AntiqueCollection collection,
        double value, String createdDate, LocalDate dateAquired, String description) {
        super(id);
        setName(name);
        this.itemType = itemType;
        this.collection = collection;
        this.value = value;
        setCreatedDate(createdDate);
        setDateAquired(dateAquired);
        this.description = description;

        if (!this.isValid()) {
            throw new EntityArgumentException(errors);
        }
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

    public ItemType getItemType() {
        return itemType;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getDateAquired() {
        return dateAquired;
    }

    public void setDateAquired(LocalDate dateAquired) {
        final String templateName = "дати";

        if (name.isBlank()) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted(templateName));
        }

        if (dateAquired.isAfter(LocalDate.now())) {
            errors.add(ErrorTemplates.DATE.getTemplate().formatted(templateName));
        }
        this.dateAquired = dateAquired;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(Item o) {
        return this.name.compareTo(o.name);
    }

    public enum ItemType {
        FURNITURE("Меблі"),
        JEWELRY("Ювелірні вироби"),
        CLASSIC_CARS("Класичні автомобілі"),
        TIMEPIECES("Годинники"),
        ART("Картини/Фото"),
        CERAMICS_AND_PORCELAIN("Кераміка та порцеляна"),
        TEXTILES("Текстиль"),
        MILITARY_MEMORABILIA("Військові пам'ятні речі"),
        COINS_AND_SILVER("Монети та срібло"),
        BOOKS_AND_MANUSCRIPTS("Книги та рукописи");

        private final String name;

        ItemType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
