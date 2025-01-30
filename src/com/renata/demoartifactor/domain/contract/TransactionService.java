package com.renata.demoartifactor.domain.contract;

import com.renata.demoartifactor.domain.Service;
import com.renata.demoartifactor.persistance.entity.impl.Transaction;
import java.time.LocalDate;
import java.util.Set;

public interface TransactionService extends Service<Transaction> {

    Set<Transaction> getAllByDate(LocalDate date); // check
}
