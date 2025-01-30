package com.renata.demoartifactor.domain.impl;

import com.renata.demoartifactor.domain.contract.TransactionService;
import com.renata.demoartifactor.persistance.entity.impl.Transaction;
import com.renata.demoartifactor.persistance.repository.contracts.TransactionRepository;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

final class TransactionServiceImpl extends GenericService<Transaction> implements
    TransactionService {

    private final TransactionRepository transactionRepository;

    TransactionServiceImpl(TransactionRepository transactionRepository) {
        super(transactionRepository);
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Set<Transaction> getAllByDate(LocalDate date) {
        return new TreeSet<>(transactionRepository.findByDate(date));
    }

    @Override
    public Set<Transaction> getAll() {
        return getAll(c -> true);
    }

    @Override
    public Set<Transaction> getAll(Predicate<Transaction> filter) {
        return new TreeSet<>(transactionRepository.findAll(filter));
    }

    //TODO add somewhere here a method for saving the transaction history into a .xls file
}
