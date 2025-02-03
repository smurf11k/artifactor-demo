package com.renata.demoartifactor.domain.contract;

import com.renata.demoartifactor.domain.Reportable;
import com.renata.demoartifactor.domain.Service;
import com.renata.demoartifactor.domain.dto.TransactionAddDto;
import com.renata.demoartifactor.persistance.entity.impl.Transaction;
import java.time.LocalDate;
import java.util.Set;

public interface TransactionService extends Service<Transaction>, Reportable<Transaction> {

    Set<Transaction> getAllByDate(LocalDate date);

    Transaction add(TransactionAddDto transactionAddDto);
}
