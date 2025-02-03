package com.renata.demoartifactor.domain.dto;

import com.renata.demoartifactor.persistance.entity.Entity;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.entity.impl.Transaction.TransactionType;
import com.renata.demoartifactor.persistance.entity.impl.User;
import java.time.LocalDateTime;
import java.util.UUID;

public final class TransactionAddDto extends Entity {

    private final TransactionType type;
    private final LocalDateTime date;
    private final Item item;
    private final double value;
    private final User user;

    public TransactionAddDto(UUID id, TransactionType type, LocalDateTime date, Item item,
        double value,
        User user) {
        super(id);
        this.type = type;
        this.date = date;
        this.item = item;
        this.value = value;
        this.user = user;
    }

    public TransactionType type() {
        return type;
    }

    public LocalDateTime date() {
        return date;
    }

    public Item item() {
        return item;
    }

    public double value() {
        return value;
    }

    public User user() {
        return user;
    }
}
