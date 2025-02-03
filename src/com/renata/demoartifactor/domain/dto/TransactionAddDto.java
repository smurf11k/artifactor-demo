package com.renata.demoartifactor.domain.dto;

import com.renata.demoartifactor.persistance.entity.Entity;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.entity.impl.Transaction.TransactionType;
import com.renata.demoartifactor.persistance.entity.impl.User;
import java.time.LocalDate;
import java.util.UUID;

public final class TransactionAddDto extends Entity {

    private final TransactionType type;
    private final LocalDate date;
    private final Item item;
    private final User user;

    public TransactionAddDto(UUID id, TransactionType type, LocalDate date, Item item, User user) {
        super(id);
        this.type = type;
        this.date = date;
        this.item = item;
        this.user = user;
    }

    public TransactionType type() {
        return type;
    }

    public LocalDate date() {
        return date;
    }

    public Item item() {
        return item;
    }

    public User user() {
        return user;
    }
}
