package com.renata.demoartifactor.persistance.entity.impl;

import com.renata.demoartifactor.persistance.entity.Entity;
import java.time.LocalDate;
import java.util.UUID;

public class Transaction extends Entity implements Comparable<Transaction> {

    public TransactionType type;
    public LocalDate date;
    public Item item;
    public User user;

    Transaction(UUID id, TransactionType type, LocalDate date, Item item, User user) {
        super(id);
        this.type = type;
        this.date = date;
        this.item = item;
        this.user = user;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //TODO add TransactionManager/TransactionService for logging all the transactions
    // TransactionHistory => save them to a file (.xls)
    // admin will have access to this file
    // see OOP PR4
    // private String timeFormat = "dd-MM-yyyy 'at' HH:mm";
    // see JsonRepositoryFactory

    @Override
    public int compareTo(Transaction o) {
        return this.date.compareTo(o.date);
    }

    public enum TransactionType {
        SELL("Продаж"),
        BUY("Купівля");

        private final String name;

        TransactionType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
