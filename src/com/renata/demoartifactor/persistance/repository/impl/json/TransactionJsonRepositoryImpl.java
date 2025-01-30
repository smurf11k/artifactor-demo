package com.renata.demoartifactor.persistance.repository.impl.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.renata.demoartifactor.persistance.entity.impl.Transaction;
import com.renata.demoartifactor.persistance.repository.contracts.TransactionRepository;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

final class TransactionJsonRepositoryImpl extends GenericJsonRepository<Transaction>
    implements TransactionRepository {

    TransactionJsonRepositoryImpl(Gson gson) {
        super(gson, JsonPathFactory.TRANSACTIONS.getPath(), TypeToken
            .getParameterized(Set.class, Transaction.class)
            .getType());
    }

    @Override
    public Set<Transaction> findByDate(LocalDate date) {
        return entities.stream()
            .filter(item -> date.equals(item.getDate()))
            .collect(Collectors.toSet());
    }
}
