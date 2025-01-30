package com.renata.demoartifactor.domain.dto;

import com.renata.demoartifactor.persistance.entity.Entity;
import java.util.UUID;

public final class ItemAddDto extends Entity {

    private ItemAddDto(UUID id) {
        super(id);
    }
}
