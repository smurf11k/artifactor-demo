package com.renata.demoartifactor.persistance.repository.contracts;

import com.renata.demoartifactor.persistance.entity.impl.Transaction;
import com.renata.demoartifactor.persistance.repository.Repository;
import java.time.LocalDate;
import java.util.Set;
import java.util.function.Predicate;

public interface TransactionRepository extends Repository<Transaction> {

    Set<Transaction> findByDate(LocalDate date);

    Set<Transaction> findAll(Predicate<Transaction> filter);
}
