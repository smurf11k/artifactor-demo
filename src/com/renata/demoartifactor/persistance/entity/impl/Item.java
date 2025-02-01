package com.renata.demoartifactor.persistance.entity.impl;

import com.renata.demoartifactor.persistance.entity.Entity;
import com.renata.demoartifactor.persistance.entity.ErrorTemplates;
import java.time.LocalDate;
import java.util.UUID;

public class Item extends Entity implements Comparable<Item> {

    public String name;
    public ItemType itemType;
    public AntiqueCollection collection;
    public double value; // approximate price
    public String createdDate; // year (approximate date)
    public LocalDate dateAquired;
    public String description;

    public Item(UUID id, String name, ItemType itemType, AntiqueCollection collection,
        double value, String createdDate, LocalDate dateAquired, String description) {
        super(id);
        this.name = name;
        this.itemType = itemType; // user chooses from existing categories
        this.collection = collection; // choose from existing collections of the authorized user
        this.value = value;
        this.createdDate = createdDate;
        this.dateAquired = dateAquired; // maybe skip, because the transaction contains this data
        // (however only the admin can see the transactions)
        this.description = description;
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
        this.dateAquired = dateAquired;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Item{" +
            "collection='" + collection + '\'' +
            ", type='" + itemType + '\'' +
            ", value='" + value + '\'' +
            ", createdDate='" + createdDate + '\'' +
            ", dateAquired='" + dateAquired + '\'' +
            ", description='" + description + '\'' +
            ", id=" + id +
            '}';
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
