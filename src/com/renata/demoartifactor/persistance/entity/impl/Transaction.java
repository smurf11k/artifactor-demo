package com.renata.demoartifactor.persistance.entity.impl;

import com.renata.demoartifactor.persistance.entity.Entity;
import java.time.LocalDate;
import java.util.UUID;

public class Transaction extends Entity implements Comparable<Transaction> {

    public String type; //купівля, продаж, обмін
    public LocalDate date;
    //public Item item; // item used in transaction
    // private String timeFormat = "dd-MM-yyyy 'at' HH:mm:ss";
    public double amount;
    public User user; //user who made the transaction

    Transaction(UUID id, String type, LocalDate date, double amount, User user) {
        super(id);
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public User getUser() {
        return user;
    }

    // add setters

    // add TransactionManager for logging all the transactions
    // TransactionHistory => save them to a file
    // admin will have access to this file
    // see OOP PR4


    @Override
    public int compareTo(Transaction o) {
        return this.date.compareTo(o.date);
    }
}
