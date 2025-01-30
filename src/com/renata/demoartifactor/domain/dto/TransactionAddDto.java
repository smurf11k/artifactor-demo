package com.renata.demoartifactor.domain.dto;

import com.renata.demoartifactor.persistance.entity.Entity;
import java.util.UUID;

public final class TransactionAddDto extends Entity {

    private TransactionAddDto(UUID id) {
        super(id);
    }
}
