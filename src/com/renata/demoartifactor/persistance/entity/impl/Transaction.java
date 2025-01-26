package com.renata.demoartifactor.persistance.entity.impl;

import com.renata.demoartifactor.persistance.entity.Entity;
import java.time.LocalDate;
import java.util.UUID;

public class Transaction extends Entity implements Comparable<Transaction> {

    public String type; //купівля, продаж, обмін - make an enum
    public LocalDate date;
    public Item item; // item used in transaction
    public double amount;
    public User user; //user who made the transaction

    Transaction(UUID id, String type, LocalDate date, Item item, double amount, User user) {
        super(id);
        this.type = type;
        this.date = date;
        this.item = item;
        this.amount = amount;
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // add TransactionManager/TransactionService for logging all the transactions
    // TransactionHistory => save them to a file (.xls)
    // admin will have access to this file
    // see OOP PR4
    // private String timeFormat = "dd-MM-yyyy 'at' HH:mm:ss";

    @Override
    public int compareTo(Transaction o) {
        return this.date.compareTo(o.date);
    }
}
