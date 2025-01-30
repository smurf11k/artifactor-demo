package com.renata.demoartifactor.persistance.repository.contracts;

import com.renata.demoartifactor.persistance.entity.impl.Transaction;
import com.renata.demoartifactor.persistance.repository.Repository;
import java.time.LocalDate;
import java.util.Set;

public interface TransactionRepository extends Repository<Transaction> {

    Set<Transaction> findByDate(LocalDate date);
}
