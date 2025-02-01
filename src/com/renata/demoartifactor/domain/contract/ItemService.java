package com.renata.demoartifactor.domain.contract;

import com.renata.demoartifactor.domain.Service;
import com.renata.demoartifactor.domain.dto.ItemAddDto;
import com.renata.demoartifactor.domain.dto.ItemUpdateDto;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import java.util.List;
import java.util.UUID;

public interface ItemService extends Service<Item> {

    Item add(ItemAddDto itemAddDto);

    List<Item> getAllByCollection(AntiqueCollection collection);

    void update(ItemUpdateDto itemUpdateDto);

    void delete(UUID id);
}
